package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.ModifierMap;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A modifier that alters the texture of crack particles (eating, breaking, etc.)
 * If multiple are registered to the same map, they combine by choosing a random sprite
 * each time {@link #get} is called, weighted using on the number
 * returned by {@link #weight} (256 by default).
 */
@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface CrackParticleSpriteModifier extends Modifier {
    Sprite get(ItemStack stack, Random random);

    /**
     * When multiple modifiers are registered to the same map, this modifier has a
     * {@code weight / (sum of all weights)} chance of being chosen.
     */
    default int weight() {
        return 256;
    }

    /**
     * Implementation detail used to traverse the linked collection when selecting a
     * random sprite.
     */
    default Sprite traverse(ItemStack stack, Random random, int position) {
        return this.get(stack, random);
    }

    static CrackParticleSpriteModifier of(Supplier<BakedModel> model) {
        return (stack, random) -> model.get().getParticleSprite();
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
            this.weight = a.weight() + b.weight();
        }

        @Override
        public Sprite get(ItemStack stack, Random random) {
            return this.traverse(stack, random, random.nextInt(this.weight));
        }

        @Override
        public Sprite traverse(ItemStack stack, Random random, int position) {
            if (position < a.weight()) {
                return a.traverse(stack, random, position);
            } else {
                return b.traverse(stack, random, position - a.weight());
            }
        }

        @Override
        public int weight() {
            return this.weight;
        }
    }
}
