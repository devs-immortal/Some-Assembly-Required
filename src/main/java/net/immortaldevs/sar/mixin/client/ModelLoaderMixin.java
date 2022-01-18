package net.immortaldevs.sar.mixin.client;

import net.immortaldevs.sar.base.Sar;
import net.immortaldevs.sar.base.client.SarSprite;
import net.immortaldevs.sar.base.client.DynamicBakedModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.profiler.Profiler;
import org.apache.commons.lang3.tuple.Triple;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;

@Environment(EnvType.CLIENT)
@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Unique
    private static final JsonUnbakedModel SAR_GENERATION_MARKER = Util.make(
            JsonUnbakedModel.deserialize("{\"gui_light\": \"front\"}"),
            (jsonUnbakedModel) -> jsonUnbakedModel.id = "SAR generation marker");

    @Inject(method = "<init>",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/client/render/model/ModelLoader;DEFAULT_TEXTURES:Ljava/util/Set;",
                    opcode = Opcodes.GETSTATIC,
                    shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void init(ResourceManager resourceManager, BlockColors blockColors, Profiler profiler, int i, CallbackInfo ci, Set<?> iOException, Set<SpriteIdentifier> block) {
        block.addAll(SarSprite.freeze());
    }

    @Redirect(method = "bake",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel;getRootModel()Lnet/minecraft/client/render/model/json/JsonUnbakedModel;",
                    ordinal = 0))
    private JsonUnbakedModel getRootModel(JsonUnbakedModel instance) {
        JsonUnbakedModel ret = instance.getRootModel();
        return ret == SAR_GENERATION_MARKER ? ModelLoader.GENERATION_MARKER : ret;
    }

    @Inject(method = "bake",
            at = @At(value = "RETURN",
                    ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void bake(Identifier id, ModelBakeSettings settings, CallbackInfoReturnable<BakedModel> cir, Triple<?, ?, ?> triple, UnbakedModel unbakedModel, JsonUnbakedModel jsonUnbakedModel) {
        if (jsonUnbakedModel.getRootModel() == SAR_GENERATION_MARKER) {
            cir.setReturnValue(new DynamicBakedModel(cir.getReturnValue()));
        }
    }

    @Inject(method = "loadModelFromJson",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
        if (Sar.SAR.equals(id.getNamespace()) && "builtin/dynamic".equals(id.getPath())) {
            cir.setReturnValue(SAR_GENERATION_MARKER);
        }
    }
}
