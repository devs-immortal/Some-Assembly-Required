package net.immortaldevs.sar.base.client;

import net.immortaldevs.sar.base.Util;
import net.immortaldevs.sar.base.client.modifier.BakedModelModifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
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

        Util.acceptModifier(stack, BakedModelModifier.class, bakedModelModifier ->
                bakedModelModifier.apply(bakedModel ->
                        ((FabricBakedModel) bakedModel).emitItemQuads(stack, randomSupplier, context)));
    }

    @Override
    public Sprite getParticleSprite() {
        return super.getParticleSprite();
    }
}
