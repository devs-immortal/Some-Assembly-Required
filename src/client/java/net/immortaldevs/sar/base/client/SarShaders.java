package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Shader;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class SarShaders {
    public static @Nullable Shader translucentGhost;
    public static @Nullable Shader translucentOverlay;

    public static @Nullable Shader getTranslucentGhost() {
        return translucentGhost;
    }

    public static @Nullable Shader getTranslucentOverlay() {
        return translucentOverlay;
    }
}
