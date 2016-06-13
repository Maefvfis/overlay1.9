package de.maefvfis.gameoverlay.client.gui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.EnumHand;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import de.maefvfis.gameoverlay.client.renderer.HeadRenderer;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.objects.Output;
import de.maefvfis.gameoverlay.reference.EntityGridOptions;

public class ChunckViewer {
	
	private List whitelist = Arrays.asList(ConfigurationHandler.PlayerGridWhitelist.split(","));
	public WorldClient world = Minecraft.getMinecraft().theWorld;
	
	private static List<choords> cachedList = new ArrayList<choords>();
	private static int tick = 0;
	private static int triggerTick = 50;
	
	
	
	
	public int countEntity(Class<?> instance, Chunk chunk, Boolean IsWither) {
		int result = 0;
		for (Object o : world.loadedEntityList) {
			if(!instance.isInstance(o)) continue;
			if(!is_aligned(chunk,((Entity) o).getPosition())) continue;
			if(IsWither && !(((EntitySkeleton) o).getSkeletonType() == 1)) continue;
			if(o instanceof EntityPlayer && whitelist.contains(((EntityPlayer) o).getName())) continue;
			result++;
		}
		return result;
	}
	
	public int countEntity(Class<?> instance, Chunk chunk) {
		return countEntity(instance,chunk,false);
	}

	public List<choords> ListEntitysOnChunkChoords(Class<?> instance, Chunk chunk) {
		return ListEntitysOnChunkChoords(instance,chunk,false,false);
	}
	public List<choords> ListEntitysOnChunkChoords(Class<?> instance, Chunk chunk, Boolean IsWither, Boolean IsTile) {
		if(IsTile) return ListTileEntitysOnChunkChoords(instance, chunk, IsWither);
		return ListEntitysOnChunkChoords(instance, chunk, IsWither);
	}
	
	public List<choords> ListTileEntitysOnChunkChoords(Class<?> instance, Chunk chunk, Boolean IsWither) {
		List<choords> result = new ArrayList<choords>();
		
		HashMap chunkTileEntityMap = (HashMap) chunk.getTileEntityMap();
		Iterator iterator = chunkTileEntityMap.values().iterator();

        while (iterator.hasNext())
        {
            TileEntity tileentity = (TileEntity)iterator.next();
            if (instance.isInstance(tileentity)) {
            	if(tileentity instanceof TileEntityChest) {
            		if(!(((TileEntityChest) tileentity).getChestType() == BlockChest.Type.TRAP) && IsWither) continue;
            		if((((TileEntityChest) tileentity).getChestType() == BlockChest.Type.TRAP) && !IsWither) continue;
            		TileEntityChest chest = (TileEntityChest) tileentity;
                	result.add(new choords(chest.getPos().getX(),chest.getPos().getZ(),chest.getPos().getY(),null,false));
            	} else {
            		TileEntityMobSpawner Spawner = (TileEntityMobSpawner)tileentity;
            		String SpawnerName = Spawner.getSpawnerBaseLogic().getCachedEntity().getName();
            		ResourceLocation l = null;
            		
            		if (SpawnerName.equals(I18n.translateToLocal("entity.Skeleton.name"))) {
            			l = HeadRenderer.skelett;
            		} else if (SpawnerName.equals(I18n.translateToLocal("entity.CaveSpider.name"))) {
            			l = HeadRenderer.headMap.get(net.minecraft.entity.monster.EntityCaveSpider.class);
            		} else if (SpawnerName.equals(I18n.translateToLocal("entity.Spider.name"))) {
            			l = HeadRenderer.headMap.get(net.minecraft.entity.monster.EntitySpider.class);      			
            		} else if (SpawnerName.equals(I18n.translateToLocal("entity.Zombie.name"))) {
						l = HeadRenderer.headMap.get(net.minecraft.entity.monster.EntityZombie.class);   
            		} else if (SpawnerName.equals(I18n.translateToLocal("entity.Blaze.name"))) {
						l = HeadRenderer.headMap.get(net.minecraft.entity.monster.EntityBlaze.class);   
            		} else if (SpawnerName.equals(I18n.translateToLocal("entity.Silverfish.name"))) {
						l = HeadRenderer.headMap.get(net.minecraft.entity.monster.EntitySilverfish.class);  
					} else {
						l = HeadRenderer.unknown;
					}

            		result.add(new choords(Spawner.getPos().getX(),Spawner.getPos().getZ(),Spawner.getPos().getY(),l,false));
            	}
            }
        }
		return result;
	}
	
	public List<choords> ListLivingEntitys(Chunk chunk) {
		if(tick < triggerTick) { tick++; return cachedList; }
		tick = 0;
		
		List<choords> result = new ArrayList<choords>();
		for (Object o : world.loadedEntityList) {
			if(!(o instanceof EntityLiving) && !(o instanceof EntityPlayer)) continue;
			if(o instanceof EntityPlayer && !whitelist.contains(((EntityPlayer) o).getName()) && Minecraft.getMinecraft().thePlayer.getName() != ((EntityPlayer) o).getName()) {
				result.add(processPlayer(((EntityPlayer) o)));
				continue;
			}
			if(HeadRenderer.headMap.containsKey(o.getClass())) {
				result.add( new choords(((Entity)o).posX,((Entity)o).posZ,((Entity)o).posY,HeadRenderer.headMap.get(o.getClass()),false));
				continue;
			}
			if(o instanceof EntitySkeleton) {
				if(((EntitySkeleton)o).getSkeletonType() == 1 && ConfigurationHandler.EntityWitherSkelett) {
					result.add( new choords(((Entity)o).posX,((Entity)o).posZ,((Entity)o).posY,HeadRenderer.wither_skelett,false));
				} else if(ConfigurationHandler.EntitySkelett) {
					result.add( new choords(((Entity)o).posX,((Entity)o).posZ,((Entity)o).posY,HeadRenderer.skelett,false));
				}
				continue;
			}
		}
		cachedList = result;
		return result;
	}
	
	public choords processPlayer(EntityPlayer p) {
		ResourceLocation resourcelocation = new ResourceLocation("textures/entity/steve.png");
		Minecraft minecraft = Minecraft.getMinecraft();
		Map map = minecraft.getSkinManager().loadSkinFromCache(p.getGameProfile());
		if (map.containsKey(Type.SKIN)) {
			resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
		}
		return new choords(p.posX,p.posZ,p.posY,resourcelocation,true);
	}
	
	
	public List<choords> ListEntitysOnChunkChoords(Class<?> instance, Chunk chunk, Boolean IsWither) {
		
		List<choords> result = new ArrayList<choords>();
		
		for (Object o : world.loadedEntityList) {
			if (instance.isInstance(o) && is_aligned(chunk,((Entity) o).getPosition())) {
				if(IsWither && instance.isInstance(o)) {
					if(((EntitySkeleton) o).getSkeletonType() == 1) {
						result.add(new choords(((EntitySkeleton) o).posX,((EntitySkeleton) o).posZ,((EntitySkeleton) o).posY,null,false));
					}
				} else if(o instanceof EntityPlayer) {
					if(!whitelist.contains(((EntityPlayer) o).getName()) && Minecraft.getMinecraft().thePlayer.getName() != ((EntityPlayer) o).getName()) {
						// Add ResourceLocation to PlayerTextures.List
						ResourceLocation resourcelocation = null;
						Minecraft minecraft = Minecraft.getMinecraft();
						Map map = minecraft.getSkinManager().loadSkinFromCache(((EntityPlayer) o).getGameProfile());
						if (map.containsKey(Type.SKIN))
	                    {
							resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
	                    }
						result.add(new choords(((EntityPlayer) o).posX,((EntityPlayer) o).posZ,((EntityPlayer) o).posY,resourcelocation,true));
					}
				} else if(o instanceof EntityArmorStand) {
					if(((EntityArmorStand) o).getHeldItem(EnumHand.MAIN_HAND) != null && ((EntityArmorStand) o).getHeldItem(EnumHand.OFF_HAND) != null) {
						if(((EntityArmorStand) o).getHeldItem(EnumHand.MAIN_HAND).getItem() == Item.getItemById(383) || ((EntityArmorStand) o).getHeldItem(EnumHand.OFF_HAND).getItem() == Item.getItemById(383)){
							result.add(new choords(((Entity) o).posX,((Entity) o).posZ,((Entity) o).posY,null,false));
						}
					}
				} else if(o instanceof EntityItem) {
					if(EntityGridOptions.IsActiveEntity(EntityGridOptions.Crates)) {
						if(((EntityItem)o).getEntityItem().getDisplayName().contains("Crate5")) {
							result.add(new choords(((Entity) o).posX,((Entity) o).posZ,((Entity) o).posY,null,false));
						}
					} else {
						result.add(new choords(((Entity) o).posX,((Entity) o).posZ,((Entity) o).posY,null,false));
					}
					
				} else {
					result.add(new choords(((Entity) o).posX,((Entity) o).posZ,((Entity) o).posY,null,false));
				}
			}
		}
		return result;
	}

	
	public class choords {
		private final ResourceLocation Steve = new ResourceLocation("textures/entity/steve.png");
		public double x;
		public double z;
		public double y;
		public ResourceLocation resourcelocation;
		public boolean isPlayer;
		
		public int intX;
		public int intY;
		
		public choords(double x1, double z1, double y1, ResourceLocation resourcelocation2,boolean isPlayer) {
			this.x = x1;
			this.z = z1;
			this.y = y1;
			this.intX = (int) x1;
			this.intY = (int) y1;
			this.resourcelocation = resourcelocation2;
			this.isPlayer = isPlayer;
		}
		
		
		
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
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	public float extractprice(String string) {
		string = string.replaceAll("B", "");
		string = string.replaceAll("S", "");
		string = string.replaceAll(" ", "");
		return Float.valueOf(string);
	}

	public boolean isShopSign(TileEntitySign Sign) {
		
		if(Sign.signText[0] == null || Sign.signText[1] == null || Sign.signText[2] == null || Sign.signText[3] == null) return false;
		if(Sign.signText[0].getUnformattedText().replaceAll(" ", "").equalsIgnoreCase("")) return false;
		if(Sign.signText[3].getUnformattedText().replaceAll(" ", "").equalsIgnoreCase("")) return false;
		if(!isInteger(Sign.signText[1].getUnformattedText())) return false;
		String preiszeile = Sign.signText[2].getUnformattedText();
		
		if(preiszeile.contains(":")) {
			String[]preise = preiszeile.split(":");
			if(!istPreis(preise[0]) || !istPreis(preise[1])) return false;
		} else {
			if(!istPreis(preiszeile)) return false;
		}
		return true;
	}
	
	public boolean istPreis(String string) {
		if(string.matches("B ([0-9]+).([0-9]+)|S ([0-9]+).([0-9]+)|B ([0-9]+)|S ([0-9]+)|([0-9]+).([0-9]+) B|([0-9]+).([0-9]+) S|([0-9]+) S|([0-9]+) B")) {
			return true;
		}
		return false;
	}
}
