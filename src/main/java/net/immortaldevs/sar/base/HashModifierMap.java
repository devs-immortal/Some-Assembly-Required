package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class HashModifierMap implements ModifierMap {
    private final Map<Class<? extends Modifier>, Modifier> entries = new HashMap<>();

    @Override
    public <T extends Modifier> void put(Class<T> key, T value) {
        entries.put(key, value);
    }

    @Override
    public <T extends Modifier> @Nullable T get(Class<T> key) {
        Modifier value = entries.get(key);
        return key.cast(value);
    }

    @Override
    public <T extends Modifier> T getOr(Class<T> key, T or) {
        T value = this.get(key);
        return value == null ? or : value;
    }

    @Override
    public <T extends Modifier> void append(Class<T> key, T value, Function<T, T> appender) {
        T entry = this.get(key);
        entries.put(key, entry == null ? value : appender.apply(entry));
    }

    @Override
    public <T extends Modifier> void putIfAbsent(Class<T> key, T value) {
        if (!entries.containsKey(key)) {
            entries.put(key, value);
        }
    }

    @Override
    public boolean contains(Class<? extends Modifier> key) {
        return entries.containsKey(key);
    }
}
