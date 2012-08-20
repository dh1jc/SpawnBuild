package de.dh1jc.bukkit.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import de.dh1jc.bukkit.plugin.SpawnBuildListener;
 
public class SpawnBuild extends JavaPlugin  {
	
	public void onEnable(){		 
        // save the configuration file, if there are no values, write the defaults.
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

		// Create the Listener
        new SpawnBuildListener(this);    
        getLogger().info("SpawnBuild enabled.");

	}
	
	public void onDisable(){        
        getLogger().info("SpawnBuild disabled.");
	}		
}