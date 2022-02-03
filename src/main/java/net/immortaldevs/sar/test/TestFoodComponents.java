package net.immortaldevs.sar.test;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public final class TestFoodComponents {
    public static final FoodComponent CHEESE = new FoodComponent.Builder()
            .hunger(6)
            .saturationModifier(1.2f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1440, 24), 1f)
            .build();
}
