package net.immortaldevs.sar.base.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ComponentModel implements Supplier<BakedModel> {
    private static final List<ComponentModel> MODELS = new ArrayList<>();

    private final ModelIdentifier id;
    private BakedModel bakedModel = null;

    public ComponentModel(String namespace, String path) {
        this(new ModelIdentifier(namespace, path, "component"));
    }

    public ComponentModel(ModelIdentifier id) {
        this.id = id;
        MODELS.add(this);
    }

    @Override
    public BakedModel get() {
        return this.bakedModel;
    }

    @Override
    public String toString() {
        return this.id.toString();
    }

    public static void reload() {
        for (ComponentModel model : MODELS) {
            model.bakedModel = MinecraftClient.getInstance().getBakedModelManager().getModel(model.id);
        }
    }

    public static void emitIds(Consumer<ModelIdentifier> consumer) {
        for (ComponentModel model : MODELS) {
            consumer.accept(model.id);
        }
    }

}
