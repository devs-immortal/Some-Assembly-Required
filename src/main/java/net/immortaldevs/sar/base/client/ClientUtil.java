package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.base.Util;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@SuppressWarnings("unused")
public final class ClientUtil {
    private static final Direction[] DIRECTIONS_AND_NULL = Arrays.copyOf(Direction.values(), 7);
    private static final Random RANDOM = new Random(42L);

    public static void getQuads(BakedModel model, Consumer<BakedQuad> consumer) {
        for (Direction direction : DIRECTIONS_AND_NULL) {
            List<BakedQuad> quads = model.getQuads(null, direction, resetRandom());
            if (!quads.isEmpty()) quads.forEach(consumer);
        }
    }

    public static List<BakedQuad>[] getQuads(BakedModel model) {
        return Util.arrayOf(
                model.getQuads(null, Direction.DOWN, resetRandom()),
                model.getQuads(null, Direction.UP, resetRandom()),
                model.getQuads(null, Direction.NORTH, resetRandom()),
                model.getQuads(null, Direction.SOUTH, resetRandom()),
                model.getQuads(null, Direction.WEST, resetRandom()),
                model.getQuads(null, Direction.EAST, resetRandom()),
                model.getQuads(null, null, resetRandom()));
    }

    private static Random resetRandom() {
        RANDOM.setSeed(42L);
        return RANDOM;
    }

    public static void renderModel(
            VertexConsumer consumer,
            BakedModel model,
            MatrixStack.Entry entry,
            float red,
            float green,
            float blue,
            int light,
            int overlay
    ) {
        for (Direction direction : DIRECTIONS_AND_NULL) {
            List<BakedQuad> quads = model.getQuads(null, direction, resetRandom());
            if (!quads.isEmpty()) for (BakedQuad quad : quads) {
                consumer.quad(entry, quad, red, green, blue, light, overlay);
            }
        }
    }

    public static void renderModel(
            VertexConsumer consumer,
            BakedModel model,
            MatrixStack.Entry entry,
            int light,
            int overlay
    ) {
        renderModel(consumer, model, entry, 1f, 1f, 1f, light, overlay);
    }
}
