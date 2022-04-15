package net.immortaldevs.sar.impl;

import net.fabricmc.api.ModInitializer;
import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.SarRegistries;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class Sar implements ModInitializer {
    public static final String SAR = "sar";

    @Override
    public void onInitialize() {
        Registry.register(SarRegistries.COMPONENT,
                id("air"),
                Component.AIR);
    }

    public static Identifier id(String path) {
        return new Identifier(SAR, path);
    }
}
