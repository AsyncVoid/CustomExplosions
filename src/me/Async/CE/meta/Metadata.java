package me.Async.CE.meta;

import me.Async.CE.Main;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;

public final class Metadata
{
	public static MetadataValue getMetadata(Metadatable meta, String key)
	{
		for(MetadataValue value : meta.getMetadata(key))
		{
			if(value.getOwningPlugin() == Main.plugin)
				return value;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Object> T getMetadata(Metadatable meta, String key, T example)
	{
		for(MetadataValue value : meta.getMetadata(key))
		{
			if(value.getOwningPlugin() == Main.plugin)
				return (T)value.value();
		}
		return null;
	}
}
