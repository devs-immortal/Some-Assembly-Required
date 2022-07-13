package net.immortaldevs.sar.impl;

import it.unimi.dsi.fastutil.HashCommon;
import net.immortaldevs.sar.api.*;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class ModifierMap implements ModifierHandler, ModifierHolder, ComponentNodeHandler {
    private @Nullable Modifier[] values = new Modifier[0x20];
    private int capacity = 0x18;
    private int mask = 0x1f;
    private @Nullable ModifierMap child = null;

    public ModifierMap getChild() {
        return this.child == null ? (this.child = new ModifierMap()) : this.child;
    }

    public void mergeChild() {
        if (this.child == null) throw new IllegalStateException();
        Modifier[] values = this.child.values;

        for (int i = 0; i < values.length; i++) {
            Modifier value = values[i];
            if (value == null) continue;
            this.merge(value);
            values[i] = null;
        }

        this.child.capacity = values.length * 3 >> 2;
    }

    public void finish() {
        this.child = null;
    }

    @Override
    public <T extends Modifier<T>> T add(T modifier) {
        int index = this.locate(modifier.getType());
        T value = (T) this.values[index];
        if (value == null) {
            this.values[index] = modifier;
            if (--this.capacity == 0) this.expand();
        } else this.values[index] = modifier.adopt(value);
        return modifier;
    }

    @Override
    public <T extends Modifier<T>, U extends Modifier<U>> void map(Class<T> type, Function<T, @Nullable U> function) {
        int index = this.locate(type);
        T oldValue = (T) this.values[index];
        if (oldValue == null) return;
        U newValue = function.apply(oldValue);
        if (newValue == null) {
            this.values[index] = null;
            this.capacity++;
        } else if (newValue.getType() == type) {
            this.values[index] = newValue;
        } else {
            this.values[index] = null;
            this.capacity++;
            this.add(newValue);
        }
    }

    @Override
    public <T extends Modifier<T>, U extends Modifier<U>> void compute(Class<T> type,
                                                                       Function<@Nullable T, @Nullable U> function) {
        int index = this.locate(type);
        U newValue = function.apply((T) this.values[index]);
        if (newValue == null) {
            this.values[index] = null;
            this.capacity++;
        } else if (newValue.getType() == type) {
            this.values[index] = newValue;
        } else {
            this.values[index] = null;
            this.capacity++;
            this.add(newValue);
        }
    }

    @Override
    public <T extends Modifier<T>> T fill(T value) {
        int index = this.locate(value.getType());
        if (this.values[index] != null) return (T) this.values[index];
        this.values[index] = value;
        if (--this.capacity == 0) this.expand();
        return value;
    }

    @Override
    public <T extends Modifier<T>> @Nullable T get(Class<T> type) {
        return (T) this.values[this.locate(type)];
    }

    @Override
    public <T extends Modifier<T>> @Nullable T remove(Class<T> type) {
        int index = this.locate(type);
        T value = (T) this.values[index];
        if (value != null) {
            this.values[index] = null;
            this.capacity++;
        }

        return value;
    }

    private void merge(Modifier modifier) {
        int index = this.locate(modifier.getType());
        Modifier value = this.values[index];
        if (value == null) {
            this.values[index] = modifier;
            if (--this.capacity == 0) this.expand();
        } else this.values[index] = value.merge(modifier);
    }

    private void expand() {
        Modifier[] oldValues = this.values;
        this.values = new Modifier[oldValues.length << 1];
        this.capacity = oldValues.length * 3 >> 1;
        this.mask = this.values.length - 1;

        for (Modifier value : oldValues) {
            if (value == null) continue;
            this.values[this.locate(value.getType())] = value;
        }
    }

    private int locate(Class<? extends Modifier> type) {
        int index = HashCommon.mix(System.identityHashCode(type)) & this.mask;

        while (true) {
            Modifier value = this.values[index];
            if (value == null || value.getType() == type) return index;
            index = index + 1 & this.mask;
        }
    }

    @Override
    public void load(ComponentNode node) {
        ModifierMap child = this.getChild();
        node.loadComponentNodes(child);
        node.getComponent().addModifiers(node, child);
        this.mergeChild();
    }

    @Override
    public void load(ComponentNode node, ModifierTransformer transformer) {
        ModifierMap child = this.getChild();
        node.getComponent().loadComponentNodes(node, child);
        node.getComponent().addModifiers(node, child);
        transformer.accept(child);
        this.mergeChild();
    }
}
