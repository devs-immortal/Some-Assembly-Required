package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public final class HashModifierMap implements ModifierMap {
    private final Map<Class<? extends Modifier>, Modifier> entries = new HashMap<>();

    public Collection<Modifier> entries() {
        return entries.values();
    }

    @Override
    public <T extends Modifier> void put(Class<T> key, T value) {
        entries.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Modifier> @Nullable T get(Class<T> key) {
        return (T) entries.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Modifier> T getOr(Class<T> key, T or) {
        return (T) entries.getOrDefault(key, or);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Modifier> void merge(Class<T> key, T value, BiFunction<T, T, T> remappingFunction) {
        entries.merge(key, value, (a, b) -> remappingFunction.apply((T) a, (T) b));
    }

    @Override
    public <T extends Modifier> void putIfAbsent(Class<T> key, T value) {
        entries.putIfAbsent(key, value);
    }

    @Override
    public boolean contains(Class<? extends Modifier> key) {
        return entries.containsKey(key);
    }
}
