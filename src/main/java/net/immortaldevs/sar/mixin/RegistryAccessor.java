package net.immortaldevs.sar.mixin;

import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;

@SuppressWarnings("unused")
@Mixin(Registry.class)
public interface RegistryAccessor {
    @Invoker
    static <T> RegistryKey<Registry<T>> callCreateRegistryKey(String registryId) {
        throw new Error();
    }

    @Invoker
    static <T> DefaultedRegistry<T> callCreate(RegistryKey<? extends Registry<T>> key, String defaultId, Function<T, RegistryEntry.Reference<T>> valueToEntryFunction, Registry.DefaultEntryGetter<T> defaultEntryGetter) {
        throw new Error();
    }
}
