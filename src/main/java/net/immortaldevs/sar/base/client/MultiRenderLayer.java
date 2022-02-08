package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface MultiRenderLayer {
    MultiRenderLayer ENTITY_TRANSLUCENT_CULL_GHOST = vertexConsumers -> VertexConsumers.union(
            vertexConsumers.getBuffer(SarTexturedRenderLayers.TRANSLUCENT_GHOST),
            vertexConsumers.getBuffer(SarTexturedRenderLayers.ENTITY_TRANSLUCENT_CULL_LAYERED));

    VertexConsumer get(VertexConsumerProvider vertexConsumers);
}