package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface SpriteExt {
    int sar$getAtlasWidth();

    int sar$getAtlasHeight();
}
