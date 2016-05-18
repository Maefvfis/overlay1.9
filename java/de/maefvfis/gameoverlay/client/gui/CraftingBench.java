package de.maefvfis.gameoverlay.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import de.maefvfis.gameoverlay.GameOverlay;

public class CraftingBench extends GuiContainer  {

    private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");
    private static final String __OBFID = "CL_00000750";
    public static Minecraft mc = Minecraft.getMinecraft();
    public static ContainerWorkbench ContainerWorkbench = new ContainerWorkbench(Minecraft.getMinecraft().thePlayer.inventory, Minecraft.getMinecraft().theWorld, new BlockPos(0, 0, 0));
    
    
    
    public CraftingBench(InventoryPlayer p_i1084_1_, World p_i1084_2_, int p_i1084_3_, int p_i1084_4_, int p_i1084_5_)
    {
        super(ContainerWorkbench);
    }
    
    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
        //this.buttonList.add(new GuiButton(id, x, y, w, h, string));
        int buttonw = 40;
        int padding = 3;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(999, this.guiLeft + this.xSize - buttonw - padding, this.guiTop + padding, buttonw, 20, "Craft"));
        this.buttonList.add(new GuiButton(998, this.guiLeft + this.xSize - (buttonw * 2) - padding, this.guiTop + padding, buttonw, 20, "Clear"));
    }
    
    public void actionPerformed(GuiButton button)
    {
    	
    	Slot Result = (Slot) this.ContainerWorkbench.inventorySlots.get(0);
    	ItemStack ResultStack = null;
    	if(Result == null || !Result.getHasStack()) {
    		
    		//return;
    	} else {
    		ResultStack = Result.getStack().copy();
    	}

    	
    	ItemStack[] recipes = new ItemStack[9];
    	for(int i = 1; i < 10; i++) {
    		Slot recepieSlot = (Slot) this.ContainerWorkbench.inventorySlots.get(i);
    		if(recepieSlot.getHasStack()) {
    			recipes[i-1] = recepieSlot.getStack();
    		}
    	}
    	
    	GameOverlay.instance.BenchManager.init(ContainerWorkbench,recipes,ResultStack);
    	
    	
	    switch(button.id)
	    {
		    case 999: 
		    {
		    	GameOverlay.instance.BenchManager.craft();

		    	break;
		    }
		    case 998: 
		    {
		    	GameOverlay.instance.BenchManager.clearCraftingBench();
		    	break;
		    }
		}
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
    	ScaledResolution scaledresolution = new ScaledResolution(mc);
    	int screenwidth = scaledresolution.getScaledWidth();
    	
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.guiLeft, l, 0, 0, this.xSize, this.ySize);

    }

}
