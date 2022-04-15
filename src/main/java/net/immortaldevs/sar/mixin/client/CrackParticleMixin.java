package net.immortaldevs.sar.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.divineintervention.injection.ModifyOperand;
import net.immortaldevs.sar.api.ComponentData;
import net.immortaldevs.sar.base.client.ClientUtil;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        Iterator<ComponentData> iter = stack.componentIterator();
        if (!iter.hasNext()) return sprite;

        List<Sprite> sprites = new ArrayList<>();
        sprites.add(sprite);
        ClientUtil.traverseComponentModels(iter,
                (data, model) -> {
                    Sprite componentSprite = model.getParticleSprite(data, stack);
                    if (componentSprite != null) sprites.add(componentSprite);
                }, () -> {});

        return sprites.get(this.random.nextInt(sprites.size()));
    }
}
