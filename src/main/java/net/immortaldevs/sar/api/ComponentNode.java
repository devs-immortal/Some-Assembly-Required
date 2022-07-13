package net.immortaldevs.sar.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public abstract class ComponentNode implements ComponentNodeHost {
    public abstract Component getComponent();

    public abstract void setComponent(Component component);

    @Contract
    public abstract @Nullable NbtCompound getNbt();

    public abstract void setNbt(NbtCompound nbt);

    public abstract boolean hasNbt();

    public abstract void removeSubNbt(String key);

    public abstract void setSubNbt(String key, NbtElement element);

    public abstract @Nullable NbtCompound getSubNbt(String key);

    public abstract NbtCompound getOrCreateNbt();

    public abstract NbtCompound getOrCreateSubNbt(String key);
}
