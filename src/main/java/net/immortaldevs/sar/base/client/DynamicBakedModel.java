package net.immortaldevs.sar.base.client;

import net.immortaldevs.sar.base.ModifierUtils;
import net.immortaldevs.sar.base.client.modifier.ItemModelModifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.immortaldevs.sar.base.client.modifier.ParticleSpriteModifier;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class DynamicBakedModel extends ForwardingBakedModel {
    public DynamicBakedModel(BakedModel wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        super.emitItemQuads(stack, randomSupplier, context);

        ModifierUtils.acceptModifier(stack, ItemModelModifier.class, itemModelModifier ->
                itemModelModifier.emitQuads(stack, randomSupplier, context));
    }

    public Sprite getParticleSprite(ItemStack stack) {
        Sprite ret = ModifierUtils.applyModifier(stack, ParticleSpriteModifier.class, particleSpriteModifier ->
                particleSpriteModifier.get(stack));
        return ret == null ? this.getParticleSprite() : ret;
    }
}
