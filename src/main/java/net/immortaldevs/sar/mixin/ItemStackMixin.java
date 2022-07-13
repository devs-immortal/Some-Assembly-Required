package net.immortaldevs.sar.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.immortaldevs.sar.api.*;
import net.immortaldevs.sar.base.*;
import net.immortaldevs.sar.impl.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements SarItemStack, ComponentNodeHandler {
    @Shadow
    public abstract NbtCompound getOrCreateSubNbt(String key);

    @Shadow
    public abstract @Nullable NbtCompound getSubNbt(String key);

    @Shadow
    public abstract void removeSubNbt(String key);

    @Shadow
    public abstract Item getItem();

    @Unique
    private static final ModifierMap EMPTY_MODIFIER_MAP = new ModifierMap();

    @Unique
    private @Nullable ModifierMap modifierMap = null;

    @Inject(method = "getAttributeModifiers",
            at = @At("RETURN"),
            cancellable = true)
    private void getAttributeModifiers(EquipmentSlot slot,
                                       CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        AttributeModifierModifier modifier = this.getModifier(AttributeModifierModifier.class);
        if (modifier == null) return;
        Multimap<EntityAttribute, EntityAttributeModifier> out = HashMultimap.create(cir.getReturnValue());
        modifier.apply((ItemStack) (Object) this, slot, out);
        cir.setReturnValue(out);
    }

    @Inject(method = "useOnEntity",
            at = @At("HEAD"),
            cancellable = true)
    private void useOnEntity(PlayerEntity user,
                             LivingEntity entity,
                             Hand hand,
                             CallbackInfoReturnable<ActionResult> cir) {
        UseOnEntityModifier modifier = this.getModifier(UseOnEntityModifier.class);
        if (modifier == null) return;
        ActionResult result = modifier.apply((ItemStack) (Object) this, user, entity, hand);
        if (result == ActionResult.PASS) return;
        cir.setReturnValue(result);
    }

    @Inject(method = "useOnBlock",
            at = @At("HEAD"),
            cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        UseOnBlockModifier modifier = this.getModifier(UseOnBlockModifier.class);
        if (modifier == null) return;
        ActionResult result = modifier.apply(context);
        if (result == ActionResult.PASS) return;
        cir.setReturnValue(result);
    }

    // TODO: find out if read-only item stacks are ever shared between threads. this may need double locking.
    @Override
    public <T extends Modifier<T>> @Nullable T getModifier(@NotNull Class<T> type) {
        if (this.modifierMap == null) {
            this.loadComponentNodes(this);
            if (this.modifierMap == null) this.modifierMap = EMPTY_MODIFIER_MAP;
            else this.modifierMap.finish();
        }

        return this.modifierMap.get(type);
    }

    @Override
    public void updateComponents() {
        NbtCompound components = this.getSubNbt("components");
        if (components != null) {
            Util.updateComponents(((NbtCompoundAccessor) components).getEntries().values(), true);
            if (components.isEmpty()) this.removeSubNbt("components");
        }

        this.modifierMap = null;
    }

    @Override
    public void loadComponentNodes(@NotNull ComponentNodeHandler handler) {
        this.getItem().loadComponentNodes((ItemStack) (Object) this, handler);

        NbtCompound components = this.getSubNbt("components");
        if (components == null) return;
        for (NbtElement element : ((NbtCompoundAccessor) components).getEntries().values()) {
            if (element instanceof NbtCompound nbt) {
                handler.load(new NbtComponentNode(nbt, this));
            } else if (element instanceof NbtList list
                    && list.getHeldType() == NbtElement.COMPOUND_TYPE) {
                for (int i = 0, size = list.size(); i < size; i++) {
                    handler.load(new NbtComponentNode(list.getCompound(i), this));
                }
            }
        }
    }

    @Override
    public boolean containsComponentNode(@NotNull Identifier id) {
        NbtCompound components = this.getSubNbt("components");
        return components != null && components.contains(id.toString(), NbtElement.COMPOUND_TYPE);
    }

    @Override
    public boolean containsComponentNodes(@NotNull Identifier id) {
        NbtCompound components = this.getSubNbt("components");
        return components != null && components.contains(id.toString(), NbtElement.LIST_TYPE);
    }

    @Override
    public @Nullable ComponentNode getComponentNode(@NotNull Identifier id) {
        NbtCompound components = this.getSubNbt("components");
        if (components == null) return null;

        String key = id.toString();
        if (!components.contains(key, NbtElement.COMPOUND_TYPE)) {
            return null;
        }

        return new NbtComponentNode(components.getCompound(key),
                this);
    }

    @Override
    public @NotNull ComponentNode getOrCreateComponentNode(@NotNull Identifier id, @NotNull Component component) {
        NbtCompound components = this.getOrCreateSubNbt("components");
        String key = id.toString();
        if (!components.contains(key, NbtElement.COMPOUND_TYPE)) {
            NbtCompound data = new NbtCompound();
            data.putString("id", component.getId().toString());
            components.put(key, data);
            this.updateComponents();
        }

        return new NbtComponentNode(components.getCompound(key),
                this);
    }

    @Override
    public void removeComponentNode(@NotNull Identifier id) {
        NbtCompound components = this.getSubNbt("components");
        if (components != null) {
            components.remove(id.toString());
            if (components.isEmpty()) this.removeSubNbt("components");
            this.updateComponents();
        }
    }

    @Override
    public @NotNull ComponentNodeCollection getComponentNodes(@NotNull Identifier id) {
        String key = id.toString();
        return new NbtComponentNodeCollection() {
            @Override
            protected @NotNull NbtList read() {
                NbtCompound components = ItemStackMixin.this.getSubNbt("components");
                if (components == null) return new NbtList();
                return components.getList(key, NbtElement.COMPOUND_TYPE);
            }

            @Override
            protected void write(@NotNull NbtList list) {
                if (list.isEmpty()) this.clear();
                else {
                    ItemStackMixin.this.getOrCreateSubNbt("components").put(key, list);
                    ItemStackMixin.this.updateComponents();
                }
            }

            @Override
            public void clear() {
                ItemStackMixin.this.removeComponentNode(id);

            }

            @Override
            public @NotNull ComponentNode get(int i) {
                return new NbtComponentNode(this.read().getCompound(i), ItemStackMixin.this);
            }
        };
    }

    @Override
    public void load(@NotNull ComponentNode node) {
        this.getModifierMap().load(node);
    }

    @Override
    public void load(@NotNull ComponentNode node, @NotNull ModifierTransformer transformer) {
        this.getModifierMap().load(node, transformer);
    }

    @Unique
    private ModifierMap getModifierMap() {
        return this.modifierMap == null ? this.modifierMap = new ModifierMap() : this.modifierMap;
    }
}
