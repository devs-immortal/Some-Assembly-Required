package net.immortaldevs.sar.impl;

import net.immortaldevs.sar.api.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface SarItemStack extends ComponentNodeHost {
    default <T extends Modifier<T>> @Nullable T getModifier(Class<T> type) {
        throw new NoSuchMethodError();
    }

    @Override
    default void updateComponents() {
        throw new NoSuchMethodError();
    }

    @Override
    default void loadComponentNodes(ComponentNodeHandler handler) {
        throw new NoSuchMethodError();
    }

    @Override
    default boolean containsComponentNode(Identifier id) {
        throw new NoSuchMethodError();
    }

    @Override
    default boolean containsComponentNodes(Identifier id) {
        throw new NoSuchMethodError();
    }

    @Override
    default @Nullable ComponentNode getComponentNode(Identifier id) {
        throw new NoSuchMethodError();
    }

    @Override
    default ComponentNode createComponentNode(Identifier id, Component component) {
        throw new NoSuchMethodError();
    }

    @Override
    default void removeComponentNode(Identifier id) {
        throw new NoSuchMethodError();
    }

    @Override
    default ComponentNodeCollection getComponentNodes(Identifier id) {
        throw new NoSuchMethodError();
    }
}
