package net.immortaldevs.sar.api;

import net.minecraft.nbt.NbtCompound;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface ComponentData extends SkeletalComponentData {
    NbtCompound getNbt();

    Component type();

    ModifierMap modifierMap();

    void addModifier(Modifier modifier);

    void loadChild(String name, Consumer<Modifier> modifierHandler);

    void loadChildren(String name, Consumer<Modifier> modifierHandler);

    default void loadChild(String name) {
        this.loadChild(name, this::addModifier);
    }

    default void loadChildren(String name) {
        this.loadChildren(name, this::addModifier);
    }

    boolean onClient();
}
