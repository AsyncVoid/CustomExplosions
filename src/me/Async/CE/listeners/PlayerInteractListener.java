package me.Async.CE.listeners;

import me.Async.CE.Main;
import me.Async.CE.enums.TNTType;
import me.Async.CE.storage.StorageHandler;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PlayerInteractListener implements Listener
{
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent ev)
	{
		if(ev.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(ev.getClickedBlock().getType() == Material.TNT)
			{
				Block b = ev.getClickedBlock();
				TNTType type = StorageHandler.getType(b);
				if(type == null) return;
				if(ev.getItem() == null)
				{
					ev.getPlayer().sendMessage(ChatColor.YELLOW + "This is a " + Main.getName(type));
				}
				else if(ev.getItem().getType() == Material.FLINT_AND_STEEL || ev.getItem().getType() == Material.FIREBALL)
				{
					ev.setCancelled(true);
					Vector vector = new Vector(0, 0.2, 0);
					if(type == TNTType.ROCKET)
					{
						double pitch = ((ev.getPlayer().getEyeLocation().getPitch() + 90) * Math.PI) / 180;
						double yaw  = ((ev.getPlayer().getEyeLocation().getYaw() + 90)  * Math.PI) / 180;
						double x = Math.sin(pitch) * Math.cos(yaw);
						double y = Math.sin(pitch) * Math.sin(yaw);
						double z = Math.cos(pitch);
						vector = new Vector(x * 2, z * 2, y * 2);
					}
					Main.detonate(b, vector, type);
					StorageHandler.removeType(b);
				}
			}
		}
		//if(ev.getAction() != Action.RIGHT_CLICK_AIR)
		{
			if(ev.getItem() == null) return;
			if (ev.getItem().getType() == Material.TNT)
			{
				if(ev.getItem().hasItemMeta())
				{
					if(ev.getItem().getItemMeta().hasDisplayName())
					{
						if(ev.getItem().getItemMeta().getDisplayName().contains("§rSuicide Bomb"))
						{
							ev.getPlayer().getItemInHand().setAmount(0);
							ev.getPlayer().getWorld().createExplosion(ev.getPlayer().getLocation(), 7f);
							ev.getPlayer().setHealth(0);
						}
					}
				}
			}
		}
	}
}
