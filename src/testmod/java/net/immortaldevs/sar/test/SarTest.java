package net.immortaldevs.sar.test;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public final class SarTest implements ModInitializer {
    public static final String SAR_TEST = "sar_test";

    @Override
    public void onInitialize() {
        SarTestComponents.init();
    }

    public static Identifier id(String path) {
        return new Identifier(SAR_TEST, path);
    }
}
