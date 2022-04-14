package net.immortaldevs.sar.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface SkeletalComponentData {
    @Nullable SkeletalComponentData getParent();

    Component getComponent();

    void changeComponent(Component component);

    @Nullable NbtCompound getNbt();

    void setNbt(NbtCompound nbt);

    boolean hasNbt();

    void removeSubNbt(String key);

    default void setSubNbt(String key, NbtElement element) {
        NbtCompound nbt = this.getNbt();
        if (nbt != null) nbt.put(key, element);
    }

    default @Nullable NbtCompound getSubNbt(String key) {
        NbtCompound nbt = this.getNbt();
        return nbt != null && nbt.contains(key, NbtElement.COMPOUND_TYPE)
                ? nbt.getCompound(key)
                : null;
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

    boolean contains(String name);

    boolean containsChild(String name);

    boolean containsChildren(String name);

    SkeletalComponentData getChild(String name);

    SkeletalComponentData createChild(String name, Component type);

    default SkeletalComponentData getOrCreateChild(String name, Component type) {
        SkeletalComponentData child = this.getChild(name);
        if (child != null) return child;
        return this.createChild(name, type);
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
