package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;

import java.util.Random;
import java.util.function.Supplier;

/**
 * A modifier that alters the texture of crack particles (eating, breaking, etc.)
 * If multiple are registered to the same map, they combine by choosing a random sprite
 * each time {@link #applyCrackParticleSpriteModifier} is called, weighted using on the number
 * returned by {@link #crackParticleSpriteModifierWeight} (256 by default).
 */
@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface CrackParticleSpriteModifier extends Modifier {
    Sprite applyCrackParticleSpriteModifier(ItemStack stack, ClientWorld world, Random random);

    /**
     * When multiple modifiers are registered to the same map, this modifier has a
     * {@code weight / (sum of all weights)} chance of being chosen.
     */
    default int crackParticleSpriteModifierWeight() {
        return 256;
    }

    /**
     * Implementation detail used to traverse the linked collection when selecting a
     * random sprite.
     */
    default Sprite crackParticleSpriteModifierTraverse(
            ItemStack stack,
            ClientWorld world,
            Random random,
            int position
    ) {
        return this.applyCrackParticleSpriteModifier(stack, world, random);
    }

    static CrackParticleSpriteModifier of(Supplier<BakedModel> model) {
        return (stack, world, random) -> model.get().getParticleSprite();
    }

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(CrackParticleSpriteModifier.class, this, Compound::new);
    }

    /**
     * Randomly selects a sprite from one of two modifiers.
     */
    class Compound implements CrackParticleSpriteModifier {
        protected final CrackParticleSpriteModifier a;
        protected final CrackParticleSpriteModifier b;
        protected final int weight;

        public Compound(CrackParticleSpriteModifier a, CrackParticleSpriteModifier b) {
            this.a = a;
            this.b = b;
            this.weight = a.crackParticleSpriteModifierWeight() + b.crackParticleSpriteModifierWeight();
        }

        @Override
        public Sprite applyCrackParticleSpriteModifier(ItemStack stack, ClientWorld world, Random random) {
            return this.crackParticleSpriteModifierTraverse(stack, world, random, random.nextInt(this.weight));
        }

        @Override
        public Sprite crackParticleSpriteModifierTraverse(
                ItemStack stack,
                ClientWorld world,
                Random random,
                int position
        ) {
            if (position < a.crackParticleSpriteModifierWeight()) {
                return a.crackParticleSpriteModifierTraverse(stack, world, random, position);
            } else {
                return b.crackParticleSpriteModifierTraverse(stack, world, random,
                        position - a.crackParticleSpriteModifierWeight());
            }
        }

        @Override
        public int crackParticleSpriteModifierWeight() {
            return this.weight;
        }
    }
}
