package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface ItemRendererModifier extends Modifier {
    void applyItemRendererModifier(
            MultiVertexConsumerProvider provider,
            ItemStack stack,
            MatrixStack matrices,
            int light,
            int overlay
    );

    static ItemRendererModifier transform(ItemRendererModifier modifier, Consumer<MatrixStack> transformation) {
        return (provider, stack, matrices, light, overlay) -> {
            matrices.push();
            transformation.accept(matrices);
            modifier.applyItemRendererModifier(provider, stack, matrices, light, overlay);
            matrices.pop();
        };
    }

    static ItemRendererModifier of(Supplier<BakedModel> model) {
        return (provider, stack, matrices, light, overlay) ->
                ClientUtil.renderModel(provider.get(MultiRenderLayer.ENTITY_TRANSLUCENT_CULL_GHOST),
                        model.get(), matrices.peek(), light, overlay);
    }

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(ItemRendererModifier.class, this, (a, b) ->
                (provider, stack, matrices, light, overlay) -> {
                    a.applyItemRendererModifier(provider, stack, matrices, light, overlay);
                    b.applyItemRendererModifier(provider, stack, matrices, light, overlay);
                });
    }
}
