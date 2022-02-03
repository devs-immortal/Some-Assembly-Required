package net.immortaldevs.sar.test;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;
import net.immortaldevs.sar.base.FoodEffectModifier;
import net.minecraft.world.explosion.Explosion;

public class NitroglycerinComponent extends Component {
    @Override
    public void init(LarvalComponentData data) {
        data.addModifier((FoodEffectModifier) (stack, world, targetEntity) -> {
            if (!world.isClient) {
                world.createExplosion(targetEntity,
                        targetEntity.getX(),
                        targetEntity.getEyeY(),
                        targetEntity.getZ(),
                        12f,
                        true,
                        Explosion.DestructionType.DESTROY);
            }
        });
    }
}
