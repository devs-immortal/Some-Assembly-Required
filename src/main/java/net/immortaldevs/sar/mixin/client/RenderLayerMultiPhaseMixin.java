package net.immortaldevs.sar.mixin.client;

import net.immortaldevs.sar.base.client.RenderLayerFactory;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("unused")
@Mixin(targets = "net/minecraft/client/render/RenderLayer$MultiPhase")
public class RenderLayerMultiPhaseMixin extends RenderLayer {
    private RenderLayerMultiPhaseMixin(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, RenderLayer.MultiPhaseParameters phases) {
        super(null, null, null, 0, false, false, null, null);
    }

    static {
        RenderLayerFactory.init(RenderLayerMultiPhaseMixin::new);
    }
}
