package net.immortaldevs.sar.api;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ModifierMap extends FixedModifierMap {
    <T extends Modifier> T put(Class<T> key, T value);

    <T extends Modifier> T merge(Class<T> key, T value, BiFunction<T, T, T> remappingFunction);

    <T extends Modifier> T putIfAbsent(Class<T> key, T value);

    <T extends Modifier> T computeIfAbsent(Class<T> key, Function<Class<T>, T> mappingFunction);

    <T extends Modifier> T computeIfPresent(Class<T> key, BiFunction<Class<T>, T, T> remappingFunction);

    <T extends Modifier> T compute(Class<T> key, BiFunction<Class<T>, T, T> remappingFunction);

    <T extends Modifier> T replace(Class<T> key, T value);

    <T extends Modifier> T remove(Class<T> key);
}
