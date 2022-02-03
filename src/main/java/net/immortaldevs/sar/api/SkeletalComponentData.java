package net.immortaldevs.sar.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("unused")
public interface SkeletalComponentData {
    Optional<SkeletalComponentData> parent();

    Component type();

    NbtCompound getNbt();

    void setNbt(NbtCompound nbt);

    boolean hasNbt();

    void removeSubNbt(String key);

    default void setSubNbt(String key, NbtElement element) {
        if (this.hasNbt()) this.getNbt().put(key, element);
    }

    default @Nullable NbtCompound getSubNbt(String key) {
        if (this.hasNbt()) return this.getNbt().getCompound(key);
        return null;
    }

    default NbtCompound getOrCreateNbt() {
        if (!this.hasNbt()) this.setNbt(new NbtCompound());
        return this.getNbt();
    }

    default NbtCompound getOrCreateSubNbt(String key) {
        NbtCompound nbt = this.getOrCreateNbt();
        if (!nbt.contains(key, NbtElement.COMPOUND_TYPE)) {
            nbt.put(key, new NbtCompound());
        }

        return nbt.getCompound(key);
    }

    void changeType(Component type);

    boolean contains(String name);

    boolean containsChild(String name);

    boolean containsChildren(String name);

    Optional<SkeletalComponentData> getChild(String name);

    SkeletalComponentData createChild(String name, Component type);

    default SkeletalComponentData getOrCreateChild(String name, Component type) {
        return this.getChild(name).orElseGet(() ->
                this.createChild(name, type));
    }

    void removeChild(String name);

    Children getChildren(String name);

    interface Children {
        int size();

        boolean isEmpty();

        void add(Component type);

        void add(int i, Component type);

        void remove(int i);

        void clear();

        SkeletalComponentData get(int i);
    }
}
