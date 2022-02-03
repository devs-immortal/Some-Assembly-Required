package net.immortaldevs.sar.base.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.PlayerScreenHandler;

public final class SarTexturedRenderLayers {
    public static final RenderLayer TRANSLUCENT_GHOST =
            SarRenderLayers.TRANSLUCENT_GHOST.apply(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);

    public static final RenderLayer ENTITY_TRANSLUCENT_CULL_LAYERED =
            SarRenderLayers.ENTITY_TRANSLUCENT_CULL_LAYERED.apply(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
}
