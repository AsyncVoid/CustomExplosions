package me.Async.CE.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import me.Async.CE.Main;
import me.Async.CE.enums.TNTType;

public class StorageHandler {
	
	private static final ConcurrentHashMap<Integer, TNTType> blockMap = new ConcurrentHashMap<Integer, TNTType>();
	private static final ConcurrentHashMap<Integer, TNTType> entityMap = new ConcurrentHashMap<Integer, TNTType>();
	
	private static final String fileName = Main.plugin.getDataFolder() + "\\TNTStore.dat";
	
	public static TNTType getType(BlockLoc loc)
	{
		try{
			return blockMap.get(loc.hashCode());
		}
		catch (NullPointerException ex)
		{
			return null;
		}
	}
	
	public static TNTType getType(Block b)
	{
		return getType(new BlockLoc(b));
	}
	
	public static TNTType getType(Entity ent)
	{
		try{
			return entityMap.get(ent.getEntityId());
		}
		catch (NullPointerException ex)
		{
			return null;
		}
	}
	
	public static void setType(BlockLoc loc, TNTType type)
	{
		blockMap.put(loc.hashCode(), type);
	}
	
	public static void setType(Block b, TNTType type)
	{
		blockMap.put(new BlockLoc(b).hashCode(), type);
	}
	
	public static void setType(Entity ent, TNTType type)
	{
		entityMap.put(ent.getEntityId(), type);
	}
	
	public static void removeType(BlockLoc loc)
	{
		try {
			blockMap.remove(loc.hashCode());
		}
		catch (NullPointerException ex)
		{
			
		}
	}
	
	public static void removeType(Block b)
	{
		removeType(new BlockLoc(b));
	}
	
	public static void removeType(Entity ent)
	{
		try {
			entityMap.remove(ent.getEntityId());
		}
		catch (NullPointerException ex)
		{
			
		}
	}
	
	public static void loadMap()
	{
		try {
			FileInputStream fis = new FileInputStream(fileName);
			DataInputStream dis = new DataInputStream(fis);
			Integer blockHash = null;
			while (dis.available() > 0) {
				if(blockHash == null)
				{
					blockHash = dis.readInt();
				}
				else
				{
					blockMap.put(blockHash, TNTType.getFromID(dis.readByte()));
					blockHash = null;
				}
			}
			dis.close();
		} catch(IOException ex) { }
	}
	
	public static void saveMap()
	{
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			DataOutputStream dos = new DataOutputStream(fos);
			for(Entry<Integer, TNTType> entry : blockMap.entrySet())
			{
				dos.writeInt(entry.getKey());
				dos.writeByte(entry.getValue().getID());
			}
			dos.close();
		} catch(IOException ex) { 
			ex.printStackTrace();
		}
	}
}
