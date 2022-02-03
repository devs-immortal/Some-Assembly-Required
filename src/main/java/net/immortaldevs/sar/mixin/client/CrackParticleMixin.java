package net.immortaldevs.sar.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.RootComponentData;
import net.immortaldevs.sar.base.client.ClientComponents;
import net.immortaldevs.sar.base.client.CrackParticleSpriteModifier;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(CrackParticle.class)
public abstract class CrackParticleMixin extends SpriteBillboardParticle {
    private CrackParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDDLnet/minecraft/item/ItemStack;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/CrackParticle;setSprite(Lnet/minecraft/client/texture/Sprite;)V",
                    shift = At.Shift.AFTER))
    private void init(ClientWorld world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
        Optional<RootComponentData> data;
        if ((data = stack.sar$getComponentRoot()).isEmpty()) return;

        var modifier = ClientComponents.getModifiers(data.get())
                .get(CrackParticleSpriteModifier.class);

        if (modifier == null) return;
        this.setSprite(modifier.get(stack, this.random));
    }
}
