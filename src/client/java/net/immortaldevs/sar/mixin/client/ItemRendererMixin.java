package net.immortaldevs.sar.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.base.ModelModifier;
import net.immortaldevs.sar.base.client.ComponentModel;
import net.immortaldevs.sar.base.client.ComponentModels;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            slice = @Slice(
                    from = @At(value = "INVOKE",
                            target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V")),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
    private void renderItem(ItemStack stack,
                            ModelTransformation.Mode renderMode,
                            boolean leftHanded,
                            MatrixStack matrices,
                            VertexConsumerProvider vertexConsumers,
                            int light,
                            int overlay,
                            BakedModel model,
                            CallbackInfo ci) {
        ModelModifier modifier = stack.getModifier(ModelModifier.class);
        if (modifier != null) render(modifier, vertexConsumers, stack, matrices, renderMode, light, overlay);
    }

    @Unique
    private static void render(ModelModifier modifier,
                               VertexConsumerProvider vertexConsumers,
                               ItemStack stack,
                               MatrixStack matrices,
                               ModelTransformation.Mode renderMode,
                               int light,
                               int overlay) {
        for (ModelModifier sibling = modifier; sibling != null; sibling = sibling.sibling) {
            ComponentModel model = ComponentModels.get(sibling.node.getComponent());
            if (model == null) continue;
            ModelModifier child = sibling.child;
            model.render(sibling.node,
                    child == null
                            ? () -> {}
                            : () -> render(child, vertexConsumers, stack, matrices, renderMode, light, overlay),
                    vertexConsumers,
                    stack,
                    matrices,
                    renderMode,
                    light,
                    overlay);
        }
    }
}
