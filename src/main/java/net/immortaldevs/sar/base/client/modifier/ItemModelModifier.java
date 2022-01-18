package net.immortaldevs.sar.base.client.modifier;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.immortaldevs.sar.api.ModifierMap;
import net.immortaldevs.sar.api.Modifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;

import java.util.Random;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface ItemModelModifier extends Modifier {
    void emitQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context);

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(ItemModelModifier.class, this, (a, b) -> (s, r, c) -> {
            a.emitQuads(s, r, c);
            b.emitQuads(s, r, c);
        });
    }
}
