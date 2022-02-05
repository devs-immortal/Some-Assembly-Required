package net.immortaldevs.sar.base.client;

import net.fabricmc.api.ClientModInitializer;
import net.immortaldevs.sar.test.client.TestClientComponents;
import net.immortaldevs.sar.test.client.TestComponentModels;

public class SarClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TestClientComponents.init();
        TestComponentModels.init();
    }
}
