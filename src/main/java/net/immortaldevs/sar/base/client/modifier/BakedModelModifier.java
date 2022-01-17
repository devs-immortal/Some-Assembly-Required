package net.immortaldevs.sar.base.client.modifier;

import net.immortaldevs.sar.api.ModifierMap;
import net.immortaldevs.sar.api.Modifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedModel;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public interface BakedModelModifier extends Modifier {
    void apply(Consumer<BakedModel> consumer);

    @Override
    default void register(ModifierMap modifierMap) {
        modifierMap.append(BakedModelModifier.class, this, bakedModelModifier -> consumer -> {
            bakedModelModifier.apply(consumer);
            this.apply(consumer);
        });
    }
}
