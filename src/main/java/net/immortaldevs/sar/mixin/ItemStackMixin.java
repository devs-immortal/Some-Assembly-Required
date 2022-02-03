package net.immortaldevs.sar.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.RootComponentData;
import net.immortaldevs.sar.api.SkeletalComponentData;
import net.immortaldevs.sar.base.*;
import net.immortaldevs.sar.base.client.Util;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(method = "getAttributeModifiers",
            at = @At("RETURN"),
            cancellable = true)
    private void getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        AttributeModifierModifier modifier = Util.getModifier(this, AttributeModifierModifier.class);
        if (modifier == null) return;
        Multimap<EntityAttribute, EntityAttributeModifier> out = HashMultimap.create(cir.getReturnValue());
        modifier.apply((ItemStack) (Object) this, slot, out);
        cir.setReturnValue(out);
    }

    @Inject(method = "useOnEntity",
            at = @At("HEAD"),
            cancellable = true)
    private void useOnEntity(PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        UseOnEntityModifier modifier = Util.getModifier(this, UseOnEntityModifier.class);
        if (modifier == null) return;
        ActionResult result = modifier.apply((ItemStack) (Object) this, user, entity, hand);
        if (result == ActionResult.PASS) return;
        cir.setReturnValue(result);
    }

    @Inject(method = "useOnBlock",
            at = @At("HEAD"),
            cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        UseOnBlockModifier modifier = Util.getModifier(this, UseOnBlockModifier.class);
        if (modifier == null) return;
        ActionResult result = modifier.apply(context);
        if (result == ActionResult.PASS) return;
        cir.setReturnValue(result);
    }

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

        return new NbtSkeletalComponentData(null,
                () -> this.componentRoot = null,
                nbt,
                Component.ROOT);
    }
}
