package de.maefvfis.gameoverlay.client.crafting.recipes;


import de.maefvfis.gameoverlay.client.crafting.CustomInventoryCrafting;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ShapelessRecipes implements IRecipe
{
	 private final ItemStack recipeOutput;
	    public final List recipeItems;
	    private static final String __OBFID = "CL_00000094";

	    public ShapelessRecipes(ItemStack output, List inputList)
	    {
	        this.recipeOutput = output;
	        this.recipeItems = inputList;
	    }

	    public ItemStack getRecipeOutput()
	    {
	        return this.recipeOutput;
	    }

	    public ItemStack[] getRemainingItems(CustomInventoryCrafting p_179532_1_)
	    {
	        ItemStack[] aitemstack = new ItemStack[p_179532_1_.getSizeInventory()];

	        for (int i = 0; i < aitemstack.length; ++i)
	        {
	            ItemStack itemstack = p_179532_1_.getStackInSlot(i);
	            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
	        }

	        return aitemstack;
	    }

	    public boolean matches(CustomInventoryCrafting p_77569_1_, World worldIn)
	    {
	        ArrayList arraylist = Lists.newArrayList(this.recipeItems);

	        for (int i = 0; i < 3; ++i)
	        {
	            for (int j = 0; j < 3; ++j)
	            {
	                ItemStack itemstack = p_77569_1_.getStackInRowAndColumn(j, i);

	                if (itemstack != null)
	                {
	                    boolean flag = false;
	                    Iterator iterator = arraylist.iterator();

	                    while (iterator.hasNext())
	                    {
	                        ItemStack itemstack1 = (ItemStack)iterator.next();

	                        if (itemstack.getItem() == itemstack1.getItem() && (itemstack1.getMetadata() == 32767 || itemstack.getMetadata() == itemstack1.getMetadata()))
	                        {
	                            flag = true;
	                            arraylist.remove(itemstack1);
	                            break;
	                        }
	                    }

	                    if (!flag)
	                    {
	                        return false;
	                    }
	                }
	            }
	        }

	        return arraylist.isEmpty();
	    }

	    public ItemStack getCraftingResult(CustomInventoryCrafting p_77572_1_)
	    {
	        return this.recipeOutput.copy();
	    }

	    public int getRecipeSize()
	    {
	        return this.recipeItems.size();
	    }

	@Override
	public ItemStack[][] getRecipe(ItemStack Itemstack) {
		// TODO Auto-generated method stub
		
		ItemStack[][] result = new ItemStack[3][3];
		ArrayList arraylist = new ArrayList(this.recipeItems);
		
		Iterator iterator = arraylist.iterator();
		
		int i = 0;
		while (iterator.hasNext())
        {
            ItemStack itemstack = (ItemStack)iterator.next();
            
            result[(int)i/3][i % 3] = itemstack;
            
            i++;
           
        }

		
		
		//Debug.println("Recipe", recipeItems.toString());
		return result;
	}


}