package net.immortaldevs.sar.mixin;

import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mixin(Registry.class)
public interface RegistryAccessor {
    @Invoker
    static <T> RegistryKey<Registry<T>> callCreateRegistryKey(String registryId) {
        throw new Error();
    }

    @Invoker
    static <T> Registry<T> callCreate(RegistryKey<? extends Registry<T>> key, Registry.DefaultEntryGetter<T> defaultEntryGetter) {
        throw new Error();
    }
}
