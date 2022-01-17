package net.immortaldevs.sar.api;

import net.minecraft.nbt.NbtCompound;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface ComponentData {
    NbtCompound nbt();

    Component type();

    void addModifier(Modifier modifier);

    void addChild(String name, Consumer<Modifier> modifierHandler);

    void addChild(String name);

    void addChildren(String name, Consumer<Modifier> modifierHandler);

    void addChildren(String name);

    boolean onClient();
}
