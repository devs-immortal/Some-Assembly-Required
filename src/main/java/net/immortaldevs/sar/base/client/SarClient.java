package net.immortaldevs.sar.base.client;

import net.fabricmc.api.ClientModInitializer;
import net.immortaldevs.sar.base.Sar;
import net.immortaldevs.sar.test.client.TestClientComponents;
import net.immortaldevs.sar.test.client.TestComponentModels;

public class SarClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (Sar.TESTING_ENABLED) {
            TestClientComponents.init();
            TestComponentModels.init();
        }
    }
}
