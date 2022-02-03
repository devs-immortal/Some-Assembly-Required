package net.immortaldevs.sar.base;

import net.fabricmc.api.ModInitializer;
import net.immortaldevs.sar.test.PotatoRecipe;
import net.immortaldevs.sar.test.TestComponents;
import net.immortaldevs.sar.test.TestItems;
import net.minecraft.util.Identifier;

public final class Sar implements ModInitializer {
    public static final String SAR = "sar";
    public static final boolean TESTING_ENABLED = true;

    @Override
    public void onInitialize() {
        if (TESTING_ENABLED) {
            TestItems.init();
            TestComponents.init();
            PotatoRecipe.init();
        }
    }

    public static Identifier id(String path) {
        return new Identifier(SAR, path);
    }
}
