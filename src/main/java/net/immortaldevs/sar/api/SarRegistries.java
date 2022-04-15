package net.immortaldevs.sar.api;

import net.immortaldevs.sar.mixin.RegistryAccessor;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public final class SarRegistries {
    public static final RegistryKey<Registry<Component>> COMPONENT_KEY =
            RegistryAccessor.callCreateRegistryKey("sar:component");

    public static final Registry<Component> COMPONENT =
            RegistryAccessor.callCreate(COMPONENT_KEY,
                    "sar:air",
                    Component::getRegistryEntry,
                    registry -> Component.AIR);
}
