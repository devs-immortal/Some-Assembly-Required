package net.immortaldevs.sar.base.client.modifier;

import net.immortaldevs.sar.api.ModifierMap;
import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.base.client.SarSprite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface SpriteLayerModifier extends Modifier {
    void apply(SpriteConsumer consumer);

    default SpriteLayerModifier translate(int x, int y, float z) {
        return consumer -> this.apply((sprite, x0, y0, z0) ->
                consumer.accept(sprite, x0 + x, y0 + y, z0 + z));
    }

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.merge(SpriteLayerModifier.class, this, (a, b) -> consumer -> {
            a.apply(consumer);
            b.apply(consumer);
        });
    }

    @Environment(EnvType.CLIENT)
    @FunctionalInterface
    interface SpriteConsumer {
        void accept(SarSprite sprite, int x, int y, float z);

        default void accept(SarSprite sprite, float z) {
            this.accept(sprite, 0, 0, z);
        }
    }
}
