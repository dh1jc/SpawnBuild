package de.dh1jc.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

/*
 * This is a sample event listener
 */
public class SpawnBuildListener implements Listener {
	private final SpawnBuild plugin;

	public SpawnBuildListener(SpawnBuild plugin) {
		// Register the listener
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	public Location getWorldSpawn(World world) {
		Location location;
		location = this.plugin.getServer().getWorld(world.getName()).getSpawnLocation();

		return location;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!event.isCancelled()) {
			Player player = event.getPlayer();

			if (!hasPermissionToBuild(player)) {
				if (getWorldSpawn(player.getWorld()).distance(player.getLocation()) <= this.plugin.getConfig().getInt("spawnbuild.spawn-radius")) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "You cannot place blocks in SpawnArea");
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		if (!event.isCancelled()) {
			Player player = event.getPlayer();

			if (!hasPermissionToBuild(player)) {
				if (getWorldSpawn(player.getWorld()).distance(player.getLocation()) <= this.plugin.getConfig().getInt("spawnbuild.spawn-radius")) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "You cannot break blocks in SpawnArea");
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerEmptyBucket(PlayerBucketEmptyEvent event) {
		if (!event.isCancelled()) {
			Player player = event.getPlayer();

			if (!hasPermissionToBuild(player)) {
				if (getWorldSpawn(player.getWorld()).distance(player.getLocation()) <= this.plugin.getConfig().getInt("spawnbuild.spawn-radius", 10)) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "You cannot empty your bucket in SpawnArea");
				}
			}
		}
	}

	public boolean hasPermissionToBuild(Player player) {
		return player.hasPermission("spawnbuild") || 
					(this.plugin.getConfig().getBoolean("spawnbuild.allowOp", true) && player.isOp()) ||
					(this.plugin.getConfig().getBoolean("spawnbuild.allowCreative", true) && (player.getGameMode()== GameMode.CREATIVE));
	}

}