package me.Async.CE.listeners;

import me.Async.CE.Main;
import me.Async.CE.enums.TNTType;
import me.Async.CE.events.ArrowHitBlockEvent;
import me.Async.CE.storage.StorageHandler;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class ArrowHitBlockListener implements Listener
{
	@EventHandler
	public void onArrowHitBlock(ArrowHitBlockEvent ev)
	{
		if(ev.getArrow().getFireTicks() > 0)
		{
			//if(ev.getBlock().getType() == Material.AIR)
			{
				Block b = ev.getBlock();
				TNTType type = StorageHandler.getType(b);
				/*for(Entity ent : b.getWorld().getNearbyEntities(b.getLocation().add(0.5, 0, 0.5), 0.5, 0.5, 0.5))
				{
					if(ent instanceof ArmorStand)
					{
						type = Main.getFromID((int) ((ArmorStand)ent).getMaxHealth());
						ent.remove();
						break;
					}
				}*/
				if(type != null)
				{
					ev.getArrow().remove();
					for(Entity ent : b.getWorld().getNearbyEntities(b.getLocation().add(0.5, 0.5, 0.5), 0.5, 0.5, 0.5))
					{
						if(ent instanceof TNTPrimed && StorageHandler.getType(ent) == null)
						{
							((TNTPrimed)ent).remove();
							break;
						}
					}
					Main.detonate(b, new Vector(0, 0.2, 0), type);
					StorageHandler.removeType(ev.getBlock());
				}
			}
		}
	}
}