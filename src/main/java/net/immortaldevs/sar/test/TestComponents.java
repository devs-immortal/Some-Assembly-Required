package net.immortaldevs.sar.test;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.base.SarRegistries;
import net.minecraft.util.registry.Registry;

import static net.immortaldevs.sar.base.Sar.id;

public final class TestComponents {
    public static final Component POTATO = new PotatoComponent();
    public static final Component CHEESE = new CheeseComponent();
    public static final Component NITROGLYCERIN = new NitroglycerinComponent();

    public static void init() {
        Registry.register(SarRegistries.COMPONENT, id("potato"), POTATO);
        Registry.register(SarRegistries.COMPONENT, id("cheese"), CHEESE);
        Registry.register(SarRegistries.COMPONENT, id("nitroglycerin"), NITROGLYCERIN);
    }
}
