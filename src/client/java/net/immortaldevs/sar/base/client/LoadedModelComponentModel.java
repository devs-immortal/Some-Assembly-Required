package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.ComponentNode;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
@SuppressWarnings("unused")
public class LoadedModelComponentModel extends BakedModelComponentModel {
    protected final LoadedModel loadedModel;

    public LoadedModelComponentModel(LoadedModel loadedModel) {
        this.loadedModel = loadedModel;
    }

    public LoadedModelComponentModel(Component component) {
        this(new LoadedModel(new ModelIdentifier(component.getId(), "component")));
    }

    @Override
    protected BakedModel getBakedModel(ComponentNode node,
                                       ItemStack stack,
                                       ModelTransformation.Mode renderMode) {
        return this.loadedModel.getBakedModel();
    }
}
