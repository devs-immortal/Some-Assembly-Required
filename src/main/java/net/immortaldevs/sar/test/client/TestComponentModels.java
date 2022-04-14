package net.immortaldevs.sar.test.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.ComponentData;
import net.immortaldevs.sar.base.client.*;
import net.immortaldevs.sar.test.PotatoComponent;
import net.immortaldevs.sar.test.TestComponents;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;

import static net.immortaldevs.sar.impl.Sar.SAR;

@Environment(EnvType.CLIENT)
public final class TestComponentModels {
    public static void init() {
        new BakedModelComponentModel(TestComponents.POTATO) {
            private static final LoadedModel POTATO =
                    new LoadedModel(new ModelIdentifier(SAR, "potato", "component"));
            private static final LoadedModel FILLED_POTATO =
                    new LoadedModel(new ModelIdentifier(SAR, "filled_potato", "component"));

            @Override
            protected BakedModel getItemBakedModel(ComponentData data, ItemStack stack, ModelTransformation.Mode renderMode) {
                return data.containsChild("filling")
                        ? FILLED_POTATO.getBakedModel()
                        : POTATO.getBakedModel();
            }
        };

        new BakedModelComponentModel(TestComponents.CHEESE) {
            private static final LoadedModel CHEESE =
                    new LoadedModel(new ModelIdentifier(SAR, "cheese", "component"));
            private static final LoadedModel POTATO_CHEESE =
                    new LoadedModel(new ModelIdentifier(SAR, "potato_cheese", "component"));

            @Override
            protected BakedModel getItemBakedModel(ComponentData data, ItemStack stack, ModelTransformation.Mode renderMode) {
                return data.getParent() != null && data.getParent().getComponent() instanceof PotatoComponent
                        ? POTATO_CHEESE.getBakedModel()
                        : CHEESE.getBakedModel();
            }
        };

        new BakedModelComponentModel(TestComponents.NITROGLYCERIN) {
            private static final LoadedModel NITROGLYCERIN =
                    new LoadedModel(new ModelIdentifier(SAR, "nitroglycerin", "component"));
            private static final LoadedModel POTATO_NITROGLYCERIN =
                    new LoadedModel(new ModelIdentifier(SAR, "potato_nitroglycerin", "component"));

            @Override
            protected BakedModel getItemBakedModel(ComponentData data, ItemStack stack, ModelTransformation.Mode renderMode) {
                return data.getParent() != null && data.getParent().getComponent() instanceof PotatoComponent
                        ? POTATO_NITROGLYCERIN.getBakedModel()
                        : NITROGLYCERIN.getBakedModel();
            }
        };
    }
}
