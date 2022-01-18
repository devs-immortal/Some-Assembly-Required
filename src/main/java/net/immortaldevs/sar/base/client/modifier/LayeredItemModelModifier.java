package net.immortaldevs.sar.base.client.modifier;

import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.immortaldevs.sar.api.ModifierMap;
import net.immortaldevs.sar.base.client.LayeredSpriteMesh;
import net.immortaldevs.sar.base.client.SarSprite;
import net.immortaldevs.sar.base.client.LayeredSpriteMesh.Layer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class LayeredItemModelModifier implements ItemModelModifier {
    private final ModifierMap home;
    private Set<LayerInfo> layersSeen;
    private Mesh mesh;

    public LayeredItemModelModifier(ModifierMap home) {
        this.home = home;
    }

    public void emitQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        Set<LayerInfo> layersSeen = new HashSet<>();

        SpriteLayerModifier spriteLayerModifier = this.home.get(SpriteLayerModifier.class);
        if (spriteLayerModifier == null) return;

        spriteLayerModifier.apply((sprite, x, y, z) -> layersSeen.add(new LayerInfo(sprite, x, y, z)));

        if (!layersSeen.equals(this.layersSeen) || this.mesh == null) {
            this.layersSeen = layersSeen;

            this.mesh = LayeredSpriteMesh.create(layersSeen.stream()
                    .distinct()
                    .sorted((a, b) -> Float.compare(a.z, b.z))
                    .map(layerInfo -> new Layer(layerInfo.sprite.id.getSprite(),
                            layerInfo.x, layerInfo.y))
                    .toArray(Layer[]::new));
        }

        context.meshConsumer().accept(this.mesh);
    }

    @Environment(EnvType.CLIENT)
    private record LayerInfo(
            SarSprite sprite,
            int x,
            int y,
            float z
    ) {
    }
}
