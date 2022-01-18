package net.immortaldevs.sar.base.client.modifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface ParticleSpriteModifier extends Modifier {
    Sprite get(ItemStack stack);

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.putIfAbsent(ParticleSpriteModifier.class, this);
    }
}
