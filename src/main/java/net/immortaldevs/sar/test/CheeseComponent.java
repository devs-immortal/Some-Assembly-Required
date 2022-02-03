package net.immortaldevs.sar.test;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;
import net.immortaldevs.sar.base.FoodEffectModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class CheeseComponent extends Component {
    @Override
    public void init(LarvalComponentData data) {
        data.addModifier((FoodEffectModifier) (stack, world, targetEntity) -> {
            if (!world.isClient) {
                targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST,
                        2880,
                        24));
            }
        });
    }
}
