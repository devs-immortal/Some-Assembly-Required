package net.immortaldevs.sar.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.base.client.LoadedModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Final
    private ReloadableResourceManagerImpl resourceManager;

    @Inject(method = "<init>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;<init>(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/client/render/model/BakedModelManager;Lnet/minecraft/client/color/item/ItemColors;Lnet/minecraft/client/render/item/BuiltinModelItemRenderer;)V",
                    shift = At.Shift.AFTER))
    private void init(RunArgs args, CallbackInfo ci) {
        this.resourceManager.registerReloader((SynchronousResourceReloader) manager -> LoadedModel.reload());
    }
}
