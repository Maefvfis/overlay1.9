package de.maefvfis.gameoverlay.client.handler;



import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
@SideOnly(Side.CLIENT)


public class SoundHandler {
	
	public Minecraft mc = Minecraft.getMinecraft();
	public int delay = 0;
	public int GridSize = 2;
	float hookX;
	float hookY;
	float hookZ;
	long randomTicks;
	int ticks;
	
	boolean anderleine = false;
	int randomTickDelayMin = 5;
	int randomTickDelayMax = 15;
	int randomTickDelay = 0;
	boolean gogogo = false;
	boolean wait4go = false;
	EntityFishHook Fishhook = new EntityFishHook(mc.theWorld);

	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onRenderChunkViewer(RenderGameOverlayEvent.Text event) {
		
		
		if(mc.gameSettings.showDebugInfo || !mc.inGameHasFocus) 
			return;
		
		if(ConfigurationHandler.myAngler.equalsIgnoreCase("OFF")) 
			return;
		
		ItemStack itemstack = mc.thePlayer.getHeldItemMainhand();

		if (itemstack == null || itemstack.getItem() != Items.FISHING_ROD)
			return;
		
		
		if(!isFishing() && ConfigurationHandler.myAngler.equalsIgnoreCase("Auto") && gogogo) {
			mc.playerController.processRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItemMainhand(), EnumHand.MAIN_HAND);
			gogogo = false;
		}
		else {
			if(anderleine && randomTickDelay < Fishhook.ticksExisted) {
				mc.playerController.processRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItemMainhand(), EnumHand.MAIN_HAND);
				anderleine = false;
				wait4go = true;
				randomTicks = System.currentTimeMillis() + ((ThreadLocalRandom.current().nextInt(randomTickDelayMin,randomTickDelayMax) / 10) * 1000);
			}
		}

		
		if(!wait4go)
			return;
		
		if(System.currentTimeMillis() >= randomTicks) {
			
			wait4go = false;
			gogogo = true;
		} else {
			ticks++;
		}

	}
	
	
	
	public boolean isFishing() {
		
		World world = mc.theWorld;
		int X = MathHelper.floor_double(mc.thePlayer.posX);
		int Z = MathHelper.floor_double(mc.thePlayer.posZ);
		Chunk chunk;
		
		for (int i1 = 0; i1 <= GridSize; i1++)
		{
			for (int i2 = 0; i2 <= GridSize; i2++)
			{
				chunk = mc.theWorld.getChunkFromBlockCoords(new BlockPos(X + this.XOffset(i1),0, Z + this.ZOffset(i2)));
				int result = 0;
				for (Object o : world.loadedEntityList) {
					if(!(o instanceof EntityFishHook)) continue;
					if(!is_aligned(chunk,((Entity) o).getPosition())) continue;
					if(((EntityFishHook) o).angler != mc.thePlayer) continue;
					Fishhook = ((EntityFishHook) o);
					return true;
				}
			}
		}
		return false;
	}
	
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onPlaySound(PlaySoundEvent event) {
		if (mc.gameSettings.showDebugInfo || !mc.inGameHasFocus)
			return;
		if(event.getName().equalsIgnoreCase("entity.bobber.splash") && isFishing()) {
			//mc.thePlayer.addChatComponentMessage(new TextComponentString("SPLISHSPLASH"));
			if(isintollerance(event.getSound().getXPosF(),(float)Fishhook.posX,2F) && isintollerance(event.getSound().getZPosF(),(float)Fishhook.posZ,2F) && isintollerance(event.getSound().getYPosF(),(float)Fishhook.posY,2F)) {
				mc.thePlayer.addChatComponentMessage(new TextComponentString("Lecker Fisch :)"));
				anderleine = true;
				randomTickDelay = Fishhook.ticksExisted + ThreadLocalRandom.current().nextInt(randomTickDelayMin,randomTickDelayMax);
				
			}
		}
	}
	
	public boolean isintollerance(float value1, float value2, float tollerance) {
		
		float diff = value1 - value2;
		if(diff < 0)
			diff = diff *(-1);

		if(diff <= tollerance)
			return true;

		return false;
	}
	
	public int ZOffset(int count) {
		if(count < GridSize / 2) return ((GridSize / 2) - count) * -16;
		if(count > GridSize / 2) return (count - (GridSize / 2)) * 16;
		return 0;
	}

	public int XOffset(int count) {
		if(count < GridSize / 2) return ((GridSize / 2) - count) * 16;
		if(count > GridSize / 2) return (count - (GridSize / 2)) * -16;
		return 0;
	}
	public boolean is_aligned(Chunk chunk, BlockPos entity) {
		ChunkPos ChunkPos = chunk.getChunkCoordIntPair();
		if(entity.getZ() >= ChunkPos.getZStart() && entity.getZ() <= ChunkPos.getZEnd()) {
			if(entity.getX() >= ChunkPos.getXStart() && entity.getX() <= ChunkPos.getXEnd()) {
				return true;
			}
		}
		return false;
	}
	

}