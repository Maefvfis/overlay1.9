package de.maefvfis.gameoverlay.objects;

import java.util.ArrayList;
import java.util.List;

import de.maefvfis.gameoverlay.objects.CraftingBenchManager.mouseAction;
import de.maefvfis.gameoverlay.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CraftingBenchManagerNeu {
	
	public ContainerWorkbench CraftingBench;
	public ItemStack[] recipes;
	public ItemStack resultStack;
	public Minecraft mc = Minecraft.getMinecraft();
	public List<ClickObj> clickList = new ArrayList();
	
	private long waitfrom = System.currentTimeMillis();
	private long waitfor = 50;
	
	private boolean running = false;
	private boolean wait = false;
	
	private boolean wait4items = false;
	private boolean wait4recipe = false;
	
	private final int benchSlotsMin = 1;
	private final int benchSlotsMax = 9;
	
	private final int invSlotsMin = 10;
	private final int invSlotsMax = 45;
	
	private boolean runningClick = false;
	private boolean runningInvSlots = false;
	
	private int akBenchSlot = benchSlotsMin;		
	private int akInvSlot = invSlotsMin; 	
	
	private int amount = 0;
	private ItemStack ItemStack = null;
	
	public CraftingBenchManagerNeu() {
		
	}
	
	public void init(ContainerWorkbench CraftingBench2, ItemStack[] recipes2, ItemStack resultStack2) {
		this.CraftingBench = CraftingBench2;
		this.recipes = recipes2;
		this.resultStack = resultStack2;
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void TickHandler (RenderGameOverlayEvent.Text event) {
		if(!running) return;
		LogHelper.info("Bench:" + String.valueOf(akBenchSlot));
		LogHelper.info("Inv:" + String.valueOf(akInvSlot));
		if(akBenchSlot > benchSlotsMax && !runningClick) {
			running = false;
			return;
		}
		//LogHelper.info("BenchSlotsNotMax");
		if(wait) {
			if(System.currentTimeMillis() >=  waitfrom + waitfor) { wait = false; }
			return;
		}
		//LogHelper.info("NotWaiting");
		
		if(runningClick) {
			if(clickList.size() == 0) { 
				runningClick = false;
				runningInvSlots = true;
				return;
			}
			Click(clickList.get(0).getKey(), clickList.get(0).getAction());
			clickList.remove(0);
			waitfromnow();

			return;
		}
		//LogHelper.info("NotRunningClick");
		
		if(!runningInvSlots) {
			akBenchSlot++;
			akInvSlot = invSlotsMin;
			runningClick = true;
			return;
		}
		
		//LogHelper.info("RunningInvSlots");
		
		if(akInvSlot <= invSlotsMax) {
			captureInvClicks();
		} else {
			LogHelper.info("Somesthing is wrong");
			akBenchSlot++;
			akInvSlot = invSlotsMin;
			runningClick = true;
			runningInvSlots = false;
		}
		
		//get	
	}
	
	public void craft() {
		if(this.resultStack == null) {
    		return;
    	}
		this.Click(0, mouseAction.shiftLeftClick);
		this.clearCraftingBench();
		
		amount = countMaxCrafting();
		runningClick = false;
		wait4items = false;
		wait4recipe = false;
		akBenchSlot = benchSlotsMin;
		akInvSlot = invSlotsMin;
		runningInvSlots = true;
		running = true;
	}
	
	public void clearCraftingBench() {
		LogHelper.info("Clearing Bench");
		for(int j = 0; j < 9; j++) {
			this.Click(j+1, mouseAction.shiftLeftClick);
		}
	}
	
	public void waitfromnow() {
		waitfrom = System.currentTimeMillis();
		wait = true;
	}
	
	public void captureInvClicks() {
		ItemStack = recipes[akBenchSlot-1];
		Slot inventorySlot = (Slot) this.CraftingBench.inventorySlots.get(akInvSlot);
		if(!inventorySlot.getHasStack()) {
			LogHelper.info("Nix Drin");
			akInvSlot++;
			return;
		}
		ItemStack invStack = inventorySlot.getStack().copy();
		if(isSameItem(invStack,ItemStack)) {

			if(invStack.stackSize == amount) {
				this.bufferClick(akInvSlot, mouseAction.leftClick);
				this.bufferClick(akBenchSlot, mouseAction.leftClick);
				runningInvSlots = false;
				LogHelper.info("1");
				return;
				
			}
			
			if(invStack.stackSize > amount) {
				int stackSize = invStack.stackSize;
				this.bufferClick(akInvSlot, mouseAction.leftClick);
				
				for(int c = stackSize; c > amount; c--) {
					this.bufferClick(akInvSlot, mouseAction.rightClick);
				}
				bufferClick(akBenchSlot, mouseAction.leftClick);
				runningInvSlots = false;
				LogHelper.info("2");
				return;
			}
			
			if(invStack.stackSize < amount) {
				int nextslot = this.getNextSlot(akInvSlot+1, ItemStack);
				this.bufferClick(nextslot, mouseAction.leftClick);
				this.bufferClick(akInvSlot, mouseAction.leftClick);
				this.bufferClick(nextslot, mouseAction.leftClick);
				LogHelper.info("3");
				runningClick = true;
				return;
			}
		} else {
			LogHelper.info("Falsches Item");
			akInvSlot++;
			return;
		}
	}
	
	public int countMaxCrafting() {
		List<ItemStack> ItemList = this.getUniqueRecipeItemlist();
		int returner = this.getSmallestStackSize(ItemList);
		for(ItemStack RecipeItem: ItemList) {
			int count = this.countItemsInInventory(RecipeItem) / this.countItemOcurenceInRecipe(RecipeItem);
			if(count < returner) {
				returner = count;
			} 
		}
		return returner;
	}
	
	public int getSmallestStackSize(List<ItemStack> ItemList) {
		int returner = 0;
		for(ItemStack ItemStack: ItemList) {
			if(returner == 0 || ItemStack.getMaxStackSize() < returner) {
				returner = ItemStack.getMaxStackSize();
			}
		}
		return returner;
	}
	
	public int countItemsInInventory(ItemStack check) {
		int Returner = 0;
		for(int i = 10; i < 46; i++) { 
			Slot inventorySlot = (Slot) this.CraftingBench.inventorySlots.get(i);
			
			if(!inventorySlot.getHasStack()) {
				continue;
			}
			
			if(isSameItem(inventorySlot.getStack(),check)) {
				Returner = Returner + inventorySlot.getStack().stackSize;
			}
		}
		return Returner;
	}
	
	public List<ItemStack> getUniqueRecipeItemlist() {
		List<ItemStack> Returner = new ArrayList();
		Returner.clear();
		for(int i = 0; i < 9; i++) {
			if(!Returner.contains(this.recipes[i]) && this.recipes[i] != null) {
				Returner.add(this.recipes[i]);
			}
		}
		return Returner;
	}
	
	public int countItemOcurenceInRecipe(ItemStack check) {
		int Returner = 0;
		for(int i = 0; i < 9; i++) {
			if(isSameItem(this.recipes[i],check)) {
				Returner++;
			}
		}
		return Returner;
	}
	
	public boolean isSameItem(ItemStack Stack1, ItemStack Stack2) {
		if(Stack1 == null || Stack2 == null) {
			return false;
		}
		if(Stack1.getItem() == Stack2.getItem() && Stack1.getItemDamage() == Stack2.getItemDamage()) {
			return true;
		}
		return false;
	}
	
	public void bufferClick(int Slot, int[] Action) {
		clickList.add(new ClickObj(Slot,Action));
		//mc.playerController.windowClick(this.CraftingBench.windowId, Slot, Action[0], Action[1], mc.thePlayer);
	}
	
	public void Click(int Slot, int[] Action) {
		//mc.playerController.windowClick(this.CraftingBench.windowId, Slot, Action[0], Action[1], mc.thePlayer);
		//TODO
	}

	public int getNextSlot(int c, ItemStack ItemStack) {
		for(int i = c; i < 46; i++) {
			Slot inventorySlot = (Slot) this.CraftingBench.inventorySlots.get(i);
			if(!inventorySlot.getHasStack()) {
				continue;
			}
			if(isSameItem(inventorySlot.getStack(),ItemStack)) {
				return i;
			}
		}
		return 0;
	}
}
