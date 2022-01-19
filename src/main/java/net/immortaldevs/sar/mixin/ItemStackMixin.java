package net.immortaldevs.sar.mixin;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.ComponentData;
import net.immortaldevs.sar.api.SkeletalComponentData;
import net.immortaldevs.sar.base.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackExt {
    @Shadow
    public abstract @Nullable NbtCompound getNbt();

    @Shadow
    public abstract NbtCompound getOrCreateSubNbt(String key);

    @Unique
    private @Nullable ComponentData componentRoot;

    @Override
    public ComponentData sar$getComponentRoot() {
        if (this.componentRoot != null) {
            return this.componentRoot;
        }

        NbtCompound nbt = this.getNbt();
        if (nbt == null || !nbt.contains("sar_data", NbtElement.COMPOUND_TYPE)) {
            return null;
        }

        this.componentRoot = new NbtComponentData(null,
                () -> this.componentRoot = null,
                nbt.getCompound("sar_data"),
                true);

        return this.componentRoot;
    }

    @Override
    public boolean sar$hasComponentRoot() {
        if (this.componentRoot != null) return true;

        NbtCompound nbt = this.getNbt();
        return (nbt != null && nbt.contains("sar_data", NbtElement.COMPOUND_TYPE));
    }

    @Override
    public @Nullable SkeletalComponentData sar$getSkeletalComponentRoot() {
        if (this.componentRoot != null) {
            return this.componentRoot;
        }

        NbtCompound nbt = this.getNbt();
        if (nbt == null || !nbt.contains("sar_data", NbtElement.COMPOUND_TYPE)) {
            return null;
        }

        return new NbtSkeletalComponentData(null,
                () -> this.componentRoot = null,
                nbt.getCompound("sar_data"));
    }

    @Override
    public SkeletalComponentData sar$getOrCreateSkeletalComponentRoot(Component type) {
        if (this.componentRoot != null) {
            return this.componentRoot;
        }

        NbtCompound nbt = this.getOrCreateSubNbt("sar_data");
        if (!nbt.contains("id", NbtElement.STRING_TYPE)) {
            nbt.putString("id", SarRegistries.COMPONENT.getId(type).toString());
        }

        return new NbtSkeletalComponentData(null,
                () -> this.componentRoot = null,
                nbt);
    }
}
