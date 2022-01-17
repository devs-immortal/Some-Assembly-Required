package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.mixin.RegistryAccessor;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class SarRegistries {
    public static final RegistryKey<Registry<Component>> COMPONENT_KEY =
            RegistryAccessor.callCreateRegistryKey("sar:component");

    public static final DefaultedRegistry<Component> COMPONENT =
            RegistryAccessor.callCreate(COMPONENT_KEY,
                    "sar:unknown", () -> Component.UNKNOWN);
}
