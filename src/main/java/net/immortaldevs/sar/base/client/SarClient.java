package net.immortaldevs.sar.base.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.test.client.TestClientComponents;
import net.immortaldevs.sar.test.client.TestComponentModels;

@Environment(EnvType.CLIENT)
public final class SarClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TestClientComponents.init();
        TestComponentModels.init();
    }
}
