package net.immortaldevs.sar.mixin.client;

import net.immortaldevs.sar.api.RootComponentData;
import net.immortaldevs.sar.base.client.ClientComponents;
import net.immortaldevs.sar.base.client.ItemRendererModifier;
import net.immortaldevs.sar.base.client.Util;
import net.immortaldevs.sar.base.client.VertexConsumerModifier;
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/model/BakedModel;isBuiltin()Z"),
            cancellable = true)
    private void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        Optional<RootComponentData> data = stack.sar$getComponentRoot();
        if (data.isEmpty()) return;

        ItemRendererModifier modifier = ClientComponents.getModifiers(data.get())
                .get(ItemRendererModifier.class);

        if (modifier == null) return;
        modifier.render(stack, matrices, vertexConsumers, light, overlay);
        matrices.pop();
        ci.cancel();
    }

    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V",
                    shift = At.Shift.BY,
                    by = -1))
    private VertexConsumer modifyVertexConsumer(VertexConsumer vertices, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
        Optional<RootComponentData> data = stack.sar$getComponentRoot();
        if (data.isEmpty()) return vertices;

        VertexConsumerModifier modifier = ClientComponents.getModifiers(data.get())
                .get(VertexConsumerModifier.class);

        if (modifier == null) return vertices;
        return modifier.apply(model, stack, matrices, vertexConsumers, vertices);
    }
}
