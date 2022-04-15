import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface FinishUsingModifier extends Modifier {
    void apply(ItemStack stack, World world, LivingEntity user);

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(FinishUsingModifier.class, this, (a, b) -> (stack, world, user) -> {
            a.apply(stack, world, user);
            b.apply(stack, world, user);
        });
    }
}
