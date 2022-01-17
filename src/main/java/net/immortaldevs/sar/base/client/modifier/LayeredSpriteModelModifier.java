package net.immortaldevs.sar.base.client.modifier;

import net.immortaldevs.sar.api.ModifierMap;
import net.immortaldevs.sar.base.client.SarSprite;
import net.immortaldevs.sar.base.client.LayeredSpriteBakedModel;
import net.immortaldevs.sar.base.client.LayeredSpriteBakedModel.Layer;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedModel;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public final class LayeredSpriteModelModifier implements BakedModelModifier {
    private ModifierMap modifierMap;
    private ObjectSet<LayerId> layersSeen;
    private BakedModel model;

    public void apply(Consumer<BakedModel> consumer) {
        Object2FloatMap<LayerId> layerIds = new Object2FloatOpenHashMap<>();

        SpriteLayerModifier spriteLayerModifier = modifierMap.get(SpriteLayerModifier.class);
        if (spriteLayerModifier == null) return;

        spriteLayerModifier.apply((sprite, x, y, z) ->
                layerIds.computeFloat(new LayerId(sprite, x, y),
                        (key, value) -> value == null ? z : Math.max(value, z)));

        if (!layerIds.keySet().equals(this.layersSeen) || this.model == null) {
            this.layersSeen = layerIds.keySet();

            Layer[] layers = layerIds.object2FloatEntrySet().stream()
                    .sorted((a, b) -> Float.compare(a.getFloatValue(), b.getFloatValue()))
                    .map(entry -> new Layer(entry.getKey().sprite.id.getSprite(),
                            entry.getKey().x, entry.getKey().y))
                    .toArray(Layer[]::new);

            // todo particle sprite
            this.model = new LayeredSpriteBakedModel(layers, layers[0].sprite());
        }

        consumer.accept(this.model);
    }

    @Override
    public void register(ModifierMap modifierMap) {
        if (!modifierMap.contains(LayeredSpriteModelModifier.class)) {
            BakedModelModifier.super.register(modifierMap);
            modifierMap.put(LayeredSpriteModelModifier.class, this);
        }

        this.modifierMap = modifierMap;
    }

    @Environment(EnvType.CLIENT)
    private record LayerId(
            SarSprite sprite,
            int x,
            int y
    ) {
    }
}
