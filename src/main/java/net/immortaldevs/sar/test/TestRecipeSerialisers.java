package net.immortaldevs.sar.test;

import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.registry.Registry;

import static net.immortaldevs.sar.base.Sar.id;

public final class TestRecipeSerialisers {
    public static final SpecialRecipeSerializer<PotatoRecipe> POTATO = new SpecialRecipeSerializer<>(PotatoRecipe::new);

    public static void init() {
        Registry.register(Registry.RECIPE_SERIALIZER, id("crafting_special_potato"), POTATO);
    }
}
