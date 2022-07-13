package net.immortaldevs.sar.base;

import com.google.common.collect.Multimap;
import net.immortaldevs.sar.api.Modifier;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@FunctionalInterface
public interface AttributeModifierModifier extends Modifier<AttributeModifierModifier> {
    void apply(ItemStack stack,
               EquipmentSlot slot,
               Multimap<EntityAttribute, EntityAttributeModifier> modifiers);

    @Override
    default Class<AttributeModifierModifier> getType() {
        return AttributeModifierModifier.class;
    }

    @Override
    default AttributeModifierModifier merge(AttributeModifierModifier that) {
        return (stack, slot, modifiers) -> {
            this.apply(stack, slot, modifiers);
            that.apply(stack, slot, modifiers);
        };
    }

    static AttributeModifierModifier of(EntityAttribute attribute, EntityAttributeModifier modifier) {
        return (stack, slot, modifiers) -> modifiers.put(attribute, modifier);
    }

    static AttributeModifierModifier of(EquipmentSlot equipmentSlot,
                                        EntityAttribute attribute, EntityAttributeModifier modifier) {
        return (stack, slot, modifiers) -> {
            if (slot == equipmentSlot) modifiers.put(attribute, modifier);
        };
    }

    static AttributeModifierModifier of(EntityAttribute attribute0, EntityAttributeModifier modifier0,
                                        EntityAttribute attribute1, EntityAttributeModifier modifier1) {
        return (stack, slot, modifiers) -> {
            modifiers.put(attribute0, modifier0);
            modifiers.put(attribute1, modifier1);
        };
    }

    static AttributeModifierModifier of(EquipmentSlot equipmentSlot,
                                        EntityAttribute attribute0, EntityAttributeModifier modifier0,
                                        EntityAttribute attribute1, EntityAttributeModifier modifier1) {
        return (stack, slot, modifiers) -> {
            if (slot == equipmentSlot) {
                modifiers.put(attribute0, modifier0);
                modifiers.put(attribute1, modifier1);
            }
        };
    }

    static AttributeModifierModifier of(EntityAttribute attribute0, EntityAttributeModifier modifier0,
                                        EntityAttribute attribute1, EntityAttributeModifier modifier1,
                                        EntityAttribute attribute2, EntityAttributeModifier modifier2) {
        return (stack, slot, modifiers) -> {
            modifiers.put(attribute0, modifier0);
            modifiers.put(attribute1, modifier1);
            modifiers.put(attribute2, modifier2);
        };
    }

    static AttributeModifierModifier of(EquipmentSlot equipmentSlot,
                                        EntityAttribute attribute0, EntityAttributeModifier modifier0,
                                        EntityAttribute attribute1, EntityAttributeModifier modifier1,
                                        EntityAttribute attribute2, EntityAttributeModifier modifier2) {
        return (stack, slot, modifiers) -> {
            if (slot == equipmentSlot) {
                modifiers.put(attribute0, modifier0);
                modifiers.put(attribute1, modifier1);
                modifiers.put(attribute2, modifier2);
            }
        };
    }
}
