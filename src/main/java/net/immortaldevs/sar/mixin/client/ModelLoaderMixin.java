package net.immortaldevs.sar.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.base.client.ComponentModel;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Shadow
    @Final
    private Map<Identifier, UnbakedModel> unbakedModels;

    @Shadow
    protected abstract void addModel(ModelIdentifier modelId);

    @Shadow
    protected abstract void putModel(Identifier id, UnbakedModel unbakedModel);

    @Shadow
    protected abstract JsonUnbakedModel loadModelFromJson(Identifier id) throws IOException;

    @Inject(method = "<init>",
            at = @At(value = "INVOKE_STRING",
                    target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
                    args = "ldc=special"))
    private void init(ResourceManager resourceManager, BlockColors blockColors, Profiler profiler, int i, CallbackInfo ci) {
        profiler.swap("components");
        ComponentModel.emitIds(this::addModel);
    }

    @Inject(method = "loadModel",
            at = @At(value = "NEW",
                    target = "net/minecraft/util/Identifier",
                    ordinal = 1),
            cancellable = true)
    private void loadModel(Identifier id, CallbackInfo ci) throws IOException {
        ModelIdentifier modelId = (ModelIdentifier) id;
        if ("component".equals(modelId.getVariant())) {
            Identifier resourceId = new Identifier(id.getNamespace(), "component/" + id.getPath());
            UnbakedModel unbakedModel = this.loadModelFromJson(resourceId);
            this.putModel(modelId, unbakedModel);
            this.unbakedModels.put(resourceId, unbakedModel);
            ci.cancel();
        }
    }
}
