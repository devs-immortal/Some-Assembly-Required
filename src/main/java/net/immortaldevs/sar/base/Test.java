package net.immortaldevs.sar.base;

import com.google.common.collect.Multimap;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class Test implements FoodEffectModifier, FoodModifier, SaturationModifierModifier, AttributeModifierModifier, UseOnBlockModifier, UseOnEntityModifier {
    @Override
    public void applyAttributeModifierModifier(ItemStack stack, EquipmentSlot slot, Multimap<EntityAttribute, EntityAttributeModifier> modifiers) {

    }

    @Override
    public void applyFoodEffectModifier(ItemStack stack, World world, LivingEntity targetEntity) {

    }

    @Override
    public void register(ModifierMap modifierMap) {
        FoodEffectModifier.super.register(modifierMap);
        FoodModifier.super.register(modifierMap);
        SaturationModifierModifier.super.register(modifierMap);
        AttributeModifierModifier.super.register(modifierMap);
        UseOnBlockModifier.super.register(modifierMap);
        UseOnEntityModifier.super.register(modifierMap);
    }

    @Override
    public int applyFoodModifier(ItemStack stack, int food) {
        return 0;
    }

    @Override
    public float applySaturationModifierModifier(ItemStack stack, float saturationModifier) {
        return 0;
    }

    @Override
    public ActionResult applyUseOnBlockModifier(ItemUsageContext context) {
        return null;
    }

    @Override
    public ActionResult applyUseOnEntityModifier(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return null;
    }
}
