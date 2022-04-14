package net.immortaldevs.sar.api;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public interface ComponentHost {
    FixedModifierMap getModifiers();

    Iterator<ComponentData> loadedComponentIterator();

    boolean hasComponent(String name);

    @Nullable SkeletalComponentData getComponent(String name);

    SkeletalComponentData getOrCreateComponent(String name, Component component);

    void removeComponent(String name);
}
