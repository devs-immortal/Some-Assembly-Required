package net.immortaldevs.sar.test.client;

import net.immortaldevs.sar.base.client.ClientComponents;
import net.immortaldevs.sar.base.client.ComponentModel;
import net.immortaldevs.sar.base.client.CrackParticleSpriteModifier;
import net.immortaldevs.sar.base.client.ItemRendererModifier;
import net.immortaldevs.sar.test.PotatoComponent;
import net.immortaldevs.sar.test.TestComponents;

public final class TestClientComponents {
    public static void init() {
        ClientComponents.register(TestComponents.POTATO, data -> {
            ComponentModel potato = data.containsChild("filling")
                    ? TestComponentModels.FILLED_POTATO
                    : TestComponentModels.POTATO;

            data.addModifier(ItemRendererModifier.of(potato));
            data.addModifier(CrackParticleSpriteModifier.of(potato));
            data.loadChild("filling");
        });

        ClientComponents.register(TestComponents.CHEESE, data -> {
            ComponentModel cheese =
                    data.parent().isPresent() && data.parent().get().type() instanceof PotatoComponent
                            ? TestComponentModels.POTATO_CHEESE
                            : TestComponentModels.CHEESE;

            data.addModifier(ItemRendererModifier.of(cheese));
            data.addModifier(CrackParticleSpriteModifier.of(cheese));
        });

        ClientComponents.register(TestComponents.NITROGLYCERIN, data -> {
            ComponentModel nitroglycerin =
                    data.parent().isPresent() && data.parent().get().type() instanceof PotatoComponent
                            ? TestComponentModels.POTATO_NITROGLYCERIN
                            : TestComponentModels.NITROGLYCERIN;

            data.addModifier(ItemRendererModifier.of(nitroglycerin));
            data.addModifier(CrackParticleSpriteModifier.of(nitroglycerin));
        });
    }
}
