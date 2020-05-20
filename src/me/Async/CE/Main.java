package me.Async.CE;

import me.Async.CE.enums.TNTType;
import me.Async.CE.listeners.ArrowHitBlockListener;
import me.Async.CE.listeners.BlockBreakListener;
import me.Async.CE.listeners.BlockDispenseListener;
import me.Async.CE.listeners.BlockPlaceListener;
import me.Async.CE.listeners.EntityExplodeListener;
import me.Async.CE.listeners.PlayerInteractListener;
import me.Async.CE.listeners.ProjectileHitListener;
import me.Async.CE.storage.StorageHandler;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin
{
	public static Plugin plugin;
	
	public Main()
	{
		plugin = this;
	}
	
	@Override
	public void onEnable()
	{
		Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
		Bukkit.getPluginManager().registerEvents(new EntityExplodeListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
		Bukkit.getPluginManager().registerEvents(new ProjectileHitListener(), this);
		Bukkit.getPluginManager().registerEvents(new ArrowHitBlockListener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockDispenseListener(), this);
		StorageHandler.loadMap();
	}
	
	@Override
	public void onDisable()
	{
		StorageHandler.saveMap();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("give"))
			{
				String name = "§rTNT";
				switch(args[1].toUpperCase())
				{
					case "TOXIC": name = "§rToxic Bomb"; break;
					case "BUNKER": name = "§rBunker Buster"; break;
					case "CLUSTER": name = "§rCluster Bomb"; break;
					case "NAIL": name = "§rNail Bomb"; break;
					case "NAPALM": name = "§rNapalm"; break;
					case "NUKE": name = "§rNuke"; break;
					case "SMOKE": name = "§rSmoke Bomb"; break;
					case "SUICIDE": name = "§rSuicide Bomb"; break;
					case "WATER": name = "§rDepth Charge"; break;
					case "ROCKET": name = "§rRocket"; break;
					case "FOAMER": name = "§rFoamer"; break;
					case "EXTREME": name = "§rExtreme TNT"; break;
				}
				ItemStack is = new ItemStack(Material.TNT, 64);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(name);
				is.setItemMeta(im);
				((Player)sender).getInventory().addItem(is);
			}
		}
		return false;
	}
	
	public static void detonate(Block b, Vector vector, TNTType type)
	{
		b.setType(Material.AIR);
		Location loc = b.getLocation().add(0.5, 0.5, 0.5);
		spawnTNT(type, loc, vector);
	}
	
	public static TNTPrimed spawnTNT(TNTType type, Location loc, Vector vector)
	{
		TNTPrimed ent = loc.getWorld().spawn(loc, TNTPrimed.class);
		//ent.setMetadata("TNTType", new FixedMetadataValue(Main.plugin, type));
		StorageHandler.setType(ent, type);
		ent.teleport(loc);
		ent.setVelocity(vector);
		switch(type)
		{
			case WATER: ent.setFuseTicks(160); break;
			case ROCKET:
				ent.setFuseTicks(160);
				break;
			case NAPALM:
				ent.setYield(0);
			default: break;
		}
		return ent;
	}
	
	public static TNTType getFromName(String name)
	{
		switch(name)
		{
			case "§rToxic Bomb": return TNTType.TOXIC;
			case "§rBunker Buster": return TNTType.BUNKER;
			case "§rCluster Bomb": return TNTType.CLUSTER;
			case "§rNail Bomb": return TNTType.NAIL;
			case "§rNapalm": return TNTType.NAPALM;
			case "§rNuke": return TNTType.NUKE;
			case "§rSmoke Bomb": return TNTType.SMOKE;
			case "§rDepth Charge": return TNTType.WATER;
			case "§rRocket": return TNTType.ROCKET;
			case "§rFoamer": return TNTType.FOAMER;
			case "§rExtreme TNT": return TNTType.EXTREME;
			default: return null;
		}
	}
	
	public static String getName(TNTType type)
	{
		switch(type)
		{
			case TOXIC: return "Toxic Bomb";
			case BUNKER: return "Bunker Buster";
			case CLUSTER: return "Cluster Bomb";
			case NAIL: return "Nail Bomb";
			case NAPALM: return "Napalm";
			case NUKE: return "Nuke";
			case SMOKE: return "Smoke Bomb";
			case WATER: return "Depth Charge";
			case ROCKET: return "Rocket";
			case FOAMER: return "Foamer";
			case EXTREME: return "Extreme TNT";
			default: return null;
		}
	}
	
	public static void handleRedstone(Block blk)
	{
		ArrayList<Block> tntBlocks = new ArrayList<Block>(); //There may be more than 1 TNT block connected to a redstone wire.
		Boolean tntExists = false; //I would of checked if tntBlk was null, but that creates a NPE, so I made a Boolean variable.
		if(blk.getRelative(BlockFace.NORTH).getType() == Material.TNT){
			tntBlocks.add(blk.getRelative(BlockFace.NORTH));
			tntExists = true;
		}
		if(blk.getRelative(BlockFace.EAST).getType() == Material.TNT){
			tntBlocks.add(blk.getRelative(BlockFace.EAST));
			tntExists = true;
		}
		if(blk.getRelative(BlockFace.SOUTH).getType() == Material.TNT){
			tntBlocks.add(blk.getRelative(BlockFace.SOUTH));
			tntExists = true;
		}
		if(blk.getRelative(BlockFace.WEST).getType() == Material.TNT){
			tntBlocks.add(blk.getRelative(BlockFace.WEST));
			tntExists = true;
		}
		if(blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getType() == Material.TNT){
			tntBlocks.add(blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH));
			tntExists = true;
		}
		if(blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getType() == Material.TNT){
			tntBlocks.add(blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST));
			tntExists = true;
		}
		if(blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getType() == Material.TNT){
			tntBlocks.add(blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH));
			tntExists = true;
		}
		if(blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getType() == Material.TNT){
			tntBlocks.add(blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST));
			tntExists = true;
		}
		if(blk.getRelative(BlockFace.DOWN).getType() == Material.TNT){
			tntBlocks.add(blk.getRelative(BlockFace.DOWN));
			tntExists = true;
		}
		if(tntExists){ //If tntExists is set to true, then there is a TNT Block in the area.
			for(Block tnt : tntBlocks){
				TNTType type = StorageHandler.getType(tnt);
				if(type != null)
					Main.detonate(tnt, new Vector(0, 0.2, 0), type);
			}
		}
	}
}
