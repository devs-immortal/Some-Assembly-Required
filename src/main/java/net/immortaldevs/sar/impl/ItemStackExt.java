package net.immortaldevs.sar.impl;

import net.immortaldevs.sar.api.*;
import net.immortaldevs.sar.base.SarItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public interface ItemStackExt extends SarItemStack {
    @Override
    FixedModifierMap getModifiers();

    @Override
    Iterator<ComponentData> componentIterator();

    @Override
    boolean hasComponent(String name);

    @Override
    @Nullable SkeletalComponentData getComponent(String name);

    @Override
    SkeletalComponentData getOrCreateComponent(String name, Component component);

    @Override
    void removeComponent(String name);
}
