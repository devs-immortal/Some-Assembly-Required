package net.immortaldevs.sar.mixin;

import net.immortaldevs.sar.base.FoodEffectModifier;
import net.immortaldevs.sar.base.client.Util;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "applyFoodEffects",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/FoodComponent;getStatusEffects()Ljava/util/List;"))
    private void applyFoodEffects(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
        FoodEffectModifier modifier = Util.getModifier(stack, FoodEffectModifier.class);
        if (modifier == null) return;
        modifier.apply(stack, world, targetEntity);
    }
}
