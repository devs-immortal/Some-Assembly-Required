package net.immortaldevs.sar.test;

import net.immortaldevs.sar.api.Component;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class PotatoRecipe extends SpecialCraftingRecipe {
    public PotatoRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        boolean potato = false;
        boolean filling = false;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty()) continue;

            if (stack.isOf(TestItems.CHEESE) || stack.isOf(TestItems.NITROGLYCERIN)) {
                if (filling) return false;
                filling = true;
            } else if (stack.isOf(Items.POTATO)) {
                if (potato) return false;
                potato = true;
            } else {
                return false;
            }
        }

        return potato && filling;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        ItemStack potato = null;
        Component filling = null;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isOf(TestItems.CHEESE)) {
                filling = TestComponents.CHEESE;
            } else if (stack.isOf(TestItems.NITROGLYCERIN)) {
                filling = TestComponents.NITROGLYCERIN;
            } else if (stack.isOf(Items.POTATO)) {
                potato = stack.copy();
            }

        }

        if (potato == null || filling == null) {
            return ItemStack.EMPTY;
        }

        potato.sar$getOrCreateSkeletalComponentRoot()
                .getOrCreateChild("potato", TestComponents.POTATO)
                .createChild("filling", filling);

        return potato;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TestRecipeSerialisers.POTATO;
    }
}
