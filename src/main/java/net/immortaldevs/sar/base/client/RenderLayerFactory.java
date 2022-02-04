package net.immortaldevs.sar.base.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;

public final class RenderLayerFactory extends RenderLayer {
    private static Factory factory;

    private RenderLayerFactory(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static Factory get() {
        return factory;
    }

    public static void init(Factory factory) {
        if (RenderLayerFactory.factory == null) {
            RenderLayerFactory.factory = factory;
            return;
        }
        throw new IllegalStateException();
    }

    public interface Factory {
        RenderLayer create(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, RenderLayer.MultiPhaseParameters phases);
    }
}