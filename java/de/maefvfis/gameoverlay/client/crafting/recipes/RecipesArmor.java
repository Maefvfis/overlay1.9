package de.maefvfis.gameoverlay.client.crafting.recipes;


import de.maefvfis.gameoverlay.client.crafting.CustomCraftingManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesArmor
{
    private String[][] recipePatterns = new String[][] {{"XXX", "X X"}, {"X X", "XXX", "XXX"}, {"XXX", "X X", "X X"}, {"X X", "X X"}};
    private Item[][] recipeItems = new Item[][] {{Items.LEATHER, Items.IRON_INGOT, Items.DIAMOND, Items.GOLD_INGOT}, {Items.LEATHER_HELMET, Items.IRON_HELMET, Items.DIAMOND_HELMET, Items.GOLDEN_HELMET}, {Items.LEATHER_CHESTPLATE, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.GOLDEN_CHESTPLATE}, {Items.LEATHER_LEGGINGS, Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS, Items.GOLDEN_LEGGINGS}, {Items.LEATHER_BOOTS, Items.IRON_BOOTS, Items.DIAMOND_BOOTS, Items.GOLDEN_BOOTS}};

    /**
     * Adds the armor recipes to the CraftingManager.
     */
    public void addRecipes(CustomCraftingManager craftManager)
    {
        for (int i = 0; i < this.recipeItems[0].length; ++i)
        {
            Item item = this.recipeItems[0][i];

            for (int j = 0; j < this.recipeItems.length - 1; ++j)
            {
                Item item1 = this.recipeItems[j + 1][i];
                craftManager.addRecipe(new ItemStack(item1), new Object[] {this.recipePatterns[j], 'X', item});
            }
        }
    }
}