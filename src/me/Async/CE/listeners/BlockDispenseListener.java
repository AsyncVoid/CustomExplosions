package me.Async.CE.listeners;

import me.Async.CE.Main;
import me.Async.CE.enums.TNTType;
import me.Async.CE.storage.StorageHandler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockDispenseListener implements Listener
{
	@EventHandler
	public void onBlockDispense(final BlockDispenseEvent ev)
	{
		if(ev.getItem().getType() == Material.TNT)
		{
			if(ev.getItem().hasItemMeta())
			{
				if(ev.getItem().getItemMeta().hasDisplayName())
				{
					final TNTType type = Main.getFromName(ev.getItem().getItemMeta().getDisplayName());
					if(type.equals(null))
						return;
					new BukkitRunnable(){
						@Override
						public void run() {
							Location loc = ev.getVelocity().toLocation(ev.getBlock().getWorld());
							for(Entity ent : loc.getWorld().getNearbyEntities(loc, 1, 1, 1))
							{
								if(ent instanceof TNTPrimed)
								{
									//((TNTPrimed)ent).setMetadata("TNTType", new FixedMetadataValue(Main.plugin, type));
									StorageHandler.setType(ent, type);
									return;
									/*Location emp = loc.getBlock().getLocation();
									Location dis = ev.getBlock().getLocation();
									
									double bX = Math.max(dis.getX(), emp.getX());
									double sX = Math.min(dis.getX(), emp.getX());
									double bZ = Math.max(dis.getZ(), emp.getZ());
									double sZ = Math.min(dis.getZ(), emp.getZ());
									
									double dX = bX - sX;
									double dZ = bZ - sZ;
									
									double yaw = Math.atan2(dZ, dX);
									double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), 0) + Math.PI;
									double x = Math.sin(pitch) * Math.cos(yaw);
									//double y = Math.sin(pitch) * Math.sin(yaw);
									double z = Math.cos(pitch);
									Vector vector = new Vector(x, 1, z).normalize();
									
									Main.prepareTNT((TNTPrimed)ent, type, vector);*/
									
								}
							}
						}
					}.runTaskLater(Main.plugin, 1);
				}
			}
		}
	}
}
