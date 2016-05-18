package de.maefvfis.gameoverlay.client.renderer;

import java.util.HashMap;
import java.util.Map;

import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import net.minecraft.util.ResourceLocation;

public class HeadRenderer {
    
	public static ResourceLocation skelett = new ResourceLocation("gameoverlay","textures/heads/skelett.png");
	public static ResourceLocation wither_skelett = new ResourceLocation("gameoverlay","textures/heads/witherskull.png");
	public static ResourceLocation unknown = new ResourceLocation("gameoverlay","textures/heads/unknown.png");
	public static Map<Class<?>,ResourceLocation> headMap = generateHeadMap();
	
    public static Map<Class<?>,ResourceLocation> generateHeadMap() {
    	
    	Map<Class<?>,ResourceLocation> ret = new HashMap<Class<?>,ResourceLocation>();
    	if(ConfigurationHandler.EntityBlaze) {
    		ret.put(net.minecraft.entity.monster.EntityBlaze.class,new ResourceLocation("gameoverlay","textures/heads/blaze.png"));
    	}
    	if(ConfigurationHandler.EntityCaveSpider) {
    		ret.put(net.minecraft.entity.monster.EntityCaveSpider.class,new ResourceLocation("gameoverlay","textures/heads/cave_spider.png"));
    	}
    	if(ConfigurationHandler.EntityChicken) {
    		ret.put(net.minecraft.entity.passive.EntityChicken.class,new ResourceLocation("gameoverlay","textures/heads/chicken.png"));
    	}
    	if(ConfigurationHandler.EntityCow) {
    		ret.put(net.minecraft.entity.passive.EntityCow.class,new ResourceLocation("gameoverlay","textures/heads/cow.png"));
    	}
    	if(ConfigurationHandler.EntityCreeper) {
    		ret.put(net.minecraft.entity.monster.EntityCreeper.class,new ResourceLocation("gameoverlay","textures/heads/creeper.png"));
    	}
    	if(ConfigurationHandler.EntityEnderman) {
    		ret.put(net.minecraft.entity.monster.EntityEnderman.class,new ResourceLocation("gameoverlay","textures/heads/enderman.png"));
    	}
    	if(ConfigurationHandler.EntityEndermite) {
    		ret.put(net.minecraft.entity.monster.EntityEndermite.class,new ResourceLocation("gameoverlay","textures/heads/endermite.png"));
    	}
    	if(ConfigurationHandler.EntityGhast) {
    		ret.put(net.minecraft.entity.monster.EntityGhast.class,new ResourceLocation("gameoverlay","textures/heads/ghast.png"));
    	}
    	if(ConfigurationHandler.EntityGuardian) {
    		ret.put(net.minecraft.entity.monster.EntityGuardian.class,new ResourceLocation("gameoverlay","textures/heads/guardian.png"));
    	}
    	if(ConfigurationHandler.EntityIronGolem) {
    		ret.put(net.minecraft.entity.monster.EntityIronGolem.class,new ResourceLocation("gameoverlay","textures/heads/iron_golem.png"));
    	}
    	if(ConfigurationHandler.EntityMagmaCube) {
    		ret.put(net.minecraft.entity.monster.EntityMagmaCube.class,new ResourceLocation("gameoverlay","textures/heads/magmacube.png"));
    	}
    	if(ConfigurationHandler.EntityMooshroom) {
    		ret.put(net.minecraft.entity.passive.EntityMooshroom.class,new ResourceLocation("gameoverlay","textures/heads/mushroom.png"));
    	}
    	if(ConfigurationHandler.EntityOcelot) {
    		ret.put(net.minecraft.entity.passive.EntityOcelot.class,new ResourceLocation("gameoverlay","textures/heads/cat.png"));
    	}
    	if(ConfigurationHandler.EntityPig) {
    		ret.put(net.minecraft.entity.passive.EntityPig.class,new ResourceLocation("gameoverlay","textures/heads/pig.png"));
    	}
    	if(ConfigurationHandler.EntityPigZombie) {
    		ret.put(net.minecraft.entity.monster.EntityPigZombie.class,new ResourceLocation("gameoverlay","textures/heads/zombipigman.png"));
    	}
    	if(ConfigurationHandler.EntityHorse) {
    		ret.put(net.minecraft.entity.passive.EntityHorse.class,new ResourceLocation("gameoverlay","textures/heads/horse.png"));
    	}
    	if(ConfigurationHandler.EntityRabbit) {
    		ret.put(net.minecraft.entity.passive.EntityRabbit.class,new ResourceLocation("gameoverlay","textures/heads/rabbit.png"));
    	}
    	if(ConfigurationHandler.EntitySheep) {
    		ret.put(net.minecraft.entity.passive.EntitySheep.class,new ResourceLocation("gameoverlay","textures/heads/sheep.png"));
    	}
    	if(ConfigurationHandler.EntityShulker) {
    		ret.put(net.minecraft.entity.monster.EntityShulker.class,new ResourceLocation("gameoverlay","textures/heads/shulker.png"));
    	}
    	if(ConfigurationHandler.EntitySilverfish) {
    		ret.put(net.minecraft.entity.monster.EntitySilverfish.class,new ResourceLocation("gameoverlay","textures/heads/silverfish.png"));
    	}
    	if(ConfigurationHandler.EntitySlime) {
    		ret.put(net.minecraft.entity.monster.EntitySlime.class,new ResourceLocation("gameoverlay","textures/heads/slime.png"));
    	}
    	if(ConfigurationHandler.EntitySnowman) {
    		ret.put(net.minecraft.entity.monster.EntitySnowman.class,new ResourceLocation("gameoverlay","textures/heads/snowman.png"));
    	}
    	if(ConfigurationHandler.EntitySpider) {
    		ret.put(net.minecraft.entity.monster.EntitySpider.class,new ResourceLocation("gameoverlay","textures/heads/spider.png"));
    	}
    	if(ConfigurationHandler.EntitySquid) {
    		ret.put(net.minecraft.entity.passive.EntitySquid.class,new ResourceLocation("gameoverlay","textures/heads/squid.png"));
    	}
    	if(ConfigurationHandler.EntityVillager) {
    		ret.put(net.minecraft.entity.passive.EntityVillager.class,new ResourceLocation("gameoverlay","textures/heads/villager.png"));
    	}
    	if(ConfigurationHandler.EntityWitch) {
    		ret.put(net.minecraft.entity.monster.EntityWitch.class,new ResourceLocation("gameoverlay","textures/heads/witch.png"));
    	}
    	if(ConfigurationHandler.EntityWolf) {
    		ret.put(net.minecraft.entity.passive.EntityWolf.class,new ResourceLocation("gameoverlay","textures/heads/wolf.png"));
    	}
    	if(ConfigurationHandler.EntityWither) {
    		ret.put(net.minecraft.entity.boss.EntityWither.class,new ResourceLocation("gameoverlay","textures/heads/wither.png"));
    	}
    	if(ConfigurationHandler.EntityZombie) {
    		ret.put(net.minecraft.entity.monster.EntityZombie.class,new ResourceLocation("gameoverlay","textures/heads/zombi.png"));
    	}
    	
		return ret;
    	
    }
    
}
