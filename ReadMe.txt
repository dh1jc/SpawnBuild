SpawnBuild
==========

The original bukkit server has a build safe area around spawn point, but no 
permission to grant builders to build there (except the operators).


This _simple_ plugin mimics this default behaviour, but gives one permission 
to use with e.g. permissionBukkit.
  builder:
    permissions:
      spawnbuild: true

Also the config.yml uses the same naming as the default bukkit.yml
  spawn-radius: 10
  
Additionally that permission can be given to all Operators or all Creatives.
  

In the original bukkit.yml you have to set 
  spawn-radius: 0

--   
Joschi