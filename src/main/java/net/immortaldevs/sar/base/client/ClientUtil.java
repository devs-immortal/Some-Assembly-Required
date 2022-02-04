package net.immortaldevs.sar.base.client;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public final class ClientUtil {
    private static final Direction[] DIRECTIONS_AND_NULL = Arrays.copyOf(Direction.values(), 7);
    private static final Random RANDOM = new Random(42L);

    public static void getQuads(BakedModel model, Consumer<BakedQuad> consumer) {
        for (Direction direction : DIRECTIONS_AND_NULL) {
            RANDOM.setSeed(42L);
            List<BakedQuad> quads = model.getQuads(null, direction, RANDOM);
            if (!quads.isEmpty()) quads.forEach(consumer);
        }
    }
}
