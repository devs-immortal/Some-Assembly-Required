package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@FunctionalInterface
public interface UseOnEntityModifier extends Modifier {
    ActionResult applyUseOnEntityModifier(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand);

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(UseOnEntityModifier.class, this, (a, b) -> (stack, user, entity, hand) -> {
            ActionResult result = a.applyUseOnEntityModifier(stack, user, entity, hand);
            if (result != ActionResult.PASS) return result;
            return b.applyUseOnEntityModifier(stack, user, entity, hand);
        });
    }
}
