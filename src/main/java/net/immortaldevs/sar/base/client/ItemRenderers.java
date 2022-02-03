package net.immortaldevs.sar.base.client;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

@FunctionalInterface
public interface ItemRenderers {
    ItemRenderers ENTITY_TRANSLUCENT_CULL_LAYERED = function ->
            (stack, matrices, vertexConsumers, light, overlay) -> {
                MatrixStack.Entry entry = matrices.peek();
                BakedModel bakedModel = function.apply(stack);

                VertexConsumer ghost = vertexConsumers.getBuffer(SarTexturedRenderLayers.TRANSLUCENT_GHOST);
                Util.getQuads(bakedModel, bakedQuad ->
                        ghost.quad(entry, bakedQuad, 1f, 1f, 1f, light, overlay));

                VertexConsumer layered = vertexConsumers.getBuffer(SarTexturedRenderLayers.ENTITY_TRANSLUCENT_CULL_LAYERED);
                Util.getQuads(bakedModel, bakedQuad ->
                        layered.quad(entry, bakedQuad, 1f, 1f, 1f, light, overlay));
            };

    ItemRendererModifier get(Function<ItemStack, BakedModel> function);
}
