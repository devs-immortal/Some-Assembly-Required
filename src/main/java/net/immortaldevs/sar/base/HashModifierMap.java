package net.immortaldevs.sar.base;

import com.google.common.base.MoreObjects;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class HashModifierMap implements ModifierMap {
    private final Reference2ReferenceMap<Class<? extends Modifier>, Modifier> entries
            = new Reference2ReferenceOpenHashMap<>();

    public Collection<Modifier> entries() {
        return this.entries.values();
    }

    @Override
    public <T extends Modifier> @Nullable T get(Class<T> key) {
        return (T) this.entries.get(key);
    }

    @Override
    public <T extends Modifier> T getOrDefault(Class<T> key, T or) {
        return (T) this.entries.getOrDefault(key, or);
    }

    @Override
    public boolean contains(Class<? extends Modifier> key) {
        return this.entries.containsKey(key);
    }

    @Override
    public <T extends Modifier> T put(Class<T> key, T value) {
        return (T) this.entries.put(key, value);
    }

    @Override
    public <T extends Modifier> T merge(Class<T> key, T value, BiFunction<T, T, T> remappingFunction) {
        return (T) this.entries.merge(key, value, (BiFunction) remappingFunction);
    }

    @Override
    public <T extends Modifier> T putIfAbsent(Class<T> key, T value) {
        return (T) this.entries.putIfAbsent(key, value);
    }

    @Override
    public <T extends Modifier> T computeIfAbsent(Class<T> key, Function<Class<T>, T> mappingFunction) {
        return (T) this.entries.computeIfAbsent(key, (Function) mappingFunction);
    }

    @Override
    public <T extends Modifier> T computeIfPresent(Class<T> key, BiFunction<Class<T>, T, T> remappingFunction) {
        return (T) this.entries.computeIfPresent(key, (BiFunction) remappingFunction);
    }

    @Override
    public <T extends Modifier> T compute(Class<T> key, BiFunction<Class<T>, T, T> remappingFunction) {
        return (T) this.entries.compute(key, (BiFunction) remappingFunction);
    }

    @Override
    public <T extends Modifier> T replace(Class<T> key, T value) {
        return (T) this.entries.replace(key, value);
    }

    @Override
    public <T extends Modifier> T remove(Class<T> key) {
        return (T) this.entries.remove(key);
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this);
        this.entries.forEach((key, value) -> helper.add(key.getSimpleName(), value));
        return helper.toString();
    }
}
