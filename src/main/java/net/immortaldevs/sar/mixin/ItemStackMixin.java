package net.immortaldevs.sar.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.immortaldevs.sar.api.*;
import net.immortaldevs.sar.base.*;
import net.immortaldevs.sar.impl.ComponentRoot;
import net.immortaldevs.sar.impl.NbtComponentRoot;
import net.immortaldevs.sar.impl.ItemStackExt;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
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

    @Shadow public abstract void removeSubNbt(String key);

    @Unique
    private @Nullable ComponentRoot componentRoot;

    @Inject(method = "getAttributeModifiers",
            at = @At("RETURN"),
            cancellable = true)
    private void getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        AttributeModifierModifier modifier = this.getModifiers().get(AttributeModifierModifier.class);
        if (modifier == null) return;
        Multimap<EntityAttribute, EntityAttributeModifier> out = HashMultimap.create(cir.getReturnValue());
        modifier.apply((ItemStack) (Object) this, slot, out);
        cir.setReturnValue(out);
    }

    @Inject(method = "useOnEntity",
            at = @At("HEAD"),
            cancellable = true)
    private void useOnEntity(PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        UseOnEntityModifier modifier = this.getModifiers().get(UseOnEntityModifier.class);
        if (modifier == null) return;
        ActionResult result = modifier.apply((ItemStack) (Object) this, user, entity, hand);
        if (result == ActionResult.PASS) return;
        cir.setReturnValue(result);
    }

    @Inject(method = "useOnBlock",
            at = @At("HEAD"),
            cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        UseOnBlockModifier modifier = this.getModifiers().get(UseOnBlockModifier.class);
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
    public Iterator<ComponentData> componentIterator() {
        return this.getComponentRoot().components();
    }

    @Override
    public boolean hasComponent(@NotNull String name) {
        NbtCompound components = this.getSubNbt("components");
        return components != null && components.contains(name, NbtElement.COMPOUND_TYPE);
    }

    @Override
    public boolean hasComponents(@NotNull String name) {
        NbtCompound components = this.getSubNbt("components");
        return components != null && components.contains(name, NbtElement.LIST_TYPE);
    }

    @Override
    public @Nullable SkeletalComponentData getComponent(@NotNull String name) {
        NbtCompound components = this.getSubNbt("components");
        if (components == null) return null;

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
            this.componentRoot = null;
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
            this.componentRoot = null;
            if (components.isEmpty()) this.removeSubNbt("components");
        }
    }

    @Override
    public ComponentCollection getComponents(@NotNull String name) {
        return new ComponentCollection() {
            @Override
            public int size() {
                return this.read().size();
            }

            @Override
            public boolean isEmpty() {
                return this.read().isEmpty();
            }

            @Override
            public void add(@NotNull Component component) {
                NbtCompound child = new NbtCompound();
                child.putString("id", component.getId().toString());

                NbtList children = this.read();
                children.add(child);
                this.write(children);
            }

            @Override
            public void add(int i, @NotNull Component component) {
                NbtCompound child = new NbtCompound();
                child.putString("id", component.getId().toString());

                NbtList children = this.read();
                children.add(i, child);
                this.write(children);
            }

            @Override
            public void remove(int i) {
                NbtList children = this.read();
                children.remove(i);
                this.write(children);
            }

            @Override
            public void clear() {
                ItemStackMixin.this.removeComponent(name);
            }

            @Override
            public SkeletalComponentData get(int i) {
                return null;
            }

            private NbtList read() {
                NbtCompound components = ItemStackMixin.this.getSubNbt("components");
                if (components == null) return new NbtList();
                return components.getList(name, NbtElement.COMPOUND_TYPE);
            }

            private void write(NbtList list) {
                if (list.isEmpty()) this.clear();
                else {
                    ItemStackMixin.this.getOrCreateSubNbt("components").put(name, list);
                    ItemStackMixin.this.componentRoot = null;
                }
            }
        };
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
