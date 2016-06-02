package de.maefvfis.gameoverlay.client.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import de.maefvfis.gameoverlay.client.crafting.CustomCraftingManager;
import de.maefvfis.gameoverlay.client.crafting.recipes.IRecipe;
@SideOnly(Side.CLIENT)
public class CreativeInv2 extends InventoryEffectRenderer
{
    /** The location of the creative inventory tabs texture */
    private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private static InventoryBasic basicInventory = new InventoryBasic("tmp", true, 45);
    /** Currently selected creative inventory tab index. */
    private static int selectedTabIndex = CreativeTabs.BUILDING_BLOCKS.getTabIndex();
    /** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
    private float currentScroll;
    /** True if the scrollbar is being dragged */
    private boolean isScrolling;
    /** True if the left mouse button was held down last time drawScreen was called. */
    private boolean wasClicking;
    private GuiTextField searchField;
    private List<Slot> originalSlots;
    private Slot destroyItemSlot;
    private boolean clearSearch;
    private CreativeCrafting listener;
    private static int tabPage = 0;
    private int maxPages = 0;
    
    private static InventoryBasic recipefield = new InventoryBasic("recipefield", true, 9);
    private static IInventory craftResult = new InventoryCraftResult();
    
    
    public CreativeInv2(EntityPlayer player)
    {   
        super(new CreativeInv2.ContainerCreative(player));
        player.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
        
        
    }
    
    
    
    public void resetCrafting() {
    	for(int i = 0; i < 3; i++) {
			for(int ii = 0; ii < 3; ii++) {
				recipefield.setInventorySlotContents((i * 3) + ii, null);
				
			}
    	}
    }
    
    public void setCrafting(ItemStack item,int x,int y) {
    	
    	
    	
    	resetCrafting();
    	CreativeInv2.ContainerCreative.x = x;
    	CreativeInv2.ContainerCreative.y = y;
        if (item != null)
        {
            InventoryPlayer inventoryplayer1 = this.mc.thePlayer.inventory;
            
            if (item.getItem() != null) {
	    		ItemStack itemstack = item;
	    		ItemStack itemstack2;
	    		
	    		List recipes = CustomCraftingManager.getInstance().getRecipeList();
	    		
	            for (int j = 0; j < recipes.size(); ++j)
	            {
	                IRecipe irecipe = (IRecipe)recipes.get(j);
	                itemstack2 = irecipe.getRecipeOutput();
	                if(itemstack2 != null && itemstack2.getItem() != null && itemstack != null && itemstack.getItem() != null) {
		                if(itemstack2.getItem() == itemstack.getItem() && itemstack.getItemDamage() == itemstack2.getItemDamage()) {
		                	
		                	ItemStack[][] RezeptAr = irecipe.getRecipe(itemstack2);
		                
		                	
		                	
		                	if(irecipe.getRecipe(itemstack2) != null) {
		                		craftResult.setInventorySlotContents(0, itemstack2);
		                	//irecipe.
		                		for(int i = 0; i < RezeptAr.length; i++) {
		                			for(int ii = 0; ii < RezeptAr[i].length; ii++) {
		                				recipefield.setInventorySlotContents((i * 3) + ii, null);
		                				ItemStack Rezeptitem = RezeptAr[i][ii];
		                				if(Rezeptitem != null) {
		                					Rezeptitem.stackSize = 1;
		                					
		                					if(Rezeptitem.getItemDamage() == 32767) {
		                						Rezeptitem.setItemDamage(0);
		                					}
		                					recipefield.setInventorySlotContents((i * 3) + ii, Rezeptitem);
		                					
		                				}
		                			}
		                		}

			                	break;
		                	}
		                }
	                }
	                
	
	            }
	    	}
        }
    }
    
    public void updateScreen()
    {

    }
    @Override
    protected void updateActivePotionEffects()
    {

    }

    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type)
    {
    	
    }

    public void initGui()
    {

        super.initGui();
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT);
        this.searchField.setMaxStringLength(15);
        this.searchField.setEnableBackgroundDrawing(false);
        this.searchField.setVisible(false);
        this.searchField.setTextColor(16777215);
        int i = selectedTabIndex;
        selectedTabIndex = -1;
        this.setCurrentCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY[i]);
        this.listener = new CreativeCrafting(this.mc);
        this.mc.thePlayer.inventoryContainer.addListener(this.listener);
        int tabCount = CreativeTabs.CREATIVE_TAB_ARRAY.length;
        if (tabCount > 12)
        {
            buttonList.add(new GuiButton(101, guiLeft,              guiTop - 50, 20, 20, "<"));
            buttonList.add(new GuiButton(102, guiLeft + xSize - 20, guiTop - 50, 20, 20, ">"));
            maxPages = ((tabCount - 12) / 10) + 1;
        }

    }

    public void onGuiClosed()
    {
        super.onGuiClosed();

        if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null)
        {
            this.mc.thePlayer.inventoryContainer.removeListener(this.listener);
        }

        Keyboard.enableRepeatEvents(false);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    	if (!CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex].hasSearchBar())
        {
            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat))
            {
                this.setCurrentCreativeTab(CreativeTabs.SEARCH);
            }
            else
            {
                super.keyTyped(typedChar, keyCode);
            }
        }
        else
        {
            if (this.clearSearch)
            {
                this.clearSearch = false;
                this.searchField.setText("");
            }

            if (!this.checkHotbarKeys(keyCode))
            {
                if (this.searchField.textboxKeyTyped(typedChar, keyCode))
                {
                    this.updateCreativeSearch();
                }
                else
                {
                    super.keyTyped(typedChar, keyCode);
                }
            }
        }
    }

    private void updateCreativeSearch()
    {
    	 CreativeInv2.ContainerCreative guicontainercreative$containercreative = (CreativeInv2.ContainerCreative)this.inventorySlots;
         guicontainercreative$containercreative.itemList.clear();

         CreativeTabs tab = CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex];
         if (tab.hasSearchBar() && tab != CreativeTabs.SEARCH)
         {
             tab.displayAllRelevantItems(guicontainercreative$containercreative.itemList);
             updateFilteredItems(guicontainercreative$containercreative);
             return;
         }

         for (Item item : Item.REGISTRY)
         {
             if (item != null && item.getCreativeTab() != null)
             {
                 item.getSubItems(item, (CreativeTabs)null, guicontainercreative$containercreative.itemList);
             }
         }
         updateFilteredItems(guicontainercreative$containercreative);
    }


    //split from above for custom search tabs
    //split from above for custom search tabs
    private void updateFilteredItems(CreativeInv2.ContainerCreative guicontainercreative$containercreative)
    {
    	if (CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex] == CreativeTabs.SEARCH) // FORGE: Only add enchanted books to the regular search
        for (Enchantment enchantment : Enchantment.REGISTRY)
        {
            if (enchantment != null && enchantment.type != null)
            {
                Items.ENCHANTED_BOOK.getAll(enchantment, guicontainercreative$containercreative.itemList);
            }
        }
        Iterator<ItemStack> iterator = guicontainercreative$containercreative.itemList.iterator();
        String s1 = this.searchField.getText().toLowerCase();

        while (iterator.hasNext())
        {
            ItemStack itemstack = (ItemStack)iterator.next();
            boolean flag = false;

            for (String s : itemstack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips))
            {
                if (TextFormatting.getTextWithoutFormattingCodes(s).toLowerCase().contains(s1))
                {
                    flag = true;
                    break;
                }
            }

            if (!flag)
            {
                iterator.remove();
            }
        }

        this.currentScroll = 0.0F;
        guicontainercreative$containercreative.scrollTo(0.0F);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
    	CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex];

        if (creativetabs != null && creativetabs.drawInForegroundOfTab())
        {
            GlStateManager.disableBlend();
            this.fontRendererObj.drawString(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]), 8, 6, 4210752);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (mouseButton == 0)
        {
            int i = mouseX - this.guiLeft;
            int j = mouseY - this.guiTop;

            for (CreativeTabs creativetabs : CreativeTabs.CREATIVE_TAB_ARRAY)
            {
                if (this.isMouseOverTab(creativetabs, i, j))
                {
                    return;
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        if (state == 0)
        {
            int i = mouseX - this.guiLeft;
            int j = mouseY - this.guiTop;

            for (CreativeTabs creativetabs : CreativeTabs.CREATIVE_TAB_ARRAY)
            {
                if (creativetabs != null && this.isMouseOverTab(creativetabs, i, j))
                {
                    this.setCurrentCreativeTab(creativetabs);
                    return;
                }
            }
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

    private boolean needsScrollBars()
    {
        if (CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex] == null) return false;
        return selectedTabIndex != CreativeTabs.INVENTORY.getTabIndex() && CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex].shouldHidePlayerInventory() && ((CreativeInv2.ContainerCreative)this.inventorySlots).canScroll();
    }

    private void setCurrentCreativeTab(CreativeTabs tab)
    {
        if (tab == null) return;
        int i = selectedTabIndex;
        selectedTabIndex = tab.getTabIndex();
        CreativeInv2.ContainerCreative guicontainercreative$containercreative = (CreativeInv2.ContainerCreative)this.inventorySlots;
        this.dragSplittingSlots.clear();
        guicontainercreative$containercreative.itemList.clear();
        tab.displayAllRelevantItems(guicontainercreative$containercreative.itemList);

        if (tab == CreativeTabs.INVENTORY)
        {
            Container container = this.mc.thePlayer.inventoryContainer;

            if (this.originalSlots == null)
            {
                this.originalSlots = guicontainercreative$containercreative.inventorySlots;
            }

            guicontainercreative$containercreative.inventorySlots = Lists.<Slot>newArrayList();

            for (int j = 0; j < container.inventorySlots.size(); ++j)
            {
                Slot slot = new CreativeInv2.CreativeSlot((Slot)container.inventorySlots.get(j), j);
                guicontainercreative$containercreative.inventorySlots.add(slot);

                if (j >= 5 && j < 9)
                {
                    int j1 = j - 5;
                    int k1 = j1 / 2;
                    int l1 = j1 % 2;
                    slot.xDisplayPosition = 54 + k1 * 54;
                    slot.yDisplayPosition = 6 + l1 * 27;
                }
                else if (j >= 0 && j < 5)
                {
                    slot.xDisplayPosition = -2000;
                    slot.yDisplayPosition = -2000;
                }
                else if (j == 45)
                {
                    slot.xDisplayPosition = 35;
                    slot.yDisplayPosition = 20;
                }
                else if (j < container.inventorySlots.size())
                {
                    int k = j - 9;
                    int l = k % 9;
                    int i1 = k / 9;
                    slot.xDisplayPosition = 9 + l * 18;

                    if (j >= 36)
                    {
                        slot.yDisplayPosition = 112;
                    }
                    else
                    {
                        slot.yDisplayPosition = 54 + i1 * 18;
                    }
                }
            }

            this.destroyItemSlot = new Slot(basicInventory, 0, 173, 112);
            guicontainercreative$containercreative.inventorySlots.add(this.destroyItemSlot);
        }
        else if (i == CreativeTabs.INVENTORY.getTabIndex())
        {
            guicontainercreative$containercreative.inventorySlots = this.originalSlots;
            this.originalSlots = null;
        }

        if (this.searchField != null)
        {
            if (tab.hasSearchBar())
            {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocused(true);
                this.searchField.setText("");
                this.searchField.width = tab.getSearchbarWidth();
                this.searchField.xPosition = this.guiLeft + (82 /*default left*/ + 89 /*default width*/) - this.searchField.width;
                this.updateCreativeSearch();
            }
            else
            {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused(false);
            }
        }

        this.currentScroll = 0.0F;
        guicontainercreative$containercreative.scrollTo(0.0F);
    }

    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0 && this.needsScrollBars())
        {
            int j = (((CreativeInv2.ContainerCreative)this.inventorySlots).itemList.size() + 9 - 1) / 9 - 5;

            if (i > 0)
            {
                i = 1;
            }

            if (i < 0)
            {
                i = -1;
            }

            this.currentScroll = (float)((double)this.currentScroll - (double)i / (double)j);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
            ((CreativeInv2.ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        boolean flag = Mouse.isButtonDown(0);
        int i = this.guiLeft;
        int j = this.guiTop;
        int k = i + 175;
        int l = j + 18;
        int i1 = k + 14;
        int j1 = l + 112;

        if (!this.wasClicking && flag && mouseX >= k && mouseY >= l && mouseX < i1 && mouseY < j1)
        {
            this.isScrolling = this.needsScrollBars();
        }

        if (!flag)
        {
            this.isScrolling = false;
        }

        this.wasClicking = flag;

        if (this.isScrolling)
        {
            this.currentScroll = ((float)(mouseY - l) - 7.5F) / ((float)(j1 - l) - 15.0F);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
            ((CreativeInv2.ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        int start = tabPage * 10;
        int end = Math.min(CreativeTabs.CREATIVE_TAB_ARRAY.length, ((tabPage + 1) * 10) + 2);
        if (tabPage != 0) start += 2;
        boolean rendered = false;

        for (CreativeTabs creativetabs : java.util.Arrays.copyOfRange(CreativeTabs.CREATIVE_TAB_ARRAY,start,end))
        {
            if (creativetabs == null) continue;
            if (this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY))
            {
                rendered = true;
                break;
            }
        }

        if (!rendered && renderCreativeInventoryHoveringText(CreativeTabs.SEARCH, mouseX, mouseY))
        {
            renderCreativeInventoryHoveringText(CreativeTabs.INVENTORY, mouseX, mouseY);
        }

        if (this.destroyItemSlot != null && selectedTabIndex == CreativeTabs.INVENTORY.getTabIndex() && this.isPointInRegion(this.destroyItemSlot.xDisplayPosition, this.destroyItemSlot.yDisplayPosition, 16, 16, mouseX, mouseY))
        {
            this.drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
        }

        if (maxPages != 0)
        {
            String page = String.format("%d / %d", tabPage + 1, maxPages + 1);
            int width = fontRendererObj.getStringWidth(page);
            GlStateManager.disableLighting();
            this.zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            fontRendererObj.drawString(page, guiLeft + (xSize / 2) - (width / 2), guiTop - 44, -1);
            this.zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
    }
    
    
    
    protected void renderToolTip(ItemStack stack, int x, int y)
    {
    	setCrafting(stack,x,y);
        if (selectedTabIndex == CreativeTabs.SEARCH.getTabIndex())
        {
            List<String> list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
            CreativeTabs creativetabs = stack.getItem().getCreativeTab();

            if (creativetabs == null && stack.getItem() == Items.ENCHANTED_BOOK)
            {
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

                if (map.size() == 1)
                {
                    Enchantment enchantment = (Enchantment)map.keySet().iterator().next();

                    for (CreativeTabs creativetabs1 : CreativeTabs.CREATIVE_TAB_ARRAY)
                    {
                        if (creativetabs1.hasRelevantEnchantmentType(enchantment.type))
                        {
                            creativetabs = creativetabs1;
                            break;
                        }
                    }
                }
            }

            if (creativetabs != null)
            {
                list.add(1, "" + TextFormatting.BOLD + TextFormatting.BLUE + I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]));
            }

            for (int i = 0; i < list.size(); ++i)
            {
                if (i == 0)
                {
                    list.set(i, stack.getRarity().rarityColor + (String)list.get(i));
                }
                else
                {
                    list.set(i, TextFormatting.GRAY + (String)list.get(i));
                }
            }

            this.drawHoveringText(list, x, y);
        }
        else
        {
            super.renderToolTip(stack, x, y);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
    	
    	
    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.enableGUIStandardItemLighting();
        CreativeTabs creativetabs = CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex];

        int start = tabPage * 10;
        int end = Math.min(CreativeTabs.CREATIVE_TAB_ARRAY.length, ((tabPage + 1) * 10 + 2));
        if (tabPage != 0) start += 2;

        for (CreativeTabs creativetabs1 : java.util.Arrays.copyOfRange(CreativeTabs.CREATIVE_TAB_ARRAY,start,end))
        {
            this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);

            if (creativetabs1 == null) continue;
            if (creativetabs1.getTabIndex() != selectedTabIndex)
            {
                this.drawTab(creativetabs1);
            }
        }

        if (tabPage != 0)
        {
            if (creativetabs != CreativeTabs.SEARCH)
            {
                this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
                drawTab(CreativeTabs.SEARCH);
            }
            if (creativetabs != CreativeTabs.INVENTORY)
            {
                this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
                drawTab(CreativeTabs.INVENTORY);
            }
        }

        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        
        
        //Crafting
        ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
        int b = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.guiLeft - 70, b, 20, 0, this.xSize-125, 80);
      //Crafting
        
        
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.guiLeft + 175;
        int j = this.guiTop + 18;
        int k = j + 112;
        this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);

        if (creativetabs.shouldHidePlayerInventory())
        {
            this.drawTexturedModalRect(i, j + (int)((float)(k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }

        if (creativetabs == null || creativetabs.getTabPage() != tabPage)
        {
            if (creativetabs != CreativeTabs.SEARCH && creativetabs != CreativeTabs.INVENTORY)
            {
                return;
            }
        }

        this.drawTab(creativetabs);

        if (creativetabs == CreativeTabs.INVENTORY)
        {
            GuiInventory.drawEntityOnScreen(this.guiLeft + 88, this.guiTop + 45, 20, (float)(this.guiLeft + 88 - mouseX), (float)(this.guiTop + 45 - 30 - mouseY), this.mc.thePlayer);
        }
    }

    /**
     * Checks if the mouse is over the given tab. Returns true if so.
     */
    protected boolean isMouseOverTab(CreativeTabs tab, int mouseX, int mouseY)
    {
        if (tab.getTabPage() != tabPage)
        {
            if (tab != CreativeTabs.SEARCH && tab != CreativeTabs.INVENTORY)
            {
                return false;
            }
        }

        int i = tab.getTabColumn();
        int j = 28 * i;
        int k = 0;

        if (i == 5)
        {
            j = this.xSize - 28 + 2;
        }
        else if (i > 0)
        {
            j += i;
        }

        if (tab.isTabInFirstRow())
        {
            k = k - 32;
        }
        else
        {
            k = k + this.ySize;
        }

        return mouseX >= j && mouseX <= j + 28 && mouseY >= k && mouseY <= k + 32;
    }

    /**
     * Renders the creative inventory hovering text if mouse is over it. Returns true if did render or false otherwise.
     * Params: current creative tab to be checked, current mouse x position, current mouse y position.
     */
    protected boolean renderCreativeInventoryHoveringText(CreativeTabs tab, int mouseX, int mouseY)
    {
        int i = tab.getTabColumn();
        int j = 28 * i;
        int k = 0;

        if (i == 5)
        {
            j = this.xSize - 28 + 2;
        }
        else if (i > 0)
        {
            j += i;
        }

        if (tab.isTabInFirstRow())
        {
            k = k - 32;
        }
        else
        {
            k = k + this.ySize;
        }

        if (this.isPointInRegion(j + 3, k + 3, 23, 27, mouseX, mouseY))
        {
            this.drawCreativeTabHoveringText(I18n.format(tab.getTranslatedTabLabel(), new Object[0]), mouseX, mouseY);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Draws the given tab and its background, deciding whether to highlight the tab or not based off of the selected
     * index.
     */
    protected void drawTab(CreativeTabs tab)
    {
        boolean flag = tab.getTabIndex() == selectedTabIndex;
        boolean flag1 = tab.isTabInFirstRow();
        int i = tab.getTabColumn();
        int j = i * 28;
        int k = 0;
        int l = this.guiLeft + 28 * i;
        int i1 = this.guiTop;
        int j1 = 32;

        if (flag)
        {
            k += 32;
        }

        if (i == 5)
        {
            l = this.guiLeft + this.xSize - 28;
        }
        else if (i > 0)
        {
            l += i;
        }

        if (flag1)
        {
            i1 = i1 - 28;
        }
        else
        {
            k += 64;
            i1 = i1 + (this.ySize - 4);
        }

        GlStateManager.disableLighting();
        GlStateManager.color(1F, 1F, 1F); //Forge: Reset color in case Items change it.
        GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        this.drawTexturedModalRect(l, i1, j, k, 28, j1);
        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;
        l = l + 6;
        i1 = i1 + 8 + (flag1 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        ItemStack itemstack = tab.getIconItemStack();
        this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i1);
        this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, l, i1);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
        }

        if (button.id == 1)
        {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
        }

        if (button.id == 101)
        {
            tabPage = Math.max(tabPage - 1, 0);
        }
        else if (button.id == 102)
        {
            tabPage = Math.min(tabPage + 1, maxPages);
        }
    }

    public int getSelectedTabIndex()
    {
        return selectedTabIndex;
    }

    @SideOnly(Side.CLIENT)
    static class ContainerCreative extends Container
        {
            public List itemList = Lists.newArrayList();
            public static int x = 0;
            public static int y = 0;
            public ContainerCreative(EntityPlayer player)
            {
                InventoryPlayer inventoryplayer = player.inventory;

                for (int i = 0; i < 5; ++i)
                {
                    for (int j = 0; j < 9; ++j)
                    {
                        this.addSlotToContainer(new Slot(CreativeInv2.basicInventory, i * 9 + j, 9 + j * 18, 18 + i * 18));
                    }
                }

                for (int k = 0; k < 9; ++k)
                {
                    this.addSlotToContainer(new Slot(inventoryplayer, k, 9 + k * 18, 112));
                }
                
                
             // Crafting
                //this.addSlotToContainer(new Slot(CreativeInv2.craftResult, 0, -200 + 124, 35));
                int l;
                int i1;
                
                for (l = 0; l < 3; ++l)
                {
                    for (i1 = 0; i1 < 3; ++i1)
                    {	

                    	
                        this.addSlotToContainer(new Slot(CreativeInv2.recipefield, i1 + l * 3,-90 + 30 + i1 * 18, 17 + l * 18));
                    }
                }
                // Crafting
                
                
                
                
                
                this.scrollTo(0.0F);
            }

            public boolean canInteractWith(EntityPlayer playerIn)
            {
                return true;
            }

            public void scrollTo(float p_148329_1_)
            {
                int i = (this.itemList.size() + 9 - 1) / 9 - 5;
                int j = (int)((double)(p_148329_1_ * (float)i) + 0.5D);

                if (j < 0)
                {
                    j = 0;
                }

                for (int k = 0; k < 5; ++k)
                {
                    for (int l = 0; l < 9; ++l)
                    {
                        int i1 = l + (k + j) * 9;

                        if (i1 >= 0 && i1 < this.itemList.size())
                        {
                            CreativeInv2.basicInventory.setInventorySlotContents(l + k * 9, (ItemStack)this.itemList.get(i1));
                        }
                        else
                        {
                        	CreativeInv2.basicInventory.setInventorySlotContents(l + k * 9, (ItemStack)null);
                        }
                    }
                }
            }

            public boolean canScroll()
            {
                return this.itemList.size() > 45;
            }

            /**
             * Retries slotClick() in case of failure
             */
            protected void retrySlotClick(int slotId, int clickedButton, boolean mode, EntityPlayer playerIn)
            {
            }

            @Nullable
            public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
            {
                if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size())
                {
                    Slot slot = (Slot)this.inventorySlots.get(index);

                    if (slot != null && slot.getHasStack())
                    {
                        slot.putStack((ItemStack)null);
                    }
                }

                return null;
            }

            /**
             * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack
             * passed in is null for the initial slot that was double-clicked.
             */
            public boolean canMergeSlot(ItemStack stack, Slot slotIn)
            {
                return slotIn.yDisplayPosition > 90;
            }

            /**
             * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to
             * check if the slot can be added to a list of Slots to split the held ItemStack across.
             */
            public boolean canDragIntoSlot(Slot slotIn)
            {
                return slotIn.inventory instanceof InventoryPlayer || slotIn.yDisplayPosition > 90 && slotIn.xDisplayPosition <= 162;
            }
        }

    @SideOnly(Side.CLIENT)
    class CreativeSlot extends Slot
    {
        private final Slot slot;

        public CreativeSlot(Slot p_i46313_2_, int p_i46313_3_)
        {
            super(p_i46313_2_.inventory, p_i46313_3_, 0, 0);
            this.slot = p_i46313_2_;
        }

        public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
        {
            this.slot.onPickupFromSlot(playerIn, stack);
        }

        /**
         * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
         */
        public boolean isItemValid(@Nullable ItemStack stack)
        {
            return this.slot.isItemValid(stack);
        }

        /**
         * Helper fnct to get the stack in the slot.
         */
        public ItemStack getStack()
        {
            return this.slot.getStack();
        }

        /**
         * Returns if this slot contains a stack.
         */
        public boolean getHasStack()
        {
            return this.slot.getHasStack();
        }

        /**
         * Helper method to put a stack in the slot.
         */
        public void putStack(@Nullable ItemStack stack)
        {
            this.slot.putStack(stack);
        }

        /**
         * Called when the stack in a Slot changes
         */
        public void onSlotChanged()
        {
            this.slot.onSlotChanged();
        }

        /**
         * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the
         * case of armor slots)
         */
        public int getSlotStackLimit()
        {
            return this.slot.getSlotStackLimit();
        }

        public int getItemStackLimit(ItemStack stack)
        {
            return this.slot.getItemStackLimit(stack);
        }

        @Nullable
        public String getSlotTexture()
        {
            return this.slot.getSlotTexture();
        }

        /**
         * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
         * stack.
         */
        public ItemStack decrStackSize(int amount)
        {
            return this.slot.decrStackSize(amount);
        }

        /**
         * returns true if the slot exists in the given inventory and location
         */
        public boolean isHere(IInventory inv, int slotIn)
        {
            return this.slot.isHere(inv, slotIn);
        }

        /**
         * Actualy only call when we want to render the white square effect over the slots. Return always True, except
         * for the armor slot of the Donkey/Mule (we can't interact with the Undead and Skeleton horses)
         */
        public boolean canBeHovered()
        {
            return this.slot.canBeHovered();
        }

        /**
         * Return whether this slot's stack can be taken from this slot.
         */
        public boolean canTakeStack(EntityPlayer playerIn)
        {
            return this.slot.canTakeStack(playerIn);
        }

        /**
         * if par2 has more items than par1, onCrafting(item,countIncrease) is called
         */
        public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_)
        {
            super.onSlotChange(p_75220_1_, p_75220_2_);
        }
    }
}