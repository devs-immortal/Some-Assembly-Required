package net.immortaldevs.sar.base.client;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface VertexConsumerModifier extends Modifier {
    VertexConsumer apply(
            BakedModel model,
            ItemStack stack,
            MatrixStack matrices,
            VertexConsumerProvider provider,
            VertexConsumer vertices);

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(VertexConsumerModifier.class, this, (a, b) ->
                (model, stack, matrices, provider, vertices) ->
                        b.apply(model, stack, matrices, provider,
                                a.apply(model, stack, matrices, provider, vertices)));
    }
}
