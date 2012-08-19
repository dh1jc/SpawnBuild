package de.dh1jc.bukkit.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import de.dh1jc.bukkit.plugin.SpawnBuildListener;
 
public class SpawnBuild extends JavaPlugin  {
	
	public void onEnable(){
		getLogger().info("spawn-radius set to "+  this.getConfig().getInt("spawnbuild.spawn-radius", 10));
		getLogger().info("Ops are ok "+  this.getConfig().getBoolean("spawnbuild.allowOp", false));
		 
        // save the configuration file, if there are no values, write the defaults.
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

		// Create the Listener
        new SpawnBuildListener(this);    
	}
	
	public void onDisable(){        
        getLogger().info("SpawnBuild disabled.");
	}		
}