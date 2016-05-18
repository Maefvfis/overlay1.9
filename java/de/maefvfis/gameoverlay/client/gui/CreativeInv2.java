package de.maefvfis.gameoverlay.client.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private static final ResourceLocation creativeInventoryTabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private static InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);
    /** Currently selected creative inventory tab index. */
    private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
    /** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
    private float currentScroll;
    /** True if the scrollbar is being dragged */
    private boolean isScrolling;
    /** True if the left mouse button was held down last time drawScreen was called. */
    private boolean wasClicking;
    private GuiTextField searchField;
    private List<Slot> field_147063_B;
    private Slot field_147064_C;
    private boolean field_147057_D;
    private CreativeCrafting field_147059_E;
    private static int tabPage = 0;
    private int maxPages = 0;
    
    private static InventoryBasic recipefield = new InventoryBasic("recipefield", true, 9);
    private static IInventory craftResult = new InventoryCraftResult();
    
    
    public CreativeInv2(EntityPlayer p_i1088_1_)
    {
        super(new CreativeInv2.ContainerCreative(p_i1088_1_));
        p_i1088_1_.openContainer = this.inventorySlots;
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
	        this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[i]);
	        this.field_147059_E = new CreativeCrafting(this.mc);
	        this.mc.thePlayer.inventoryContainer.onCraftGuiOpened(this.field_147059_E);
	        int tabCount = CreativeTabs.creativeTabArray.length;
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
            this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_147059_E);
        }

        Keyboard.enableRepeatEvents(false);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (!CreativeTabs.creativeTabArray[selectedTabIndex].hasSearchBar())
        {
            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat))
            {
                this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
            }
            else
            {
                super.keyTyped(typedChar, keyCode);
            }
        }
        else
        {
            if (this.field_147057_D)
            {
                this.field_147057_D = false;
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

        CreativeTabs tab = CreativeTabs.creativeTabArray[selectedTabIndex];
        if (tab.hasSearchBar() && tab != CreativeTabs.tabAllSearch)
        {
            tab.displayAllRelevantItems(guicontainercreative$containercreative.itemList);
            updateFilteredItems(guicontainercreative$containercreative);
            return;
        }

        for (Item item : Item.itemRegistry)
        {
            if (item != null && item.getCreativeTab() != null)
            {
                item.getSubItems(item, (CreativeTabs)null, guicontainercreative$containercreative.itemList);
            }
        }
        updateFilteredItems(guicontainercreative$containercreative);
    }


    //split from above for custom search tabs
    private void updateFilteredItems(CreativeInv2.ContainerCreative guicontainercreative$containercreative)
    {
    	if (CreativeTabs.creativeTabArray[selectedTabIndex] == CreativeTabs.tabAllSearch) // FORGE: Only add enchanted books to the regular search
        for (Enchantment enchantment : Enchantment.enchantmentRegistry)
        {
            if (enchantment != null && enchantment.type != null)
            {
                Items.enchanted_book.getAll(enchantment, guicontainercreative$containercreative.itemList);
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
        CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];

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
            int l = mouseX - this.guiLeft;
            int i1 = mouseY - this.guiTop;
            CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
            int j1 = acreativetabs.length;

            for (int k1 = 0; k1 < j1; ++k1)
            {
                CreativeTabs creativetabs = acreativetabs[k1];

                if (this.func_147049_a(creativetabs, l, i1))
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
            int l = mouseX - this.guiLeft;
            int i1 = mouseY - this.guiTop;
            CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
            int j1 = acreativetabs.length;

            for (int k1 = 0; k1 < j1; ++k1)
            {
                CreativeTabs creativetabs = acreativetabs[k1];

                if (creativetabs != null && this.func_147049_a(creativetabs, l, i1))
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
        if (CreativeTabs.creativeTabArray[selectedTabIndex] == null) return false;
        return selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory() && ((CreativeInv2.ContainerCreative)this.inventorySlots).func_148328_e();
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

        if (tab == CreativeTabs.tabInventory)
        {
            Container container = this.mc.thePlayer.inventoryContainer;

            if (this.field_147063_B == null)
            {
                this.field_147063_B = guicontainercreative$containercreative.inventorySlots;
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

            this.field_147064_C = new Slot(field_147060_v, 0, 173, 112);
            guicontainercreative$containercreative.inventorySlots.add(this.field_147064_C);
        }
        else if (i == CreativeTabs.tabInventory.getTabIndex())
        {
            guicontainercreative$containercreative.inventorySlots = this.field_147063_B;
            this.field_147063_B = null;
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
            int j = ((CreativeInv2.ContainerCreative)this.inventorySlots).itemList.size() / 9 - 5 + 1;

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

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	
        boolean flag = Mouse.isButtonDown(0);
        int k = this.guiLeft;
        int l = this.guiTop;
        int i1 = k + 175;
        int j1 = l + 18;
        int k1 = i1 + 14;
        int l1 = j1 + 112;

        if (!this.wasClicking && flag && mouseX >= i1 && mouseY >= j1 && mouseX < k1 && mouseY < l1)
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
            this.currentScroll = ((float)(mouseY - j1) - 7.5F) / ((float)(l1 - j1) - 15.0F);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
            ((CreativeInv2.ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
        int start = tabPage * 10;
        int i2 = Math.min(acreativetabs.length, ((tabPage + 1) * 10) + 2);
        if (tabPage != 0) start += 2;
        boolean rendered = false;

        for (int j2 = start; j2 < i2; ++j2)
        {
            CreativeTabs creativetabs = acreativetabs[j2];

            if (creativetabs == null) continue;
            if (this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY))
            {
                rendered = true;
                break;
            }
        }

        if (!rendered && renderCreativeInventoryHoveringText(CreativeTabs.tabAllSearch, mouseX, mouseY))
        {
            renderCreativeInventoryHoveringText(CreativeTabs.tabInventory, mouseX, mouseY);
        }

        if (this.field_147064_C != null && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && this.isPointInRegion(this.field_147064_C.xDisplayPosition, this.field_147064_C.yDisplayPosition, 16, 16, mouseX, mouseY))
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
        if (selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex())
        {
            List<String> list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
            CreativeTabs creativetabs = stack.getItem().getCreativeTab();

            if (creativetabs == null && stack.getItem() == Items.enchanted_book)
            {
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

                if (map.size() == 1)
                {
                    Enchantment enchantment = (Enchantment)map.keySet().iterator().next();

                    for (CreativeTabs creativetabs1 : CreativeTabs.creativeTabArray)
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
        CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
        CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
        int k = acreativetabs.length;
        int l;

        int start = tabPage * 10;
        k = Math.min(acreativetabs.length, ((tabPage + 1) * 10 + 2));
        if (tabPage != 0) start += 2;

        for (l = start; l < k; ++l)
        {
            CreativeTabs creativetabs1 = acreativetabs[l];
            this.mc.getTextureManager().bindTexture(creativeInventoryTabs);

            if (creativetabs1 == null) continue;
            if (creativetabs1.getTabIndex() != selectedTabIndex)
            {
                this.func_147051_a(creativetabs1);
            }
        }

        if (tabPage != 0)
        {
            if (creativetabs != CreativeTabs.tabAllSearch)
            {
                this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
                func_147051_a(CreativeTabs.tabAllSearch);
            }
            if (creativetabs != CreativeTabs.tabInventory)
            {
                this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
                func_147051_a(CreativeTabs.tabInventory);
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
        int i1 = this.guiLeft + 175;
        k = this.guiTop + 18;
        l = k + 112;
        this.mc.getTextureManager().bindTexture(creativeInventoryTabs);

        if (creativetabs.shouldHidePlayerInventory())
        {
            this.drawTexturedModalRect(i1, k + (int)((float)(l - k - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }
        
        
        
        
        
        
        if (creativetabs == null || creativetabs.getTabPage() != tabPage)
        {
            if (creativetabs != CreativeTabs.tabAllSearch && creativetabs != CreativeTabs.tabInventory)
            {
                return;
            }
        }
        
        
        
        this.func_147051_a(creativetabs);

        if (creativetabs == CreativeTabs.tabInventory)
        {
            GuiInventory.drawEntityOnScreen(this.guiLeft + 43, this.guiTop + 45, 20, (float)(this.guiLeft + 43 - mouseX), (float)(this.guiTop + 45 - 30 - mouseY), this.mc.thePlayer);
        }
    }

    protected boolean func_147049_a(CreativeTabs p_147049_1_, int p_147049_2_, int p_147049_3_)
    {
        if (p_147049_1_.getTabPage() != tabPage)
        {
            if (p_147049_1_ != CreativeTabs.tabAllSearch &&
                p_147049_1_ != CreativeTabs.tabInventory)
            {
                return false;
            }
        }

        int k = p_147049_1_.getTabColumn();
        int l = 28 * k;
        byte b0 = 0;

        if (k == 5)
        {
            l = this.xSize - 28 + 2;
        }
        else if (k > 0)
        {
            l += k;
        }

        int i1;

        if (p_147049_1_.isTabInFirstRow())
        {
            i1 = b0 - 32;
        }
        else
        {
            i1 = b0 + this.ySize;
        }

        return p_147049_2_ >= l && p_147049_2_ <= l + 28 && p_147049_3_ >= i1 && p_147049_3_ <= i1 + 32;
    }

    protected boolean renderCreativeInventoryHoveringText(CreativeTabs p_147052_1_, int p_147052_2_, int p_147052_3_)
    {
        int k = p_147052_1_.getTabColumn();
        int l = 28 * k;
        byte b0 = 0;

        if (k == 5)
        {
            l = this.xSize - 28 + 2;
        }
        else if (k > 0)
        {
            l += k;
        }

        int i1;

        if (p_147052_1_.isTabInFirstRow())
        {
            i1 = b0 - 32;
        }
        else
        {
            i1 = b0 + this.ySize;
        }

        if (this.isPointInRegion(l + 3, i1 + 3, 23, 27, p_147052_2_, p_147052_3_))
        {
            this.drawCreativeTabHoveringText(I18n.format(p_147052_1_.getTranslatedTabLabel(), new Object[0]), p_147052_2_, p_147052_3_);
            return true;
        }
        else
        {
            return false;
        }
    }

    protected void func_147051_a(CreativeTabs p_147051_1_)
    {
        boolean flag = p_147051_1_.getTabIndex() == selectedTabIndex;
        boolean flag1 = p_147051_1_.isTabInFirstRow();
        int i = p_147051_1_.getTabColumn();
        int j = i * 28;
        int k = 0;
        int l = this.guiLeft + 28 * i;
        int i1 = this.guiTop;
        byte b0 = 32;

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
            i1 -= 28;
        }
        else
        {
            k += 64;
            i1 += this.ySize - 4;
        }

        GlStateManager.disableLighting();
        GlStateManager.color(1F, 1F, 1F); //Forge: Reset color in case Items change it.
        GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        this.drawTexturedModalRect(l, i1, j, k, 28, b0);
        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;
        l += 6;
        i1 += 8 + (flag1 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        ItemStack itemstack = p_147051_1_.getIconItemStack();
        this.itemRender.renderItemAndEffectIntoGUI(itemstack, l, i1);
        this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, l, i1);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

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
            private static final String __OBFID = "CL_00000753";
            public static int x = 0;
            public static int y = 0;
            public ContainerCreative(EntityPlayer p_i1086_1_)
            {
                InventoryPlayer inventoryplayer = p_i1086_1_.inventory;
                int i;

                for (i = 0; i < 5; ++i)
                {
                    for (int j = 0; j < 9; ++j)
                    {
                        this.addSlotToContainer(new Slot(CreativeInv2.field_147060_v, i * 9 + j, 9 + j * 18 , 18 + i * 18 ));
                    }
                }

                for (i = 0; i < 9; ++i)
                {
                    this.addSlotToContainer(new Slot(inventoryplayer, i, 9 + i * 18, 112));
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
                int i = (this.itemList.size() + 8) / 9 - 5;
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
                        	CreativeInv2.field_147060_v.setInventorySlotContents(l + k * 9, (ItemStack)this.itemList.get(i1));
                        }
                        else
                        {
                        	CreativeInv2.field_147060_v.setInventorySlotContents(l + k * 9, (ItemStack)null);
                        }
                    }
                }
            }

            public boolean func_148328_e()
            {
                return this.itemList.size() > 45;
            }

            protected void retrySlotClick(int p_75133_1_, int p_75133_2_, boolean p_75133_3_, EntityPlayer p_75133_4_) {}

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

            public boolean canMergeSlot(ItemStack p_94530_1_, Slot p_94530_2_)
            {
                return p_94530_2_.yDisplayPosition > 90;
            }

            public boolean canDragIntoSlot(Slot p_94531_1_)
            {
                return p_94531_1_.inventory instanceof InventoryPlayer || p_94531_1_.yDisplayPosition > 90 && p_94531_1_.xDisplayPosition <= 162;
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
        public boolean isItemValid(ItemStack stack)
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
        public void putStack(ItemStack stack)
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