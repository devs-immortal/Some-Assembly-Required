package net.immortaldevs.sar.base;

import net.fabricmc.api.ModInitializer;
import net.immortaldevs.sar.test.TestComponents;
import net.immortaldevs.sar.test.TestItems;
import net.immortaldevs.sar.test.TestRecipeSerialisers;
import net.minecraft.util.Identifier;

public final class Sar implements ModInitializer {
    public static final String SAR = "sar";

    @Override
    public void onInitialize() {
        TestItems.init();
        TestComponents.init();
        TestRecipeSerialisers.init();
    }

    public static Identifier id(String path) {
        return new Identifier(SAR, path);
    }
}
