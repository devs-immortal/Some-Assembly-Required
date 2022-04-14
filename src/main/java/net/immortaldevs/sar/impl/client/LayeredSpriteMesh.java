package net.immortaldevs.sar.impl.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.texture.Sprite;
import org.jetbrains.annotations.Nullable;

/**
 * Creates a 3d item mesh resembling the vanilla {@link ItemModelGenerator}, but prevents duplicate faces when
 * multiple textures overlap. Currently unused due to changes to component rendering making it unnecessary,
 * but kept around in case someone has a use for it.
 */
@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
@Deprecated
public final class LayeredSpriteMesh {
    public static Mesh create(Layer[] layers) {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        if (renderer == null) throw new UnsupportedOperationException(
                "Layered sprite models require the fabric rendering api, but no implementation was found.");

        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        int resolutionX = 16, resolutionY = 16;
        int minOffsetX = 0, maxOffsetX = 0, minOffsetY = 0, maxOffsetY = 0;
        for (Layer layer : layers) {
            face(emitter, layer, false);
            face(emitter, layer, true);

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

    private static void face(QuadEmitter emitter, Layer layer, boolean back) {
        float left, right, z, uMin, uMax;
        if (back) {
            right = (left = layer.x * 0.0625f) + 1;
            z = 0.46875f;
            uMin = layer.sprite.getMinU();
            uMax = layer.sprite.getMaxU();
        } else {
            left = (right = layer.x * 0.0625f) + 1;
            z = 0.53125f;
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
    @Deprecated
    public record Layer(Sprite sprite, int x, int y) {
    }

    @Environment(EnvType.CLIENT)
    @Deprecated
    public interface SpriteExt {
        int sar$getAtlasWidth();

        int sar$getAtlasHeight();
    }
}
