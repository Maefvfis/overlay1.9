package de.maefvfis.gameoverlay.client.renderer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import de.maefvfis.gameoverlay.client.renderer.vector.Vector4i;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;



public class Events {
	public static final List<Vector4i> SPAWN_LIST = new ArrayList<Vector4i>();

	private final Minecraft minecraft = Minecraft.getMinecraft();
	private final Frustum frustrum = new Frustum();
	private final AxisAlignedBB boundingBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	private int ticks = -1;

	@SubscribeEvent
	public void tick(ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			onTick();
		}
	}


	public boolean onTick() {
		this.minecraft.mcProfiler.startSection("msh");

		if (--this.ticks < 0) {
			this.ticks = ConfigurationHandler.updateRate;

			if (this.minecraft.theWorld != null && ConfigurationHandler.renderSpawns) {
				SPAWN_LIST.clear();

				this.frustrum.setPosition(PlayerPosition.x, PlayerPosition.y, PlayerPosition.z);

				World world = this.minecraft.theWorld;

				int lowX, lowY, lowZ, highX, highY, highZ, x, y, z;

				lowX = (int) (Math.floor(PlayerPosition.x) - ConfigurationHandler.renderRangeXZ);
				highX = (int) (Math.floor(PlayerPosition.x) + ConfigurationHandler.renderRangeXZ);
				lowY = (int) (Math.floor(PlayerPosition.y) - ConfigurationHandler.renderRangeYBellow);
				highY = (int) (Math.floor(PlayerPosition.y) + ConfigurationHandler.renderRangeYAbove);
				lowZ = (int) (Math.floor(PlayerPosition.z) - ConfigurationHandler.renderRangeXZ);
				highZ = (int) (Math.floor(PlayerPosition.z) + ConfigurationHandler.renderRangeXZ);

				for (y = lowY; y <= highY; y++) {
					for (x = lowX; x <= highX; x++) {
						for (z = lowZ; z <= highZ; z++) {
							if (!this.frustrum.isBoundingBoxInFrustum(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1))) {
								continue;
							}

							if (getCanSpawnHere(world, x, y, z)) {
								SPAWN_LIST.add(new Vector4i(x, y, z, 1));
							}
						}
					}
				}
			}
		}

		this.minecraft.mcProfiler.endSection();

		return true;
	}


	private boolean getCanSpawnHere(World world, int x, int y, int z) {
		Chunk chunk = Minecraft.getMinecraft().theWorld.getChunkFromBlockCoords(new BlockPos(x, 0, z));
		if(!chunk.isLoaded()) { return false; }
		if(chunk.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(x & 15, y+1, z & 15)) >= 8) { return false; }
		
		BlockPos pos = new BlockPos(x, y - 1, z);
		IBlockState state = chunk.getBlockState(new BlockPos(x, y - 1, z));
		if(state != null) {
			Block block = chunk.getBlockState(x, y - 1, z).getBlock();
			if (block == null || block == Blocks.AIR || state.getMaterial().isLiquid() || !block.canCreatureSpawn(state,world,pos,SpawnPlacementType.ON_GROUND)) {
				return false;
			}
		}
		return true;
	}

}
