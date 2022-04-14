package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Shader;

@Environment(EnvType.CLIENT)
public final class SarShaders {
    public static Shader translucentGhost;
    public static Shader translucentOverlay;

    public static Shader getTranslucentGhost() {
        return translucentGhost;
    }

    public static Shader getTranslucentOverlay() {
        return translucentOverlay;
    }
}
