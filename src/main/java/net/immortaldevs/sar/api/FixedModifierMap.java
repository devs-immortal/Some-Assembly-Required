package net.immortaldevs.sar.api;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface FixedModifierMap {
    <T extends Modifier> @Nullable T get(Class<T> key);

    <T extends Modifier> T getOrDefault(Class<T> key, T or);

    boolean contains(Class<? extends Modifier> key);
}
