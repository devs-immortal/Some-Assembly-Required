package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class SarRenderLayers extends RenderLayer {
    public static final Function<Identifier, RenderLayer> ENTITY_TRANSLUCENT_CULL_LAYERED = Util.memoize(id ->
            RenderLayer.of("sar_entity_translucent_cull_layered",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                    VertexFormat.DrawMode.QUADS,
                    256,
                    true,
                    false,
                    MultiPhaseParameters.builder()
                            .shader(new RenderPhase.Shader(SarShaders::getTranslucentOverlay))
                            .texture(new Texture(id, false, false))
                            .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                            .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                            .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                            .writeMaskState(RenderPhase.COLOR_MASK)
                            .build(false)));

    public static final Function<Identifier, RenderLayer> TRANSLUCENT_GHOST = Util.memoize(id ->
            RenderLayer.of("sar_translucent_ghost",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                    VertexFormat.DrawMode.QUADS,
                    256,
                    false,
                    false,
                    MultiPhaseParameters.builder()
                            .shader(new RenderPhase.Shader(SarShaders::getTranslucentGhost))
                            .texture(new Texture(id, false, false))
                            .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                            .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                            .writeMaskState(RenderPhase.DEPTH_MASK)
                            .build(true)));

    private SarRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }
}
