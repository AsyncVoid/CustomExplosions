package me.Async.CE.listeners;

import me.Async.CE.Main;
import me.Async.CE.enums.TNTType;
import me.Async.CE.storage.StorageHandler;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

public class BlockPlaceListener implements Listener
{
	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent ev)
	{
		if(ev.getBlockPlaced().getType() == Material.TNT)
		{
			if(ev.isCancelled()) return;
			if(!ev.getItemInHand().hasItemMeta()) return;
			if(ev.getItemInHand().getItemMeta().hasDisplayName())
			{
				String name = ev.getItemInHand().getItemMeta().getDisplayName();
				final TNTType type = Main.getFromName(name);
				if(type == null) return;
				
				if(ev.getBlockPlaced().getBlockPower() > 0)
				{
					Main.detonate(ev.getBlockPlaced(), new Vector(0, 0.2, 0), type);
					return;
				}
				
				StorageHandler.setType(ev.getBlockPlaced(), type);
				/*
				final int id = Main.getID(type);
				Location loc = ev.getBlockPlaced().getLocation().add(0.5, 0, 0.5);
				ArmorStand stand = (ArmorStand) ev.getBlockPlaced().getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
				stand.setArms(false);
				stand.setBasePlate(false);
				stand.setCanPickupItems(false);
				//stand.setCustomName("HAZARD");
				//stand.setCustomNameVisible(false);
				stand.setGravity(false);
				stand.setMarker(false);
				stand.setVisible(false);
				stand.setSmall(true);
				stand.setMaxHealth(id);*/
				//LeashHitch a = (LeashHitch) ev.getBlockPlaced().getWorld().spawnEntity(loc, EntityType.LEASH_HITCH);
				//{Small:1, ShowArms: 0, NoGravity:1, NoBasePlate:1, Invisible:1}
			}
			
		}
		else if(ev.getBlockPlaced().getType() == Material.REDSTONE_TORCH_ON || ev.getBlockPlaced().getType() == Material.REDSTONE_BLOCK)
		{
			Main.handleRedstone(ev.getBlock());
		}
	}
}
