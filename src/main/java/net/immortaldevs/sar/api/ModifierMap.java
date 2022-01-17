package net.immortaldevs.sar.api;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("unused")
public interface ModifierMap {
    <T extends Modifier> void put(Class<T> key, T value);

    <T extends Modifier> @Nullable T get(Class<T> key);

    <T extends Modifier> T getOr(Class<T> key, T or);

    <T extends Modifier> void append(Class<T> key, T value, Function<T, T> appender);

    <T extends Modifier> void putIfAbsent(Class<T> key, T value);

    boolean contains(Class<? extends Modifier> key);
}
