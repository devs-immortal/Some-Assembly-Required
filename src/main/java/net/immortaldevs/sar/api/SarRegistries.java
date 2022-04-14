package net.immortaldevs.sar.api;

import net.immortaldevs.sar.mixin.RegistryAccessor;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import static net.immortaldevs.sar.impl.Sar.id;

public final class SarRegistries {
    public static final RegistryKey<Registry<Component>> COMPONENT_KEY =
            RegistryAccessor.callCreateRegistryKey("sar:component");

    public static final Registry<Component> COMPONENT =
            RegistryAccessor.callCreate(COMPONENT_KEY, registry -> Component.AIR);

    static {
        Registry.register(COMPONENT,
                id("air"),
                Component.AIR);

        Registry.register(COMPONENT,
                id("root"),
                Component.ROOT);
    }
}
