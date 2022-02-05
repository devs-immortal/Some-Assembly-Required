package net.immortaldevs.sar.test;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

import static net.immortaldevs.sar.base.Sar.id;

public class TestItems {
    public static final Item CHEESE = new Item(new Item.Settings().group(ItemGroup.FOOD).food(TestFoodComponents.CHEESE));
    public static final Item NITROGLYCERIN = new Item(new Item.Settings().group(ItemGroup.MISC).recipeRemainder(Items.GLASS_BOTTLE));

    public static void init() {
        Registry.register(Registry.ITEM, id("cheese"), CHEESE);
        Registry.register(Registry.ITEM, id("nitroglycerin"), NITROGLYCERIN);
    }
}
