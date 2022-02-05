package net.immortaldevs.sar.test.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.base.client.ComponentModel;

@Environment(EnvType.CLIENT)
public final class TestComponentModels {
    public static final ComponentModel POTATO = new ComponentModel("sar", "potato");
    public static final ComponentModel FILLED_POTATO = new ComponentModel("sar", "filled_potato");
    public static final ComponentModel CHEESE = new ComponentModel("sar", "cheese");
    public static final ComponentModel POTATO_CHEESE = new ComponentModel("sar", "potato_cheese");
    public static final ComponentModel NITROGLYCERIN = new ComponentModel("sar", "nitroglycerin");
    public static final ComponentModel POTATO_NITROGLYCERIN = new ComponentModel("sar", "potato_nitroglycerin");

    public static void init() {

    }
}
