package de.dh1jc.bukkit.plugin;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import de.dh1jc.bukkit.plugin.SpawnBuild;

/*
 * This is a sample event listener
 */
public class SpawnBuildListener implements Listener {
	private final SpawnBuild plugin;

	private final int spawnRadius;
	private final Boolean allowDamage;
	private final Boolean allowFight;
	private final Boolean allowOp;
	private final Boolean allowCreative;
	
	public SpawnBuildListener(SpawnBuild plugin) {
		// Register the listener
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;

		this.spawnRadius = this.plugin.getConfig().getInt("spawnbuild.spawn-radius", 10);
		this.allowOp = this.plugin.getConfig().getBoolean("spawnbuild.allowOp", false);
		this.allowCreative = this.plugin.getConfig().getBoolean("spawnbuild.allowCreative", false);
		this.allowDamage = this.plugin.getConfig().getBoolean("spawnbuild.allowDamage", true);
		this.allowFight = this.plugin.getConfig().getBoolean("spawnbuild.allowFight", true);
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

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerPVP (EntityDamageByEntityEvent event) {
        if (!event.isCancelled()) {
            // Are you getting damaged?
        	Entity entity = event.getEntity();
            if (entity instanceof Player) {
                Player player = (Player)entity;
 
                if (player.hasPermission("spawnbuild.safe") || !allowDamage) {
                	if (getWorldSpawn(player.getWorld()).distance(player.getLocation()) <= spawnRadius) {
	                    event.setCancelled(true);
	                    player.sendMessage(ChatColor.RED + "You cannot be damaged while in SpawnArea.");
	
	                    // send notify to damager
	                    entity = event.getDamager();
	                    if (entity instanceof Player) {
	                    	player = (Player)entity;
	                    	player.sendMessage(ChatColor.RED + "You cannot damage someone in SpawnArea.");
	                    }
	                }
                }
            }
            
            // Are you damaging others
            entity = event.getDamager();
            if (entity instanceof Player) {
                Player player = (Player)entity;

                if (!player.hasPermission("spawnbuild.fight") || !allowFight) {
                	if (getWorldSpawn(player.getWorld()).distance(player.getLocation()) <= spawnRadius) {
    	                event.setCancelled(true);
    	                player.sendMessage(ChatColor.RED + "You cannot fight while in SpawnArea.");
                	}
                }
            }
        }
    }
	
	public boolean hasPermissionToBuild(Player player) {
		return player.hasPermission("spawnbuild") || 
					(allowOp && player.isOp()) ||
					(allowCreative && (player.getGameMode()== GameMode.CREATIVE));
	}
}