package net.immortaldevs.sar.base;

import net.immortaldevs.sar.api.Modifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@FunctionalInterface
public interface UseOnEntityModifier extends Modifier<UseOnEntityModifier> {
    ActionResult apply(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand);

    @Override
    default Class<UseOnEntityModifier> getType() {
        return UseOnEntityModifier.class;
    }

    @Override
    default UseOnEntityModifier merge(UseOnEntityModifier that) {
        return (stack, user, entity, hand) -> {
            ActionResult result = this.apply(stack, user, entity, hand);
            if (result != ActionResult.PASS) return result;
            return that.apply(stack, user, entity, hand);
        };
    }
}
