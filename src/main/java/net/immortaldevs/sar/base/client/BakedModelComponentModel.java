package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.Component;
import net.immortaldevs.sar.api.ComponentData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

@Environment(EnvType.CLIENT)
public abstract class BakedModelComponentModel extends ComponentModel {
    public BakedModelComponentModel(Component component) {
        super(component);
    }

    @Override
    public @Nullable Sprite getParticleSprite(ComponentData data, ItemStack stack) {
        return this.getItemBakedModel(data, stack, ModelTransformation.Mode.NONE).getParticleSprite();
    }

    @Override
    public void itemRender(ComponentData data,
                           VertexConsumerProvider vertexConsumers,
                           ItemStack stack,
                           MatrixStack matrices,
                           ModelTransformation.Mode renderMode,
                           int light,
                           int overlay) {
        Random random = new Random();
        VertexConsumer consumer = this.getItemVertexConsumer(data, vertexConsumers, stack, renderMode);
        MatrixStack.Entry entry = matrices.peek();
        for (Direction direction : ClientUtil.DIRECTIONS_AND_NULL) {
            random.setSeed(42L);
            List<BakedQuad> quads = this.getItemBakedModel(data, stack, renderMode)
                    .getQuads(null, direction, random);
            for (BakedQuad quad : quads) {
                consumer.quad(entry, quad, 1f, 1f, 1f, light, overlay);
            }
        }
    }

    protected abstract BakedModel getItemBakedModel(ComponentData data,
                                                    ItemStack stack,
                                                    ModelTransformation.Mode renderMode);

    @SuppressWarnings("unused")
    protected VertexConsumer getItemVertexConsumer(ComponentData data,
                                                   VertexConsumerProvider vertexConsumers,
                                                   ItemStack stack,
                                                   ModelTransformation.Mode renderMode) {
        return VertexConsumers.union(
                vertexConsumers.getBuffer(SarTexturedRenderLayers.TRANSLUCENT_GHOST),
                vertexConsumers.getBuffer(SarTexturedRenderLayers.ENTITY_TRANSLUCENT_CULL_LAYERED));
    }
}
