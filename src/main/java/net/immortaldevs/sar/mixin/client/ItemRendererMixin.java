package net.immortaldevs.sar.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.RootComponentData;
import net.immortaldevs.sar.base.client.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/RenderLayers;getItemLayer(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/client/render/RenderLayer;"),
            cancellable = true)
    private void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        Optional<RootComponentData> data = stack.sar$getComponentRoot();
        if (data.isEmpty()) return;

        ItemRendererModifier modifier = ClientComponents.getModifiers(data.get())
                .get(ItemRendererModifier.class);

        if (modifier == null) return;

        MultiVertexConsumerProvider provider = new MultiVertexConsumerProvider() {
            private Object lastLayer = null;
            private VertexConsumer lastConsumer = null;

            @Override
            public VertexConsumer get(MultiRenderLayer layer) {
                if (this.lastLayer == layer) return this.lastConsumer;
                this.lastLayer = layer;
                return this.lastConsumer = layer.get(vertexConsumers);
            }

            @Override
            public VertexConsumer get(RenderLayer layer) {
                if (this.lastLayer == layer) return this.lastConsumer;
                this.lastLayer = layer;
                return this.lastConsumer = vertexConsumers.getBuffer(layer);
            }

            @Override
            public VertexConsumer getImmediate(MultiRenderLayer layer) {
                return layer.get(vertexConsumers);
            }

            @Override
            public VertexConsumer getImmediate(RenderLayer layer) {
                return vertexConsumers.getBuffer(layer);
            }
        };

        modifier.applyItemRendererModifier(provider, stack, matrices, light, overlay);

        if (model == NullModel.INSTANCE) {
            matrices.pop();
            ci.cancel();
        }
    }
}
