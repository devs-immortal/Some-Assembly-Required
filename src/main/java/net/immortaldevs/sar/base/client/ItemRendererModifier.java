package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface ItemRendererModifier extends Modifier {
    void render(
            MultiVertexConsumerProvider provider,
            ItemStack stack,
            MatrixStack matrices,
            int light,
            int overlay
    );

    static ItemRendererModifier of(Supplier<BakedModel> model) {
        return (provider, stack, matrices, light, overlay) ->
                ClientUtil.renderModel(provider.get(MultiRenderLayer.ENTITY_TRANSLUCENT_CULL_GHOST),
                        model.get(), matrices.peek(), light, overlay);
    }

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(ItemRendererModifier.class, this, (a, b) ->
                (provider, stack, matrices, light, overlay) -> {
                    a.render(provider, stack, matrices, light, overlay);
                    b.render(provider, stack, matrices, light, overlay);
                });
    }
}
