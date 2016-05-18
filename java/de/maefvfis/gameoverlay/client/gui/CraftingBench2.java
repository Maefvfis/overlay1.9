package de.maefvfis.gameoverlay.client.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CraftingBench2 extends GuiContainer
{
    private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");
    private static final String __OBFID = "CL_00000750";

    public CraftingBench2(InventoryPlayer playerInv, World worldIn)
    {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }

    public CraftingBench2(InventoryPlayer playerInv, World worldIn, BlockPos blockPosition)
    {
        super(new ContainerWorkbench(playerInv, worldIn, blockPosition));
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
        
        int buttonw = 40;
        int padding = 3;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(999, this.guiLeft + this.xSize - buttonw - padding, this.guiTop + padding, buttonw, 20, "All"));
        this.buttonList.add(new GuiButton(998, this.guiLeft + this.xSize - (buttonw * 2) - padding, this.guiTop + padding, buttonw, 20, "Stack"));
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}