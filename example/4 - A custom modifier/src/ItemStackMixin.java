import net.immortaldevs.sar.base.SarItemStack;
import net.immortaldevs.sar.impl.FinishUsingModifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements SarItemStack {
    @Inject(method = "finishUsing",
            at = @At("HEAD"))
    private void finishUsing(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        FinishUsingModifier modifier = this.getModifiers().get(FinishUsingModifier.class);
        if (modifier == null) return;
        modifier.apply((ItemStack) (Object) this, world, user);
    }
}
