package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.ComponentData;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class LoadedModelComponentModel extends BakedModelComponentModel {
    protected final LoadedModel loadedModel;

    public LoadedModelComponentModel(Component component) {
        super(component);
        this.loadedModel = new LoadedModel(new ModelIdentifier(component.getId(), "component"));
    }

    @Override
    protected BakedModel getItemBakedModel(ComponentData data,
                                           ItemStack stack,
                                           ModelTransformation.Mode renderMode) {
        return this.loadedModel.getBakedModel();
    }
}
