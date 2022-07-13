package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.ModelIdentifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class LoadedModel {
    private static final List<LoadedModel> MODELS = new ArrayList<>();

    private final ModelIdentifier id;
    private @Nullable BakedModel bakedModel;

    public LoadedModel(ModelIdentifier id) {
        this.id = id;
        MODELS.add(this);
    }

    public BakedModel getBakedModel() {
        if (this.bakedModel == null) throw new IllegalStateException();
        return this.bakedModel;
    }

    public static void reload() {
        BakedModelManager bakedModelManager = MinecraftClient.getInstance().getBakedModelManager();
        for (LoadedModel model : MODELS) {
            model.bakedModel = bakedModelManager.getModel(model.id);
        }
    }

    public static void emitIds(Consumer<? super ModelIdentifier> out) {
        for (LoadedModel model : MODELS) {
            out.accept(model.id);
        }
    }
}
