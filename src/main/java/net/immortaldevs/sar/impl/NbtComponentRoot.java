package net.immortaldevs.sar.impl;

import com.google.common.collect.Iterators;
import net.immortaldevs.sar.api.*;
import net.immortaldevs.sar.base.HashModifierMap;
import net.immortaldevs.sar.base.NbtLarvalComponentData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class NbtComponentRoot implements ComponentRoot {
    private final FixedModifierMap modifiers;
    private final ComponentData[] components;

    public NbtComponentRoot(NbtCompound nbt, Runnable changedCallback) {
        ModifierMap modifiers = new HashModifierMap();
        this.modifiers = modifiers;

        List<NbtCompound> components = new ArrayList<>();

        for (String key : nbt.getKeys()) {
            NbtElement element = nbt.get(key);
            if (element instanceof NbtCompound compound) components.add(compound);
            else if (element instanceof NbtList list) for (NbtElement e : list) {
                if (e instanceof NbtCompound compound) components.add(compound);
            }
        }

        this.components = new ComponentData[components.size()];
        for (int i = 0; i < this.components.length; i++) {
            this.components[i] = new NbtLarvalComponentData(components.get(i),
                    null,
                    changedCallback,
                    data -> data.getComponent().init(data),
                    data -> {},
                    modifier -> modifier.register(modifiers));
        }
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
