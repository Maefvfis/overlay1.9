package de.maefvfis.gameoverlay.client.crafting.recipes;

import de.maefvfis.gameoverlay.client.crafting.CustomInventoryCrafting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;


public class RecipeTippedArrow implements IRecipe
{
    private static final ItemStack[] EMPTY_ITEMS = new ItemStack[9];

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(CustomInventoryCrafting inv, World worldIn)
    {
        if (inv.getWidth() == 3 && inv.getHeight() == 3)
        {
            for (int i = 0; i < inv.getWidth(); ++i)
            {
                for (int j = 0; j < inv.getHeight(); ++j)
                {
                    ItemStack itemstack = inv.getStackInRowAndColumn(i, j);

                    if (itemstack == null)
                    {
                        return false;
                    }

                    Item item = itemstack.getItem();

                    if (i == 1 && j == 1)
                    {
                        if (item != Items.lingering_potion)
                        {
                            return false;
                        }
                    }
                    else if (item != Items.arrow)
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(CustomInventoryCrafting inv)
    {
        ItemStack itemstack = inv.getStackInRowAndColumn(1, 1);

        if (itemstack != null && itemstack.getItem() == Items.lingering_potion)
        {
            ItemStack itemstack1 = new ItemStack(Items.tipped_arrow, 8);
            PotionUtils.addPotionToItemStack(itemstack1, PotionUtils.getPotionFromItem(itemstack));
            PotionUtils.appendEffects(itemstack1, PotionUtils.getFullEffectsFromItem(itemstack));
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return 9;
    }

    public ItemStack getRecipeOutput()
    {
        return null;
    }

    public ItemStack[] getRemainingItems(CustomInventoryCrafting inv)
    {
        return EMPTY_ITEMS;
    }

	@Override
	public ItemStack[][] getRecipe(ItemStack Itemstack) {
		// TODO Auto-generated method stub
		return null;
	}
}