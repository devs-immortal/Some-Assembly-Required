package net.immortaldevs.sar.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface SkeletalComponentData {
    @Nullable SkeletalComponentData parent();

    Component type();

    NbtCompound getNbt();

    void setNbt(NbtCompound nbt);

    boolean hasNbt();

    void removeSubNbt(String key);

    default void setSubNbt(String key, NbtElement element) {
        if (this.hasNbt()) this.getNbt().put(key, element);
    }

    default NbtCompound getSubNbt(String key) {
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

    @Nullable SkeletalComponentData getChild(String name);

    default SkeletalComponentData requireChild(String name) {
        SkeletalComponentData child = this.getChild(name);
        if (child != null) return child;
        return this.createChild(name, Component.UNKNOWN);
    }

    SkeletalComponentData createChild(String name, Component type);

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
