package net.immortaldevs.sar.test;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CumRecipe extends SpecialCraftingRecipe {
    public CumRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        boolean victim = false;
        boolean spray = false;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty()) continue;

            if (stack.isOf(TestItems.CUM_SPRAY)) {
                if (spray) return false;
                spray = true;
            } else {
                if (victim) return false;
                victim = true;
            }
        }

        return victim && spray;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty() || stack.isOf(TestItems.CUM_SPRAY)) continue;

            ItemStack victim = stack.copy();
            victim.sar$getOrCreateSkeletalComponentRoot()
                    .createChild("cum", TestComponents.CUM);

            return victim;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TestRecipeSerialisers.CUM;
    }
}
