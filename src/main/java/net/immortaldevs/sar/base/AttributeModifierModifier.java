package net.immortaldevs.sar.base;

import com.google.common.collect.Multimap;
import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@FunctionalInterface
public interface AttributeModifierModifier extends Modifier {
    void apply(
            ItemStack stack,
            EquipmentSlot slot,
            Multimap<EntityAttribute, EntityAttributeModifier> modifiers
    );

    static AttributeModifierModifier of(EntityAttribute attribute, EntityAttributeModifier modifier) {
        return (stack, slot, modifiers) -> {
            System.out.println(modifiers);
            modifiers.put(attribute, modifier);
        };
    }

    static AttributeModifierModifier of(EntityAttribute attribute0, EntityAttributeModifier modifier0,
                                        EntityAttribute attribute1, EntityAttributeModifier modifier1) {
        return (stack, slot, modifiers) -> {
            modifiers.put(attribute0, modifier0);
            modifiers.put(attribute1, modifier1);
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

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(AttributeModifierModifier.class, this, (a, b) -> (stack, slot, modifiers) -> {
            a.apply(stack, slot, modifiers);
            b.apply(stack, slot, modifiers);
        });
    }
}
