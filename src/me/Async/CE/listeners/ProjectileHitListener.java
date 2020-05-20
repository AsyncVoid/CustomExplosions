package me.Async.CE.listeners;

import java.lang.reflect.Field;

import me.Async.CE.Main;
import me.Async.CE.events.ArrowHitBlockEvent;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ProjectileHitListener implements Listener
{
	@EventHandler
	public void onProjectileHit(final ProjectileHitEvent ev)
	{
		if (ev.getEntityType() != EntityType.ARROW) return;
        new BukkitRunnable() {
        	public void run() {
        		try {
        			net.minecraft.server.v1_8_R3.EntityArrow entityArrow = ((CraftArrow)ev.getEntity()).getHandle();
        			Field fieldX = net.minecraft.server.v1_8_R3.EntityArrow.class.getDeclaredField("d");
                    Field fieldY = net.minecraft.server.v1_8_R3.EntityArrow.class.getDeclaredField("e");
                    Field fieldZ = net.minecraft.server.v1_8_R3.EntityArrow.class.getDeclaredField("f");
                    
                    fieldX.setAccessible(true);
                    fieldY.setAccessible(true);
                    fieldZ.setAccessible(true);

                    int x = fieldX.getInt(entityArrow);
                    int y = fieldY.getInt(entityArrow);
                    int z = fieldZ.getInt(entityArrow);

                    if (y != -1) {
                        Block block = ev.getEntity().getWorld().getBlockAt(x, y, z);
                        Bukkit.getServer().getPluginManager()
                            .callEvent(
                                    new ArrowHitBlockEvent((Arrow) ev.getEntity(), block)
                                    );
                    }
                } catch (NoSuchFieldException e1) {
                    e1.printStackTrace();
                } catch (SecurityException e1) {
                    e1.printStackTrace();
                } catch (IllegalArgumentException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
             }
        }.runTaskLaterAsynchronously(Main.plugin, 0);
	}
}
