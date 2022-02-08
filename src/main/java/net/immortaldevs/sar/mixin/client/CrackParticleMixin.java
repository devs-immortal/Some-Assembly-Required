package net.immortaldevs.sar.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.divineintervention.injection.ModifyOperand;
import net.immortaldevs.sar.api.RootComponentData;
import net.immortaldevs.sar.base.client.ClientComponents;
import net.immortaldevs.sar.base.client.CrackParticleSpriteModifier;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
@Mixin(CrackParticle.class)
public abstract class CrackParticleMixin extends SpriteBillboardParticle {
    private CrackParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @ModifyOperand(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDDLnet/minecraft/item/ItemStack;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/model/BakedModel;getParticleSprite()Lnet/minecraft/client/texture/Sprite;"))
    private Sprite modifySprite(Sprite sprite, ClientWorld world, double x, double y, double z, ItemStack stack) {
        Optional<RootComponentData> data;
        if ((data = stack.sar$getComponentRoot()).isEmpty()) return sprite;

        var modifier = ClientComponents.getModifiers(data.get())
                .get(CrackParticleSpriteModifier.class);

        if (modifier == null) return sprite;
        return modifier.applyCrackParticleSpriteModifier(stack, world, this.random);
    }
}
