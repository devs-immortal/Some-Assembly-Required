package net.immortaldevs.sar.mixin;

import net.immortaldevs.sar.base.HungerModifier;
import net.immortaldevs.sar.base.Util;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @Shadow
    public abstract void add(int food, float saturationModifier);

    @Inject(method = "eat",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void eat(Item item, ItemStack stack, CallbackInfo ci, FoodComponent foodComponent) {
        HungerModifier modifier = Util.getModifier(stack, HungerModifier.class);
        if (modifier == null) return;

        HungerModifier.Values values = new HungerModifier.Values(foodComponent.getHunger(),
                foodComponent.getSaturationModifier());

        modifier.apply(stack, values);
        this.add(values.food, values.saturationModifier);
        ci.cancel();
    }
}
