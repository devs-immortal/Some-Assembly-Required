package net.immortaldevs.sar.base;

import com.google.gson.JsonElement;
import net.immortaldevs.sar.api.Component;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ComponentModelGenerator {
    public final BiConsumer<Identifier, Supplier<JsonElement>> writer;

    public ComponentModelGenerator(BiConsumer<Identifier, Supplier<JsonElement>> writer) {
        this.writer = writer;
    }

    public final void register(Component component, Model model) {
        Identifier id = SarModelIds.getComponentModelId(component);
        model.upload(id, TextureMap.layer0(id), this.writer);
    }

    public final void register(Component component, String suffix, Model model) {
        Identifier id = SarModelIds.getComponentSubModelId(component, suffix);
        model.upload(id, TextureMap.layer0(id), this.writer);
    }

    public final void register(Component component, Item base) {
        new Model(Optional.of(ModelIds.getItemModelId(base)), Optional.empty())
                .upload(SarModelIds.getComponentModelId(component), new TextureMap(), this.writer);
    }
}
