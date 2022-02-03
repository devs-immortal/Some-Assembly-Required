package net.immortaldevs.sar.test;

import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.LarvalComponentData;
import net.immortaldevs.sar.base.FoodEffectModifier;
import net.immortaldevs.sar.base.UseOnBlockModifier;
import net.immortaldevs.sar.base.UseOnEntityModifier;
import net.minecraft.block.Blocks;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
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

        data.addModifier((UseOnEntityModifier) (stack, user, entity, hand) -> {
            entity.setOnFireFor(6);
            entity.playSound(SoundEvents.BLOCK_HONEY_BLOCK_PLACE, 0.4f, 1.5f);
            return ActionResult.SUCCESS;
        });

        data.addModifier((UseOnBlockModifier) context -> {
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.CUT_RED_SANDSTONE.getDefaultState());
            return ActionResult.SUCCESS;
        });
    }
}
