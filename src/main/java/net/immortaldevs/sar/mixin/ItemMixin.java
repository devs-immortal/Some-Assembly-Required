package net.immortaldevs.sar.mixin;

import net.immortaldevs.divineintervention.injection.ModifyOperand;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(Item.class)
public abstract class ItemMixin {
    @Unique
    private static final FoodComponent EMPTY_FOOD_COMPONENT = new FoodComponent.Builder()
            .saturationModifier(1f)
            .build();

    @ModifyOperand(method = "use",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;isFood()Z"))
    private boolean use(boolean result, World world, PlayerEntity user, Hand hand) {
        return user.getStackInHand(hand).isFood();
    }

    @ModifyOperand(method = "finishUsing",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;isFood()Z"))
    private boolean finishUsing(boolean result, ItemStack stack, World world, LivingEntity user) {
        return stack.isFood();
    }

    @ModifyOperand(method = "getUseAction",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;isFood()Z"))
    private boolean getUseAction(boolean result, ItemStack stack) {
        return stack.isFood();
    }

    @ModifyOperand(method = "getMaxUseTime",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;isFood()Z"))
    private boolean getMaxUseTime(boolean result, ItemStack stack) {
        return stack.isFood();
    }

    @ModifyOperand(method = "getFoodComponent",
            at = @At("RETURN"))
    private FoodComponent getFoodComponent(FoodComponent foodComponent) {
        return foodComponent == null ? EMPTY_FOOD_COMPONENT : foodComponent;
    }
}
