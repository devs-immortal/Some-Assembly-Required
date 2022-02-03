package net.immortaldevs.sar.mixin;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.RootComponentData;
import net.immortaldevs.sar.api.SkeletalComponentData;
import net.immortaldevs.sar.base.ItemStackExt;
import net.immortaldevs.sar.base.NbtRootComponentData;
import net.immortaldevs.sar.base.NbtSkeletalComponentData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@SuppressWarnings({"OptionalAssignedToNull", "OptionalUsedAsFieldOrParameterType"})
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackExt {
    @Shadow
    public abstract NbtCompound getOrCreateSubNbt(String key);

    @Shadow
    private @Nullable NbtCompound nbt;

    @Unique
    private @Nullable Optional<RootComponentData> componentRoot;

    @Override
    public Optional<RootComponentData> sar$getComponentRoot() {
        if (this.componentRoot != null) {
            return this.componentRoot;
        }

        if (this.nbt == null || !this.nbt.contains("sar_data", NbtElement.COMPOUND_TYPE)) {
            return this.componentRoot = Optional.empty();
        }

        return this.componentRoot = Optional.of(new NbtRootComponentData(
                () -> this.componentRoot = null,
                this.nbt.getCompound("sar_data")));
    }

    @Override
    public boolean sar$hasComponentRoot() {
        return this.nbt != null && this.nbt.contains("sar_data", NbtElement.COMPOUND_TYPE);
    }

    @Override
    public Optional<SkeletalComponentData> sar$getSkeletalComponentRoot() {
        if (this.componentRoot != null) {
            // ;-;
            return Optional.ofNullable(this.componentRoot.orElse(null));
        }

        if (this.nbt == null || !this.nbt.contains("sar_data", NbtElement.COMPOUND_TYPE)) {
            return Optional.empty();
        }

        return Optional.of(new NbtSkeletalComponentData(null,
                () -> this.componentRoot = null,
                this.nbt.getCompound("sar_data"),
                Component.ROOT));
    }

    @Override
    public SkeletalComponentData sar$getOrCreateSkeletalComponentRoot() {
        if (this.componentRoot != null && this.componentRoot.isPresent()) {
            return this.componentRoot.get();
        }

        NbtCompound nbt = this.getOrCreateSubNbt("sar_data");
        nbt.putString("id", "sar:root");

        return new NbtSkeletalComponentData(null,
                () -> this.componentRoot = null,
                nbt,
                Component.ROOT);
    }
}
