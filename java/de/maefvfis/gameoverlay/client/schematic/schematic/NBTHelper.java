package de.maefvfis.gameoverlay.client.schematic.schematic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class NBTHelper {
    public static List<TileEntity> readTileEntitiesFromCompound(final NBTTagCompound compound) {
        return readTileEntitiesFromCompound(compound, new ArrayList<TileEntity>());
    }

    public static List<TileEntity> readTileEntitiesFromCompound(final NBTTagCompound compound, final List<TileEntity> tileEntities) {
        final NBTTagList tagList = compound.getTagList(Names.NBT.TILE_ENTITIES, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            final NBTTagCompound tileEntityCompound = tagList.getCompoundTagAt(i);
            final TileEntity tileEntity = readTileEntityFromCompound(tileEntityCompound);
            tileEntities.add(tileEntity);
        }

        return tileEntities;
    }

    public static NBTTagCompound writeTileEntitiesToCompound(final List<TileEntity> tileEntities) {
        return writeTileEntitiesToCompound(tileEntities, new NBTTagCompound());
    }

    public static NBTTagCompound writeTileEntitiesToCompound(final List<TileEntity> tileEntities, final NBTTagCompound compound) {
        final NBTTagList tagList = new NBTTagList();
        for (TileEntity tileEntity : tileEntities) {
            final NBTTagCompound tileEntityCompound = writeTileEntityToCompound(tileEntity);
            tagList.appendTag(tileEntityCompound);
        }

        compound.setTag(Names.NBT.TILE_ENTITIES, tagList);

        return compound;
    }

    public static List<Entity> readEntitiesFromCompound(final NBTTagCompound compound) {
        return readEntitiesFromCompound(compound, null, new ArrayList<Entity>());
    }

    public static List<Entity> readEntitiesFromCompound(final NBTTagCompound compound, final World world) {
        return readEntitiesFromCompound(compound, world, new ArrayList<Entity>());
    }

    public static List<Entity> readEntitiesFromCompound(final NBTTagCompound compound, final List<Entity> entities) {
        return readEntitiesFromCompound(compound, null, entities);
    }

    public static List<Entity> readEntitiesFromCompound(final NBTTagCompound compound, final World world, final List<Entity> entities) {
        final NBTTagList tagList = compound.getTagList(Names.NBT.ENTITIES, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            final NBTTagCompound entityCompound = tagList.getCompoundTagAt(i);
            final Entity entity = readEntityFromCompound(entityCompound, world);
            if (entity != null) {
                entities.add(entity);
            }
        }

        return entities;
    }

    public static NBTTagCompound writeEntitiesToCompound(final List<Entity> entities) {
        return writeEntitiesToCompound(entities, new NBTTagCompound());
    }

    public static NBTTagCompound writeEntitiesToCompound(final List<Entity> entities, final NBTTagCompound compound) {
        final NBTTagList tagList = new NBTTagList();
        for (Entity entity : entities) {
            final NBTTagCompound entityCompound = new NBTTagCompound();
            entity.writeToNBT(entityCompound);
            tagList.appendTag(entityCompound);
        }

        compound.setTag(Names.NBT.ENTITIES, tagList);

        return compound;
    }

   

    public static NBTTagCompound writeTileEntityToCompound(final TileEntity tileEntity) {
        final NBTTagCompound tileEntityCompound = new NBTTagCompound();
        tileEntity.writeToNBT(tileEntityCompound);
        return tileEntityCompound;
    }

    public static TileEntity readTileEntityFromCompound(final NBTTagCompound tileEntityCompound) {
        return TileEntity.createTileEntity(null, tileEntityCompound);
        // TODO null ?
    }

    public static NBTTagCompound writeEntityToCompound(final Entity entity) {
        final NBTTagCompound entityCompound = new NBTTagCompound();
        if (entity.writeToNBTOptional(entityCompound)) {
            return entityCompound;
        }

        return null;
    }

    public static Entity readEntityFromCompound(final NBTTagCompound nbtTagCompound, final World world) {
        return EntityList.createEntityFromNBT(nbtTagCompound, world);
    }
}