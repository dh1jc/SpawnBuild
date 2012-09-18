package de.dh1jc.bukkit.plugin;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

/*
 * This is a sample event listener
 */
public class SpawnBuildListener implements Listener {
	private final SpawnBuild plugin;

	private final int spawnRadius;
	private final Boolean allowOp;
	private final Boolean allowCreative;
	
	public SpawnBuildListener(SpawnBuild plugin) {
		// Register the listener
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;

		this.spawnRadius = this.plugin.getConfig().getInt("spawnbuild.spawn-radius", 10);
		this.allowOp = this.plugin.getConfig().getBoolean("spawnbuild.allowOp", false);
		this.allowCreative = this.plugin.getConfig().getBoolean("spawnbuild.allowCreative", false);
	}
	
	public Location getWorldSpawn(World world) {
		Location location;
		location = 	world.getSpawnLocation();

		return location;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!event.isCancelled()) {
			Player player = event.getPlayer();

			if (!hasPermissionToBuild(player)) {
				if (getWorldSpawn(player.getWorld()).distance(player.getLocation()) <= spawnRadius) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "You cannot place blocks in SpawnArea.");
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		if (!event.isCancelled()) {
			Player player = event.getPlayer();

			if (!hasPermissionToBuild(player)) {
				if (getWorldSpawn(player.getWorld()).distance(player.getLocation()) <= spawnRadius) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "You cannot break blocks in SpawnArea.");
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerEmptyBucket(PlayerBucketEmptyEvent event) {
		if (!event.isCancelled()) {
			Player player = event.getPlayer();

			if (!hasPermissionToBuild(player)) {
				if (getWorldSpawn(player.getWorld()).distance(player.getLocation()) <= spawnRadius) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "You cannot empty your bucket in SpawnArea.");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerFillBucket(PlayerBucketFillEvent event) {
		if (!event.isCancelled()) {
			Player player = event.getPlayer();

			if (!hasPermissionToBuild(player)) {
				if (getWorldSpawn(player.getWorld()).distance(player.getLocation()) <= spawnRadius) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "You cannot fill your bucket in SpawnArea.");
				}
			}
		}
	}

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockIgnite (BlockIgniteEvent event) {
    	
        if (!event.isCancelled() && (
        		event.getCause().equals(IgniteCause.FLINT_AND_STEEL) || event.getCause().equals(IgniteCause.FIREBALL)        		
        		)) {
            Player player = event.getPlayer();

            if (!hasPermissionToBuild(player)) {
				if (getWorldSpawn(player.getWorld()).distance(player.getLocation()) <= spawnRadius) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "You cannot ignite something in SpawnArea.");
				}
			}
        }
    }
	
	public boolean hasPermissionToBuild(Player player) {
		return player.hasPermission("spawnbuild") || 
				player.hasPermission("spawnbuild."+player.getWorld().getName().toLowerCase()) ||
				(allowOp && player.isOp()) ||
				(allowCreative && (player.getGameMode()== GameMode.CREATIVE));
	}
}