package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Shader;

@Environment(EnvType.CLIENT)
public class SarShaders {
    public static Shader translucentGhost;

    public static Shader getTranslucentGhost() {
        return translucentGhost;
    }
}
