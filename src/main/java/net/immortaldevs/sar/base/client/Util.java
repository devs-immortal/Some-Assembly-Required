package net.immortaldevs.sar.base.client;

import net.immortaldevs.sar.api.Modifier;
import net.immortaldevs.sar.api.RootComponentData;
import net.immortaldevs.sar.base.ItemStackExt;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class Util {
    private static final Direction[] DIRECTIONS_AND_NULL = Arrays.copyOf(Direction.values(), 7);
    private static final Random RANDOM = new Random(42L);

    public static void getQuads(BakedModel model, Consumer<BakedQuad> consumer) {
        for (Direction direction : DIRECTIONS_AND_NULL) {
            RANDOM.setSeed(42L);
            List<BakedQuad> quads = model.getQuads(null, direction, RANDOM);
            if (!quads.isEmpty()) quads.forEach(consumer);
        }
    }

    public static <T extends Modifier> @Nullable T getModifier(ItemStackExt stack, Class<T> type) {
        Optional<RootComponentData> data = stack.sar$getComponentRoot();
        if (data.isEmpty()) return null;
        return data.get().modifiers().get(type);
    }

    @SafeVarargs
    public static <T> T[] arrayOf(T... values) {
        return values;
    }
}
