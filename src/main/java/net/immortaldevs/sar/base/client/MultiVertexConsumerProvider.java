package net.immortaldevs.sar.base.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;

public interface MultiVertexConsumerProvider {
    VertexConsumer get(MultiRenderLayer layer);
    
    VertexConsumer get(RenderLayer layer);

    VertexConsumer getImmediate(MultiRenderLayer layer);

    VertexConsumer getImmediate(RenderLayer layer);
}
