package net.immortaldevs.sar.test.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.immortaldevs.sar.base.Sar;
import net.immortaldevs.sar.base.client.RenderLayerFactory;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Matrix4f;

public final class TestRenderLayers extends RenderLayer {
    private static final Texturing CUM_TEXTURING = new Texturing("cum",
            () -> RenderSystem.setTextureMatrix(Matrix4f.scale(8f, 8f, 8f)),
            RenderSystem::resetTextureMatrix);

    public static final RenderLayer CUM = RenderLayerFactory.get().create("cum",
            VertexFormats.POSITION_TEXTURE,
            VertexFormat.DrawMode.QUADS,
            256,
            false,
            false,
            MultiPhaseParameters.builder()
                    .shader(RenderPhase.DIRECT_GLINT_SHADER)
                    .texture(new RenderPhase.Texture(Sar.id("textures/misc/cum.png"), false, false))
                    .writeMaskState(COLOR_MASK)
                    .cull(DISABLE_CULLING)
                    .depthTest(EQUAL_DEPTH_TEST)
                    .transparency(GLINT_TRANSPARENCY)
                    .texturing(CUM_TEXTURING)
                    .build(false));

    private TestRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }
}
