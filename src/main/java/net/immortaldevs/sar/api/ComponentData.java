package net.immortaldevs.sar.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface ComponentData {
    NbtCompound nbt();

    Component type();

    ModifierMap modifierMap();

    void addModifier(Modifier modifier);

    void addChild(NbtCompound data, Consumer<Modifier> modifierHandler);

    default void addChild(NbtCompound data) {
        this.addChild(data, this::addModifier);
    }

    default void addChild(String name, Consumer<Modifier> modifierHandler) {
        if (this.nbt().contains(name, NbtElement.COMPOUND_TYPE)) {
            this.addChild(this.nbt().getCompound(name), modifierHandler);
        }
    }

    default void addChild(String name) {
        if (this.nbt().contains(name, NbtElement.COMPOUND_TYPE)) {
            this.addChild(this.nbt().getCompound(name));
        }
    }

    default void addChildren(String name, Consumer<Modifier> modifierHandler) {
        NbtList nbtChildren = this.nbt().getList(name, NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < nbtChildren.size(); i++) {
            this.addChild(nbtChildren.getCompound(i), modifierHandler);
        }
    }

    default void addChildren(String name) {
        NbtList nbtChildren = this.nbt().getList(name, NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < nbtChildren.size(); i++) {
            this.addChild(nbtChildren.getCompound(i));
        }
    }

    boolean onClient();
}
