package net.immortaldevs.sar.test;

import net.immortaldevs.sar.api.ComponentNode;
import net.immortaldevs.sar.api.ModifierHandler;
import net.immortaldevs.sar.api.SarRegistries;
import net.immortaldevs.sar.base.*;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;

import static net.immortaldevs.sar.test.SarTest.id;

public final class SarTestComponents {
    public static final ModifierOrderTestComponent MODIFIER_ORDER_TEST = new ModifierOrderTestComponent();
    public static final SimpleComponent MODIFIER_TEST = new SimpleComponent(
            AttributeModifierModifier.of(EquipmentSlot.MAINHAND,
                    EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new EntityAttributeModifier("test",
                            0.25,
                            EntityAttributeModifier.Operation.ADDITION)),
            EnchantmentLevelModifier.of(Enchantments.FIRE_ASPECT, 2),
            (FoodEffectModifier) (stack, world, targetEntity) -> targetEntity.setOnFireFor(4),
            FoodStatusEffectModifier.of(StatusEffects.NAUSEA, 8, 0, 1f),
            HungerModifier.add(-4),
            SaturationModifierModifier.add(10f),
            (UseOnBlockModifier) context -> {
                context.getWorld().setBlockState(context.getBlockPos(), Blocks.DIAMOND_BLOCK.getDefaultState());
                return ActionResult.SUCCESS;
            },
            (UseOnEntityModifier) (stack, user, entity, hand) -> {
                entity.setOnFireFor(2);
                return ActionResult.SUCCESS;
            });

    public static void init() {
        Registry.register(SarRegistries.COMPONENT, id("modifier_order_test"), MODIFIER_ORDER_TEST);
        Registry.register(SarRegistries.COMPONENT, id("modifier_test"), MODIFIER_TEST);
    }
}
