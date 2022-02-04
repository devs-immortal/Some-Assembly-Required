package net.immortaldevs.sar.test;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;
import net.immortaldevs.sar.base.FoodEffectModifier;
import net.immortaldevs.sar.base.HungerModifier;
import net.immortaldevs.sar.base.SaturationModifier;
import net.minecraft.entity.effect.StatusEffects;

public class CumComponent extends Component {
    @Override
    public void init(LarvalComponentData data) {
        data.addModifier(SaturationModifier.add(0.4f));
        data.addModifier(HungerModifier.multiply(5, 4));
        data.addModifier(FoodEffectModifier.of(StatusEffects.STRENGTH, 940, 2));
    }
}
