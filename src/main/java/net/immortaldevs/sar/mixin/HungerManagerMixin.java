package net.immortaldevs.sar.mixin;

import net.immortaldevs.sar.api.RootComponentData;
import net.immortaldevs.sar.base.HungerModifier;
import net.immortaldevs.sar.base.SaturationModifier;
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

import java.util.Optional;

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
        Optional<RootComponentData> data = stack.sar$getComponentRoot();
        if (data.isEmpty()) return;

        int hunger = foodComponent.getHunger();
        float saturation = foodComponent.getSaturationModifier();
        boolean exit = false;

        HungerModifier hungerModifier = data.get().modifiers().get(HungerModifier.class);
        if (hungerModifier != null) hunger = hungerModifier.apply(stack, hunger);
        else exit = true;

        SaturationModifier saturationModifier = data.get().modifiers().get(SaturationModifier.class);
        if (saturationModifier != null) saturation = saturationModifier.apply(stack, saturation);
        else if (exit) return;

        this.add(hunger, saturation);
        ci.cancel();
    }
}
