package net.immortaldevs.sar.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.immortaldevs.sar.api.*;
import net.immortaldevs.sar.base.*;
import net.immortaldevs.sar.impl.ComponentRoot;
import net.immortaldevs.sar.impl.NbtComponentRoot;
import net.immortaldevs.sar.impl.ItemStackExt;
import net.immortaldevs.sar.impl.Util;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackExt {
    @Shadow
    public abstract NbtCompound getOrCreateSubNbt(String key);

    @Shadow
    private @Nullable NbtCompound nbt;

    @Shadow
    public abstract @Nullable NbtCompound getSubNbt(String key);

    @Unique
    private @Nullable ComponentRoot componentRoot;

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
    public FixedModifierMap getModifiers() {
        return this.getComponentRoot().modifiers();
    }

    @Override
    public Iterator<ComponentData> loadedComponentIterator() {
        return this.getComponentRoot().components();
    }

    @Override
    public boolean hasComponent(@NotNull String name) {
        NbtCompound components = this.getSubNbt("components");
        return components != null && components.contains(name, NbtElement.COMPOUND_TYPE);
    }

    @Override
    public @Nullable SkeletalComponentData getComponent(@NotNull String name) {
        if (this.nbt == null || !this.nbt.contains("components", NbtElement.COMPOUND_TYPE)) {
            return null;
        }

        NbtCompound components = this.nbt.getCompound("components");

        if (!components.contains(name, NbtElement.COMPOUND_TYPE)) {
            return null;
        }

        return new NbtSkeletalComponentData(components.getCompound(name),
                null,
                () -> this.componentRoot = null);
    }

    @Override
    public SkeletalComponentData getOrCreateComponent(@NotNull String name, @NotNull Component component) {
        NbtCompound components = this.getOrCreateSubNbt("components");

        if (!components.contains(name, NbtElement.COMPOUND_TYPE)) {
            NbtCompound data = new NbtCompound();
            data.putString("id", component.getId().toString());
            components.put(name, data);
        }

        return new NbtSkeletalComponentData(components.getCompound(name),
                null,
                () -> this.componentRoot = null);
    }

    @Override
    public void removeComponent(@NotNull String name) {
        NbtCompound components = this.getSubNbt("components");
        if (components != null) {
            components.remove(name);
        }
    }

    @Unique
    private ComponentRoot getComponentRoot() {
        if (this.componentRoot != null) return this.componentRoot;
        else if (this.nbt == null || !this.nbt.contains("components", NbtElement.COMPOUND_TYPE)) {
            return this.componentRoot = ComponentRoot.EMPTY;
        } else return this.componentRoot = new NbtComponentRoot(this.nbt.getCompound("components"),
                () -> this.componentRoot = null);
    }
}
