package net.immortaldevs.sar.mixin;

import net.immortaldevs.sar.base.RootComponentData;
import net.immortaldevs.sar.base.VersionedRootComponentData;
import net.immortaldevs.sar.base.ItemStackExt;
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

    @Unique
    private @Nullable VersionedRootComponentData componentRoot;

    @Override
    public @Nullable RootComponentData sar$getComponentRoot() {
        NbtCompound nbt = this.getNbt();
        if (nbt == null || !nbt.contains("sar_cache", NbtElement.INT_TYPE)) {
            return null;
        }

        int version = nbt.getInt("sar_cache");
        if (this.componentRoot != null && this.componentRoot.version == version) {
            return this.componentRoot;
        }

        NbtCompound sarComponent = nbt.getCompound("sar_data");
        this.componentRoot = new VersionedRootComponentData(sarComponent, true, version);

        return this.componentRoot;
    }
}
