package net.immortaldevs.sar.base.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.immortaldevs.sar.api.ComponentData;
import net.immortaldevs.sar.api.ComponentHost;
import net.immortaldevs.sar.base.client.ComponentModel;
import net.immortaldevs.sar.impl.Util;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public final class ClientUtil {
    public static final Direction[] DIRECTIONS_AND_NULL = Arrays.copyOf(Direction.values(), 7);

    public static void traverseComponentModels(Iterator<ComponentData> iter,
                                               BiConsumer<ComponentData, ComponentModel> consumer,
                                               Runnable postChildren) {
        while (iter.hasNext()) {
            ComponentData data = iter.next();
            ComponentModel model = ComponentModel.get(data.getComponent());
            if (model != null) consumer.accept(data, model);
            traverseComponentModels(data.loadedChildIterator(), consumer, postChildren);
            if (model != null) postChildren.run();
        }
    }
}
