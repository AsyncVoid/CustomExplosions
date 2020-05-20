package me.Async.CE.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Async.CE.Main;
import me.Async.CE.enums.TNTType;
import me.Async.CE.storage.StorageHandler;

public class BlockBreakListener implements Listener
{
	@EventHandler
	public void onBlockBreak(BlockBreakEvent ev)
	{
		if(ev.getBlock().getType() == Material.TNT)
		{
			TNTType type = StorageHandler.getType(ev.getBlock());
			if(type == null) return;
			StorageHandler.removeType(ev.getBlock());
			ev.setCancelled(true);
			ev.getBlock().setType(Material.AIR);
			ItemStack is = new ItemStack(Material.TNT);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName("§r" + Main.getName(type));
			is.setItemMeta(im);
			Location loc = ev.getBlock().getLocation().add(0.5, 0.5, 0.5);
			loc.getWorld().dropItemNaturally(loc, is);
		}
	}
}
