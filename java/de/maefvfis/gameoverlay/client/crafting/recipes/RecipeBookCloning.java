package de.maefvfis.gameoverlay.client.crafting.recipes;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import de.maefvfis.gameoverlay.client.crafting.CustomInventoryCrafting;

public class RecipeBookCloning implements IRecipe
{
    private static final String __OBFID = "CL_00000081";

    public boolean matches(CustomInventoryCrafting p_77569_1_, World worldIn)
    {
        int i = 0;
        ItemStack itemstack = null;

        for (int j = 0; j < p_77569_1_.getSizeInventory(); ++j)
        {
            ItemStack itemstack1 = p_77569_1_.getStackInSlot(j);

            if (itemstack1 != null)
            {
                if (itemstack1.getItem() == Items.written_book)
                {
                    if (itemstack != null)
                    {
                        return false;
                    }

                    itemstack = itemstack1;
                }
                else
                {
                    if (itemstack1.getItem() != Items.writable_book)
                    {
                        return false;
                    }

                    ++i;
                }
            }
        }

        return itemstack != null && i > 0;
    }

    public ItemStack getCraftingResult(CustomInventoryCrafting p_77572_1_)
    {
        int i = 0;
        ItemStack itemstack = null;

        for (int j = 0; j < p_77572_1_.getSizeInventory(); ++j)
        {
            ItemStack itemstack1 = p_77572_1_.getStackInSlot(j);

            if (itemstack1 != null)
            {
                if (itemstack1.getItem() == Items.written_book)
                {
                    if (itemstack != null)
                    {
                        return null;
                    }

                    itemstack = itemstack1;
                }
                else
                {
                    if (itemstack1.getItem() != Items.writable_book)
                    {
                        return null;
                    }

                    ++i;
                }
            }
        }

        if (itemstack != null && i >= 1 && ItemWrittenBook.getGeneration(itemstack) < 2)
        {
            ItemStack itemstack2 = new ItemStack(Items.written_book, i);
            itemstack2.setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
            itemstack2.getTagCompound().setInteger("generation", ItemWrittenBook.getGeneration(itemstack) + 1);

            if (itemstack.hasDisplayName())
            {
                itemstack2.setStackDisplayName(itemstack.getDisplayName());
            }

            return itemstack2;
        }
        else
        {
            return null;
        }
    }

    public int getRecipeSize()
    {
        return 9;
    }

    public ItemStack getRecipeOutput()
    {
        return null;
    }

    public ItemStack[] getRemainingItems(InventoryCrafting inv)
    {
        ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);

            if (itemstack != null && itemstack.getItem() instanceof ItemWrittenBook)
            {
                aitemstack[i] = itemstack.copy();
                aitemstack[i].stackSize = 1;
                break;
            }
        }

        return aitemstack;
    }

	@Override
	public ItemStack[][] getRecipe(ItemStack Itemstack) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack[] getRemainingItems(CustomInventoryCrafting p_180303_1_) {
		// TODO Auto-generated method stub
		return null;
	}


}