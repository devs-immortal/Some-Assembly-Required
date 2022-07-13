package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.ComponentNode;
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
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class BakedModelComponentModel implements ComponentModel {
    private static final Direction[] DIRECTIONS_AND_NULL = Arrays.copyOf(Direction.values(), 7);

    @Override
    public @Nullable Sprite getParticleSprite(ComponentNode node, ItemStack stack) {
        return this.getBakedModel(node, stack, ModelTransformation.Mode.NONE).getParticleSprite();
    }

    @Override
    public void render(ComponentNode node,
                       Children children,
                       VertexConsumerProvider vertexConsumers,
                       ItemStack stack,
                       MatrixStack matrices,
                       ModelTransformation.Mode renderMode,
                       int light,
                       int overlay) {
        Random random = Random.create(42L);
        VertexConsumer consumer = this.getVertexConsumer(node, vertexConsumers, stack, renderMode);
        MatrixStack.Entry entry = matrices.peek();
        for (Direction direction : DIRECTIONS_AND_NULL) {
            random.setSeed(42L);
            List<BakedQuad> quads = this.getBakedModel(node, stack, renderMode)
                    .getQuads(null, direction, random);
            for (BakedQuad quad : quads) {
                consumer.quad(entry, quad, 1f, 1f, 1f, light, overlay);
            }
        }
    }

    protected abstract BakedModel getBakedModel(ComponentNode node,
                                                ItemStack stack,
                                                ModelTransformation.Mode renderMode);

    @SuppressWarnings("unused")
    protected VertexConsumer getVertexConsumer(ComponentNode node,
                                               VertexConsumerProvider vertexConsumers,
                                               ItemStack stack,
                                               ModelTransformation.Mode renderMode) {
        return VertexConsumers.union(
                vertexConsumers.getBuffer(SarTexturedRenderLayers.TRANSLUCENT_GHOST),
                vertexConsumers.getBuffer(SarTexturedRenderLayers.ENTITY_TRANSLUCENT_CULL_LAYERED));
    }
}
