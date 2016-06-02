package de.maefvfis.gameoverlay.client.schematic.schematic;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameData;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.utility.LogHelper;
import de.maefvfis.gameoverlay.utility.MBlockPos;


public class SchematicSave {
	
	
	private static final FMLControlledNamespacedRegistry<Block> BLOCK_REGISTRY = GameData.getBlockRegistry();
	public World world = Minecraft.getMinecraft().theWorld;
	
	public static int[] isCoord(String string) {
		
		
		String[] strArray = string.split(",");
		int[] intArray = new int[strArray.length];
		for(int i = 0; i < strArray.length; i++) {
		    intArray[i] = Integer.parseInt(strArray[i]);
		}
		
		if(intArray.length == 3) {
			return intArray;
		}
		
		return null;
		
	}
	
	public SchematicSave() {
		
		if(ConfigurationHandler.SchematicPos1.isEmpty() || ConfigurationHandler.SchematicPos2.isEmpty()) {
			Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new TextComponentString("Select region first!"));
			return;
		}
		
		int[] pos1 = isCoord(ConfigurationHandler.SchematicPos1);
		int[] pos2 = isCoord(ConfigurationHandler.SchematicPos2);
		
		if(pos1 == null || pos2 == null) { return; }		
		
		final int fromX = pos1[0] < pos2[0] ? pos1[0] : pos2[0];
        final int toX = pos1[0] > pos2[0] ? pos1[0] : pos2[0];
        
        final int fromY = pos1[1] < pos2[1] ? pos1[1] : pos2[1];
        final int toY = pos1[1] > pos2[1] ? pos1[1] : pos2[1];
        
        final int fromZ = pos1[2] < pos2[2] ? pos1[2] : pos2[2];
        final int toZ = pos1[2] > pos2[2] ? pos1[2] : pos2[2];
		
		final int width = toX - fromX + 1;
		final int height = toY - fromY + 1;
		final int lengh = toZ - fromZ + 1;
		
		Schematic schematic = new Schematic(null, width, height, lengh);
		 
		schematic = copyToSchematic(schematic, fromX, toX, fromY, toY, fromZ, toZ);
		File file = new File(ConfigurationHandler.SchematicName + ".schematic");
		writeToFile(file ,schematic); 
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new TextComponentString("Saved: " + ConfigurationHandler.SchematicName + ".schematic"));
		 
	}
	
	 public Schematic copyToSchematic(final Schematic schematic, final int fromX, final int toX, final int fromY, final int toY, final int fromZ, final int toZ) {
	        BlockPos pos;
	        BlockPos localPos;
	        
	        for (int x = fromX; x <= toX; x++) {
	            for (int y = fromY; y <= toY; y++) {
	                for (int z = fromZ; z <= toZ; z++) {
	                	
	                	final int sX = x - fromX;
	                	final int sY = y - fromY;
	                	final int sZ = z - fromZ;
	                	pos = new BlockPos(x, y, z);
	                    localPos = new BlockPos(sX, sY, sZ);
	                	
	                    final IBlockState blockState = world.getBlockState(pos);
                        final Block block = blockState.getBlock();
                        final boolean success = schematic.setBlockState(localPos, blockState);

                        if (success && block.hasTileEntity(blockState)) {
                            final TileEntity tileEntity = world.getTileEntity(pos);
                            if (tileEntity != null) {
                            	schematic.setTileEntity(localPos, tileEntity);
                            }
                        }
	                	
	                }
	            }
	        }
	        return schematic;
	 }    
	        
	 
	 
	 
	 public static boolean writeToNBT(NBTTagCompound tagCompound, Schematic schematic) {
		
	        NBTTagCompound tagCompoundIcon = new NBTTagCompound();
	        ItemStack icon = new ItemStack(Blocks.GRASS);
	        
	        icon.writeToNBT(tagCompoundIcon);
	        
	        tagCompound.setTag(Names.NBT.ICON, tagCompoundIcon);

	        tagCompound.setShort(Names.NBT.WIDTH, (short) schematic.getWidth());
	        tagCompound.setShort(Names.NBT.LENGTH, (short) schematic.getLength());
	        tagCompound.setShort(Names.NBT.HEIGHT, (short) schematic.getHeight());
	        
	        int size = schematic.getWidth() * schematic.getLength() * schematic.getHeight();
	        byte localBlocks[] = new byte[size];
	        byte localMetadata[] = new byte[size];
	        byte extraBlocks[] = new byte[size];
	        byte extraBlocksNibble[] = new byte[(int) Math.ceil(size / 2.0)];
	        boolean extra = false;
	        
	        final MBlockPos pos = new MBlockPos();
	        Map<String, Short> mappings = new HashMap<String, Short>();
	        for (int x = 0; x < schematic.getWidth(); x++) {
	            for (int y = 0; y < schematic.getHeight(); y++) {
	                for (int z = 0; z < schematic.getLength(); z++) {
	                	
	                    final int index = x + (y * schematic.getLength() + z) * schematic.getWidth();
	                    final IBlockState blockState = schematic.getBlockState(new BlockPos(x, y, z));
	                    final Block block = blockState.getBlock();
	                    final int blockId = BLOCK_REGISTRY.getId(block);
	                    localBlocks[index] = (byte) blockId;
	                    localMetadata[index] = (byte) block.getMetaFromState(blockState);
	                    if ((extraBlocks[index] = (byte) (blockId >> 8)) > 0) {
	                        extra = true;
	                    }

	                    String name = String.valueOf(BLOCK_REGISTRY.getNameForObject(block));
	                    if (!mappings.containsKey(name)) {
	                        mappings.put(name, (short) blockId);
	                    }
	                }
	            }
	        }

	        int count = 20;
	        NBTTagList tileEntitiesList = new NBTTagList();
	        for (TileEntity tileEntity : schematic.getTileEntities()) {
	            try {
	                NBTTagCompound tileEntityTagCompound = NBTHelper.writeTileEntityToCompound(tileEntity);
	                tileEntitiesList.appendTag(tileEntityTagCompound);
	            } catch (Exception e) {
	                final BlockPos tePos = tileEntity.getPos();
	                final int index = tePos.getX() + (tePos.getY() * schematic.getLength() + tePos.getZ()) * schematic.getWidth();
	                if (--count > 0) {
	                    final IBlockState blockState = schematic.getBlockState(tePos);
	                    Block block = blockState.getBlock();
	                    //Reference.logger.error("Block {}[{}] with TileEntity {} failed to save! Replacing with bedrock...", block, block != null ? BLOCK_REGISTRY.getNameForObject(block) : "?", tileEntity.getClass().getName(), e);
	                }
	                localBlocks[index] = (byte) BLOCK_REGISTRY.getId(Blocks.BEDROCK);
	                localMetadata[index] = 0;
	                extraBlocks[index] = 0;
	            }
	        }

	        for (int i = 0; i < extraBlocksNibble.length; i++) {
	            if (i * 2 + 1 < extraBlocks.length) {
	                extraBlocksNibble[i] = (byte) ((extraBlocks[i * 2 + 0] << 4) | extraBlocks[i * 2 + 1]);
	            } else {
	                extraBlocksNibble[i] = (byte) (extraBlocks[i * 2 + 0] << 4);
	            }
	        }

	        final NBTTagList entityList = new NBTTagList();
	        final List<Entity> entities = schematic.getEntities();
	        for (Entity entity : entities) {
	            try {
	                final NBTTagCompound entityCompound = NBTHelper.writeEntityToCompound(entity);
	                if (entityCompound != null) {
	                    entityList.appendTag(entityCompound);
	                }
	            } catch (Throwable t) {
	                //Reference.logger.error("Entity {} failed to save, skipping!", entity, t);
	            }
	        }

	        //PreSchematicSaveEvent event = new PreSchematicSaveEvent(schematic, mappings);
	        //MinecraftForge.EVENT_BUS.post(event);

	        NBTTagCompound nbtMapping = new NBTTagCompound();
	        for (Map.Entry<String, Short> entry : mappings.entrySet()) {
	            nbtMapping.setShort(entry.getKey(), entry.getValue());
	        }

	        tagCompound.setString(Names.NBT.MATERIALS, Names.NBT.FORMAT_ALPHA);
	        tagCompound.setByteArray(Names.NBT.BLOCKS, localBlocks);
	        tagCompound.setByteArray(Names.NBT.DATA, localMetadata);
	        if (extra) {
	            tagCompound.setByteArray(Names.NBT.ADD_BLOCKS, extraBlocksNibble);
	        }
	        tagCompound.setTag(Names.NBT.ENTITIES, entityList);
	        tagCompound.setTag(Names.NBT.TILE_ENTITIES, tileEntitiesList);
	        tagCompound.setTag(Names.NBT.MAPPING_SCHEMATICA, nbtMapping);
	        //final NBTTagCompound extendedMetadata = event.extendedMetadata;
	        //if (!extendedMetadata.hasNoTags()) {
	         //   tagCompound.setTag(Names.NBT.EXTENDED_METADATA, extendedMetadata);
	        //}
	        return true;
	    }

	 private static Method methodSetNBTTagName;

	    public static void writeEntry(String name, NBTBase nbt, DataOutput dataOutput)
	    {
	        if (methodSetNBTTagName == null)
	            methodSetNBTTagName = net.minecraftforge.fml.relauncher.ReflectionHelper.findMethod(NBTTagCompound.class, new NBTTagCompound(),
	                    new String[]{"func_150298_a", "writeEntry"},
	                    String.class, NBTBase.class, DataOutput.class);


	            try {
					try {
						methodSetNBTTagName.invoke(null, name, nbt, dataOutput);
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }

	    


    public boolean writeToFile(File file, Schematic schematic) {
        try {
        	
            NBTTagCompound tagCompound = new NBTTagCompound();
            
            writeToNBT(tagCompound, schematic);
            
            DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
            
            
            try {
                writeEntry(Names.NBT.ROOT, tagCompound, dataOutputStream);
            } finally {
                dataOutputStream.close();
            }

            return true;
        } catch (Exception ex) {
        	LogHelper.info("Failed to write schematic!");
            //Reference.logger.error("Failed to write schematic!", ex);
        }

        return false;
    }
    
    

    
    
    
    
}