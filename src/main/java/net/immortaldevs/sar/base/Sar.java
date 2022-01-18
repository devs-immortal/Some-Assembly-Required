package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Component;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Sar implements ModInitializer {
    public static final String SAR = "sar";
    @Override
    public void onInitialize() {
        Registry.register(SarRegistries.COMPONENT,
                new Identifier(SAR, "unknown"),
                Component.UNKNOWN);
    }

    public static Identifier id(String path) {
        return new Identifier(SAR, path);
    }
}
