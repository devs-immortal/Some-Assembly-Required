package net.immortaldevs.sar.test;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;
import net.immortaldevs.sar.base.FoodEffectModifier;
import net.immortaldevs.sar.base.HungerModifier;
import net.minecraft.entity.effect.StatusEffects;

public class CumComponent extends Component {
    @Override
    public void init(LarvalComponentData data) {
        data.addModifier((HungerModifier) (stack, values) -> {
            values.food = (5 * values.food) / 4;
            values.saturationModifier *= 0.4f;
        });
        data.addModifier(FoodEffectModifier.of(StatusEffects.STRENGTH, 940, 2));
    }
}
