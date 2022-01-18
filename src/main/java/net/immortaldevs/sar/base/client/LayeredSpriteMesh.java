package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.texture.Sprite;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class LayeredSpriteMesh {
    public static Mesh create(Layer[] layers) {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        if (renderer == null) throw new UnsupportedOperationException(
                "Layered sprite models require the fabric rendering api, but no implementation was found.");

        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        int resolutionX = 16, resolutionY = 16;
        int minOffsetX = 0, maxOffsetX = 0, minOffsetY = 0, maxOffsetY = 0;
        for (int i = 0; i < layers.length; i++) {
            Layer layer = layers[i];
            face(emitter, layer, false, i);
            face(emitter, layer, true, i);

            if (layer.sprite.getWidth() > resolutionX) {
                resolutionX = layer.sprite.getWidth();
            }

            if (layer.sprite.getHeight() > resolutionY) {
                resolutionY = layer.sprite.getHeight();
            }

            if (layer.x < minOffsetX) {
                minOffsetX = layer.x;
            } else if (layer.x > maxOffsetX) {
                maxOffsetX = layer.x;
            }

            if (layer.y < minOffsetY) {
                minOffsetY = layer.y;
            } else if (layer.y > maxOffsetY) {
                maxOffsetY = layer.y;
            }
        }

        int xStart = (minOffsetX * resolutionX) >> 4;
        int xEnd = ((maxOffsetX * resolutionX) >> 4) + resolutionX;
        int yStart = (minOffsetY * resolutionY) >> 4;
        int yEnd = ((maxOffsetY * resolutionY) >> 4) + resolutionY;
        for (int x = xStart; x < xEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                Layer topmostLayer = getTopmostLayer(layers, resolutionX, resolutionY, x, y);
                if (topmostLayer == null) continue;

                tryDrawEdge(layers, topmostLayer, emitter, 0, resolutionX, resolutionY, x, y, x - 1, y);
                tryDrawEdge(layers, topmostLayer, emitter, 1, resolutionX, resolutionY, x, y, x + 1, y);
                tryDrawEdge(layers, topmostLayer, emitter, 2, resolutionX, resolutionY, x, y, x, y - 1);
                tryDrawEdge(layers, topmostLayer, emitter, 3, resolutionX, resolutionY, x, y, x, y + 1);
            }
        }

        return builder.build();
    }

    private static @Nullable Layer getTopmostLayer(Layer[] layers, int resolutionX, int resolutionY, int x, int y) {
        int i = layers.length;
        while (true) {
            i--;
            if (!isPixelTransparent(layers[i], resolutionX, resolutionY, x, y)) {
                return layers[i];
            }
            if (i <= 0) return null;
        }
    }

    private static boolean isPixelTransparent(Layer layer, int resolutionX, int resolutionY, int x, int y) {
        int layerX = ((x << 4) / resolutionX) - layer.x;
        int layerY = ((y << 4) / resolutionY) - layer.y;

        return layerX < 0
                || layerX >= 16
                || layerY < 0
                || layerY >= 16
                || layer.sprite.isPixelTransparent(0, layerX, layerY);
    }

    private static void tryDrawEdge(Layer[] layers, Layer topmostLayer, QuadEmitter emitter, int edge,
                                    int resolutionX, int resolutionY, int x, int y, int offsetX, int offsetY) {
        if (getTopmostLayer(layers, resolutionX, resolutionY, offsetX, offsetY) != null) {
            return;
        }

        float oneX = 1f / resolutionX;
        float oneY = 1f / resolutionY;
        float left = x * oneX;
        float right = left + oneX;
        float bottom = (resolutionY - y) * oneY;
        float top = bottom - oneY;

        switch (edge) {
            case 0 -> {
                emitter.pos(0, left, bottom, 0.53125f);
                emitter.pos(1, left, bottom, 0.46875f);
                emitter.pos(2, left, top, 0.46875f);
                emitter.pos(3, left, top, 0.53125f);
            }
            case 1 -> {
                emitter.pos(0, right, top, 0.53125f);
                emitter.pos(1, right, top, 0.46875f);
                emitter.pos(2, right, bottom, 0.46875f);
                emitter.pos(3, right, bottom, 0.53125f);
            }
            case 2 -> {
                emitter.pos(0, right, bottom, 0.53125f);
                emitter.pos(1, right, bottom, 0.46875f);
                emitter.pos(2, left, bottom, 0.46875f);
                emitter.pos(3, left, bottom, 0.53125f);
            }
            case 3 -> {
                emitter.pos(0, left, top, 0.53125f);
                emitter.pos(1, left, top, 0.46875f);
                emitter.pos(2, right, top, 0.46875f);
                emitter.pos(3, right, top, 0.53125f);
            }
            default -> throw new IllegalArgumentException();
        }

        float uMin = xToU(topmostLayer, x, resolutionX);
        float uMax = xToU(topmostLayer, x + 1, resolutionX);
        float vMin = yToV(topmostLayer, y, resolutionY);
        float vMax = yToV(topmostLayer, y + 1, resolutionY);

        emitter.sprite(0, 0, uMin, vMax);
        emitter.sprite(1, 0, uMin, vMin);
        emitter.sprite(2, 0, uMax, vMin);
        emitter.sprite(3, 0, uMax, vMax);

        emitter.spriteColor(0, 0, -1);
        emitter.spriteColor(1, 0, -1);
        emitter.spriteColor(2, 0, -1);
        emitter.spriteColor(3, 0, -1);

        emitter.emit();
    }

    private static float xToU(Layer layer, int x, int resolution) {
        return ((float) ((x << 4) / resolution) - layer.x + layer.sprite.getX())
                / ((SpriteExt) layer.sprite).sar$getAtlasWidth();
    }

    private static float yToV(Layer layer, int y, int resolution) {
        return ((float) ((y << 4) / resolution) - layer.y + layer.sprite.getY())
                / ((SpriteExt) layer.sprite).sar$getAtlasHeight();
    }

    /*
     * todo fix item sprite z fighting
     * Currently, a 0.000244140625 block offset is applied to each layer, as a temporary workaround to prevent
     * z fighting which works only at close ranges. A more permanent solution is needed.
     * When two quads perfectly overlap, no z fighting occurs and the faces are sorted in the order in which they are
     * added. Since vanilla items always have all layers in the same position, this is enough to prevent issues.
     * However, as soon as there is an offset, z fighting occurs.
     * Here are some options I've thought of to fix this permanently:
     *
     * - Add the offset into the AptitudeSprite object, allowing all possible offsets of the sprite to be known during
     *   sprite atlas creation, and add a sprite to the atlas for each offset. Each layer's quads can then be in the
     *   same position. However, this would only support a list of pre-made sprite offsets, which duesn't provide
     *   the current level of freedom.
     *
     * - Keep the quad vertices in the same place, but adjust their uv coordinates to move the sprite. This works fine,
     *   but would require some empty space around the sprite in the atlas to prevent other textures from bleeding in
     *   when the sprite is moved. For example, if a 16*16 sprite were placed into a 32*32 texture with 8 pixels of
     *   blank space on all four sides, the sprite could be safely translated 8 pixels in any direction without other
     *   sprites bleeding in. This works fine, but it causes tool part sprites to occupy 4* the texture space, which is
     *   quite wasteful. A separate atlas could instead be used with 4 pixels of blank space around every sprite,
     *   allowing them to share space with their neighbours, but it still isn't pretty.
     *
     * - Dynamically generate and store combined textures. Not much to say on this; I don't imagine it being easy.
     *
     * - Work out why no z fighting occurs when the quads are in the same place, and try to learn from it.
     *
     * I've given up for now. The small z offset works well enough for testing.
     */
    private static void face(QuadEmitter emitter, Layer layer, boolean back, int index) {
        float left, right, z, uMin, uMax;
        if (back) {
            right = (left = layer.x * 0.0625f) + 1;
            z = 0.46875f - (index * 0.000244140625f);
            uMin = layer.sprite.getMinU();
            uMax = layer.sprite.getMaxU();
        } else {
            left = (right = layer.x * 0.0625f) + 1;
            z = 0.53125f + (index * 0.000244140625f);
            uMin = layer.sprite.getMaxU();
            uMax = layer.sprite.getMinU();
        }

        float top = -layer.y * 0.0625f;
        float bottom = top + 1;

        emitter.pos(0, left, top, z);
        emitter.pos(1, left, bottom, z);
        emitter.pos(2, right, bottom, z);
        emitter.pos(3, right, top, z);

        float vMin = layer.sprite.getMinV();
        float vMax = layer.sprite.getMaxV();

        emitter.sprite(0, 0, uMin, vMax);
        emitter.sprite(1, 0, uMin, vMin);
        emitter.sprite(2, 0, uMax, vMin);
        emitter.sprite(3, 0, uMax, vMax);

        emitter.spriteColor(0, 0, -1);
        emitter.spriteColor(1, 0, -1);
        emitter.spriteColor(2, 0, -1);
        emitter.spriteColor(3, 0, -1);

        emitter.emit();
    }

    @Environment(EnvType.CLIENT)
    public record Layer(Sprite sprite, int x, int y) {
    }
}
