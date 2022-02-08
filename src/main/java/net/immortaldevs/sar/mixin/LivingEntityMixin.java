package net.immortaldevs.sar.mixin;

import com.mojang.datafixers.util.Pair;
import net.immortaldevs.sar.api.RootComponentData;
import net.immortaldevs.sar.base.FoodEffectModifier;
import net.immortaldevs.sar.base.FoodStatusEffectModifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyVariable(method = "applyFoodEffects",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/FoodComponent;getStatusEffects()Ljava/util/List;",
                    shift = At.Shift.BY,
                    by = 2),
            index = 5)
    private List<Pair<StatusEffectInstance, Float>> applyFoodEffects(List<Pair<StatusEffectInstance, Float>> list, ItemStack stack, World world, LivingEntity targetEntity) {
        Optional<RootComponentData> data = stack.sar$getComponentRoot();
        if (data.isEmpty()) return list;

        FoodEffectModifier foodEffectModifier = data.get()
                .modifiers()
                .get(FoodEffectModifier.class);

        if (foodEffectModifier != null) {
            foodEffectModifier.applyFoodEffectModifier(stack, world, targetEntity);
        }

        FoodStatusEffectModifier foodStatusEffectModifier = data.get()
                .modifiers()
                .get(FoodStatusEffectModifier.class);

        if (foodStatusEffectModifier == null) return list;

        List<Pair<StatusEffectInstance, Float>> effects = new ArrayList<>(list);
        foodStatusEffectModifier.applyFoodStatusEffectModifier(stack, world, targetEntity, effects);
        return effects;
    }
}
