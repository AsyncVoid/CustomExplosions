package me.Async.CE.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import me.Async.CE.Main;
import me.Async.CE.enums.TNTType;
import me.Async.CE.storage.StorageHandler;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutExplosion;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class EntityExplodeListener implements Listener
{
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent ev)
	{
		if(ev.getEntityType() == EntityType.PRIMED_TNT)
		{
			for(Block b : ev.blockList())
			{
				if(b.getType() == Material.TNT)
				{
					TNTType type = StorageHandler.getType(b);
					Vector unitVector = b.getLocation().add(0.5, 0.5, 0.5).toVector().subtract(ev.getLocation().toVector()).normalize().multiply(0.1);
					Main.detonate(b, unitVector, type);
					StorageHandler.removeType(b);
				}
			}
			final Entity tnt = ev.getEntity();
			TNTType type = StorageHandler.getType(tnt);
			StorageHandler.removeType(tnt);
			if(type != null) //tnt.hasMetadata("TNTType")
			{
				final Location loc = ev.getLocation();
				//TNTType type = Metadata.getMetadata(tnt, "TNTType", TNTType.BUNKER);
				switch(type)
				{
					case TOXIC:
						Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 10, 10, 10);
						for(Entity ent : entities)
						{
							if (ent instanceof LivingEntity)
								((LivingEntity)ent).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5*20, 1));
						}
						break;
					case NAIL:
						Random rand = new Random();
					    for (int i = 0; i < 20; i++)
					    {
					    	double x = rand.nextGaussian();
					    	double z = rand.nextGaussian();
					    	double y = Math.abs(rand.nextGaussian());
					    	Vector vel = new Vector(x * 2, y * 2, z * 2f);
					    	Entity arrow = loc.getWorld().spawnEntity(loc, EntityType.ARROW);
					      	arrow.setVelocity(vel);
					    }
					case NAPALM: 
						for(Entity ent : loc.getWorld().getNearbyEntities(loc, 10, 10, 10))
						{
							ent.setFireTicks(200);
						}
						break;
					case BUNKER:
						new BukkitRunnable(){
							@Override
							public void run() {
								Location down = loc;
								down.setY(down.getY() - 1);
								if(down.getBlock().getType() != Material.BEDROCK)
								{
									TNTPrimed spawn = (TNTPrimed)loc.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
									//spawn.setMetadata("TNTType", new FixedMetadataValue(Main.plugin, TNTType.BUNKER));
									StorageHandler.setType(spawn, TNTType.BUNKER);
									spawn.setFuseTicks(20);
									spawn.setCustomNameVisible(true);
									if(tnt.getCustomName() == null)
										spawn.setCustomName(8 + "");
									else
									{
										int i = Integer.parseInt(tnt.getCustomName()) - 1;
										spawn.setCustomName(i + "");
										if(i <= 0)
											spawn.remove();
									}
								}
							}
						}.runTaskLater(Main.plugin, 10);
						break;
					case WATER:
						Location down = loc;
						if(down.getBlock().getType() == Material.STATIONARY_WATER || down.getBlock().getType() == Material.WATER)
						{
							down.setY(down.getY() - 1);
							down.getBlock().breakNaturally();
						}
						break;
					case SMOKE:
						final BukkitTask smoke = new BukkitRunnable(){
							@Override
							public void run() {
								for(Entity ent : loc.getWorld().getNearbyEntities(loc, 10, 10, 10))
								{
									if (ent instanceof Player)
									{
										for(int x=-5; x<6; x++)
										{
											for(int z=-5; z<6; z++)
											{
												for(int y=-5; y<6; y++)
												{
													PacketPlayOutExplosion poe = new PacketPlayOutExplosion(
															loc.getX() + x, loc.getY() + y, loc.getZ() + z, 0.1f, new ArrayList<BlockPosition>(), null);
													PlayerConnection con = ((CraftPlayer) ent).getHandle().playerConnection;
													con.sendPacket(poe);
												}
											}
										}
										 
									}
								}
							}
						}.runTaskTimerAsynchronously(Main.plugin, 0, 5);
						new BukkitRunnable(){
							@Override
							public void run() {
								smoke.cancel();
							}
						}.runTaskLaterAsynchronously(Main.plugin, 200);
						break;
					case FOAMER:
						if(loc.getBlock().getType() == Material.STATIONARY_WATER || loc.getBlock().getType() == Material.WATER || loc.getBlock().getType() == Material.AIR)
						{
							loc.getBlock().setType(Material.SPONGE);
						}
						break;
					case EXTREME:
						for(int i = 0; i < 3; i++)
							loc.getWorld().createExplosion(loc, 5f, false);
						break;
					default:
						break;
						
				}
			}
		}
	}
	
	@EventHandler
	public void onExplosionPrime(ExplosionPrimeEvent ev)
	{
		if(ev.getEntityType() == EntityType.PRIMED_TNT)
		{
			TNTPrimed tnt = (TNTPrimed)ev.getEntity();
			TNTType type = StorageHandler.getType(tnt);
			if(type != null) //tnt.hasMetadata("TNTType")
			{
				//Location loc = ev.getEntity().getLocation();
				//TNTType type = Metadata.getMetadata(tnt, "TNTType", TNTType.BUNKER);
				switch(type)
				{
					case NAIL: ev.setRadius(5); break;
					case NAPALM: ev.setFire(true); ev.setRadius(3); break;
					case BUNKER: ev.setRadius(2); break;
					case TOXIC: ev.setRadius(4); break;
					case WATER: ev.setRadius(2); break;
					case ROCKET: ev.setRadius(3); break;
					case SMOKE: ev.setRadius(0); break;
					case FOAMER: ev.setRadius(0); break;
					default: break;
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockRedstone(BlockRedstoneEvent ev)
	{
		if(ev.getNewCurrent() >= 1){ //If the current of the block, be it a lever or redstone wire is ON
			Main.handleRedstone(ev.getBlock());
		}
	}
}
