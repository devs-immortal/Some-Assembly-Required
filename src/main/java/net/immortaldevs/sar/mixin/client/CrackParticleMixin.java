package net.immortaldevs.sar.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.base.client.DynamicBakedModel;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(CrackParticle.class)
public abstract class CrackParticleMixin extends SpriteBillboardParticle {
    private CrackParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Redirect(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDDLnet/minecraft/item/ItemStack;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/model/BakedModel;getParticleSprite()Lnet/minecraft/client/texture/Sprite;"))
    private Sprite init(BakedModel instance, ClientWorld world, double x, double y, double z, ItemStack stack) {
        if (instance instanceof DynamicBakedModel dynamicBakedModel) {
            return dynamicBakedModel.getParticleSprite(stack);
        }

        return instance.getParticleSprite();
    }
}
