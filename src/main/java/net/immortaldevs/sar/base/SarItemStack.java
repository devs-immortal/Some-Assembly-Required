package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.*;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

@SuppressWarnings("unused")
public interface SarItemStack extends ComponentHost {
    @Override
    default FixedModifierMap getModifiers() {
        throw new NotImplementedException();
    }

    @Override
    default Iterator<ComponentData> componentIterator() {
        throw new NotImplementedException();
    }

    @Override
    default boolean hasComponent(String name) {
        throw new NotImplementedException();
    }

    @Override
    default @Nullable SkeletalComponentData getComponent(String name) {
        throw new NotImplementedException();
    }

    @Override
    default SkeletalComponentData getOrCreateComponent(String name, Component component) {
        throw new NotImplementedException();
    }

    @Override
    default void removeComponent(String name) {
        throw new NotImplementedException();
    }
}
