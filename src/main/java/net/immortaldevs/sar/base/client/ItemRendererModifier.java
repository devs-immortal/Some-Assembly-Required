package net.immortaldevs.sar.base.client;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

@FunctionalInterface
public interface ItemRendererModifier extends Modifier {
    void render(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay);

    default ItemRendererModifier transform(Consumer<MatrixStack> consumer) {
        return (stack, matrices, vertexConsumers, light, overlay) -> {
            matrices.push();
            consumer.accept(matrices);
            this.render(stack, matrices, vertexConsumers, light, overlay);
            matrices.pop();
        };
    }

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(ItemRendererModifier.class, this, (a, b) ->
                (stack, matrices, vertexConsumers, light, overlay) -> {
                    a.render(stack, matrices, vertexConsumers, light, overlay);
                    b.render(stack, matrices, vertexConsumers, light, overlay);
                });
    }
}
