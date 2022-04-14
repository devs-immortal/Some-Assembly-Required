package net.immortaldevs.sar.impl;

import com.google.common.collect.Iterators;
import net.immortaldevs.sar.api.*;
import net.immortaldevs.sar.base.HashModifierMap;
import net.immortaldevs.sar.base.NbtLarvalComponentData;
import net.minecraft.nbt.NbtCompound;

import java.util.Iterator;

public final class NbtComponentRoot implements ComponentRoot {
    private final FixedModifierMap modifiers;
    private final ComponentData[] components;

    public NbtComponentRoot(NbtCompound nbt, Runnable changedCallback) {
        ModifierMap modifiers = new HashModifierMap();
        this.modifiers = modifiers;
        this.components = nbt.getKeys().stream()
                .map(key -> new NbtLarvalComponentData(nbt.getCompound(key),
                        null,
                        changedCallback,
                        data -> data.getComponent().init(data),
                        data -> {},
                        modifier -> modifier.register(modifiers)))
                .toArray(ComponentData[]::new);
    }

    @Override
    public FixedModifierMap modifiers() {
        return this.modifiers;
    }

    @Override
    public Iterator<ComponentData> components() {
        return Iterators.forArray(this.components);
    }
}
