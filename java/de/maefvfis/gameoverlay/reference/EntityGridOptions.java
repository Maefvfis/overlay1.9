package de.maefvfis.gameoverlay.reference;

import java.util.ArrayList;
import java.util.List;

import de.maefvfis.gameoverlay.client.kadconmap.KadconMapHolder;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.objects.ChunkImage;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class EntityGridOptions {
	
	public String blubb = "";
	
	public static final int Mobs = 0; 
	public static final int Spawner = 1; 
	public static final int Crates = 2; 
	public static final int Items = 3;
	public static final int RedstoneKisten = 4;
	public static final int Kisten = 5;
	
	public static final String[] MobStrings = new String[] { "Mobs","Spawner","Drop Crates","EntityItem","RedstoneKiste","Kiste" };
	public static final Class<?>[] MobClasses = new Class<?>[] {  EntityMob.class, TileEntityMobSpawner.class,EntityItem.class, EntityItem.class, TileEntityChest.class, TileEntityChest.class  };
	public static final boolean [] MobIsWither = new boolean[] { false, false, false, false, true, false };
	public static final boolean [] IsTile = new boolean[] { false, true, false, false, true, true };
	
	
	public static final int[] MapModes = new int[]{0,1,2,3};
	
	//public static final String[] MobStrings = new String[] { "Mobs", "Animals", "Slimes", "Players", "Witherskeletts","Squid","Spawner","Cookie Stand","Drop Crates","EntityItem","RedstoneKiste","Kiste" };
	//public static final Class<?>[] MobClasses = new Class<?>[] {  EntityMob.class, EntityAnimal.class, EntitySlime.class, EntityPlayer.class, EntitySkeleton.class, EntitySquid.class, TileEntityMobSpawner.class, EntityArmorStand.class, EntityItem.class, EntityItem.class, TileEntityChest.class, TileEntityChest.class  };
	//public static final boolean [] MobIsWither = new boolean[] { false, false, false, false, true, false, false, false, false, false, true, false };
	//public static final boolean [] IsTile = new boolean[] { false, false, false, false, false, false, true, false, false, false, true, true };
	
	public static List<Entity> EnitysObj = new ArrayList<Entity>();
	public static Entity[] EnitysObj2;
	public static Entity ActiveEntity;
	
	public EntityGridOptions() {
		for (int i = 0; i < MobStrings.length; i++) {
			EnitysObj.add(new Entity(MobStrings[i],MobClasses[i],MobIsWither[i],IsTile[i]));
		}
		SetActiveEntity((String)ConfigurationHandler.myConfigGridType);
	}
	
	public static void init() {
		for (int i = 0; i < MobStrings.length; i++) {
			EnitysObj.add(new Entity(MobStrings[i],MobClasses[i],MobIsWither[i],IsTile[i]));
		}
		SetActiveEntity((String)ConfigurationHandler.myConfigGridType);
	}
	
	public static void SetActiveEntity(String MobString) {
		for (Entity ent: EnitysObj) {
			if(ent.EntityName.equals(MobString)) {
				ActiveEntity = ent;
			}
		}
	}
	
	public static boolean IsActiveEntity(int i) {
		return (getActiveEntityIndex() == i);
	}
	
	
	public static void CycleActiveEntity() {
    			ChunkImage.deleteBuffer();
				int i = getActiveEntityIndex();
				if(i == EnitysObj.size()-1) {
					SetActiveEntity(EnitysObj.get(0).EntityName);
					ConfigurationHandler.myConfigGridType = EnitysObj.get(0).EntityName;
				} else {
					SetActiveEntity(EnitysObj.get(i+1).EntityName);
					ConfigurationHandler.myConfigGridType = EnitysObj.get(i+1).EntityName;
				}

				return;
	}
	
	
	public static void CycleMapModes() {
		KadconMapHolder.reset(true);
		ChunkImage.deleteBuffer();
		int i = ConfigurationHandler.myConfigMapType;
		if(i == MapModes.length -1) {
			ConfigurationHandler.myConfigMapType = MapModes[0];
		} else {
			ConfigurationHandler.myConfigMapType = MapModes[i+1];
		}
		return;
	}
	
	public static void CycleBackActiveEntity() {
		ChunkImage.deleteBuffer();
		int i = getActiveEntityIndex();
		if(i == 0) {
			SetActiveEntity(EnitysObj.get(EnitysObj.size()-1).EntityName);
			ConfigurationHandler.myConfigGridType = EnitysObj.get(0).EntityName;
		} else {
			SetActiveEntity(EnitysObj.get(i-1).EntityName);
			ConfigurationHandler.myConfigGridType = EnitysObj.get(i-1).EntityName;
		}

		return;

}
	
	public static int getActiveEntityIndex() {
			for(int i = 0; i < EnitysObj.size(); i++) {
				if(EnitysObj.get(i).EntityName == ActiveEntity.EntityName) {
					return i;
				}
			}
			return 0;
		
	}
	
	public static void SetActiveEntity(Class<?> MobClass) {
		for (Entity ent: EnitysObj) {
			if(ent.EntityClass == MobClass) {
				ActiveEntity = ent;
			}
		}
	}

	public static class Entity {
		public Class<?> EntityClass;
		public String EntityName;
		public boolean WitherSkelett = false;
		public boolean IsTile = false;
		
		Entity(String setEntityName, Class<?> setEntityClass) {
			EntityName = setEntityName;
			EntityClass = setEntityClass;
		}
		Entity(String setEntityName, Class<?> setEntityClass, boolean Wither) {
			EntityName = setEntityName;
			EntityClass = setEntityClass;
			WitherSkelett = Wither;
		}
		Entity(String setEntityName, Class<?> setEntityClass, boolean Wither, boolean isTile2) {
			EntityName = setEntityName;
			EntityClass = setEntityClass;
			WitherSkelett = Wither;
			IsTile = isTile2;
		}
	}
}
