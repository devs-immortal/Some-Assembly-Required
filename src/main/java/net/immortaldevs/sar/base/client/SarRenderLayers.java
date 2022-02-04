package net.immortaldevs.sar.base.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public final class SarRenderLayers extends RenderLayer {
    public static final Function<Identifier, RenderLayer> ENTITY_TRANSLUCENT_CULL_LAYERED = Util.memoize(id ->
            RenderLayerFactory.get().create("sar_entity_translucent_cull_layered",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                    VertexFormat.DrawMode.QUADS,
                    256,
                    true,
                    true,
                    MultiPhaseParameters.builder()
                            .shader(RenderPhase.ENTITY_TRANSLUCENT_CULL_SHADER)
                            .texture(new Texture(id, false, false))
                            .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                            .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                            .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                            .writeMaskState(RenderPhase.COLOR_MASK)
                            .build(false)));

    public static final Function<Identifier, RenderLayer> TRANSLUCENT_GHOST = Util.memoize(id ->
            RenderLayerFactory.get().create("sar_translucent_ghost",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                    VertexFormat.DrawMode.QUADS,
                    256,
                    false,
                    false,
                    MultiPhaseParameters.builder()
                            .shader(new RenderPhase.Shader(SarShaders::getTranslucentGhost))
                            .texture(new Texture(id, false, false))
                            .writeMaskState(RenderPhase.DEPTH_MASK)
                            .cull(DISABLE_CULLING)
                            .transparency(GLINT_TRANSPARENCY)
                            .build(true)));

    private SarRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }
}
