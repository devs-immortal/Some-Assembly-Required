package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;

@Environment(EnvType.CLIENT)
public interface MultiVertexConsumerProvider {
    VertexConsumer get(MultiRenderLayer layer);
    
    VertexConsumer get(RenderLayer layer);

    VertexConsumer getImmediate(MultiRenderLayer layer);

    VertexConsumer getImmediate(RenderLayer layer);
}
