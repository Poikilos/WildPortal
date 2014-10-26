#WildPortal
==========

GOAL: Teleport to the random place in wild, avoiding towny claims, caves, residences, protected worldguard regions, lava, and water


##Usage:  
/wildportal (create a wildportal as next clicked standing or wall-mounted sign [if wildportal.manage permission])  
/wildportal remove (remove WildPortal from sign [if wildportal.manage permission])  
Use the portal by clicking the sign (allow list not yet implemented, so everyone can use it)  
/wildportal return (return to the block last used as a portal [if wildportal.return permission])  
/wildportal verbose [true|false] (set the level of debug output to the server console--if neither true nor false are provided, the value will be toggled [if wildportal.return permission])  

##Release Notes:
2014-10-25  
* [resolved: Set player as invulnerable for 20 seconds (20*20 ticks) after teleport.] Not invulnerable as long as expected after teleported
* Added LastCreated WildPortal variables to config, so that return can be used (by someone with manage) even if did not click a sign (last used sign is not saved, only last created sign)
* Added /wildportal verbose (to toggle additional server console logging)
* Finished load and save of WildPortal.csv (along with that, finished implementing RTable and fixed its bugs).
* [resolved: new MultiEventHandler() should be called once] new MultiEventHandler() should be called twice (for setExecutor then registerEvents)
* operating on portal may fail due to finding it in database table by checking double location instead of int location in wildportalListRemoveByLocation and wildportalListContainsLocation
* implement "<this>" keyword  

2014-10-24  
* Spawn even higher in air to avoid trees and allow chunks more time to load (invincible for 60 seconds)
* implement permissions
* Basic features are implemented (except setting destination range)
* You can create infinite wildportals, but they won't yet be saved on server shutdown.
* check for an air block (account for situations where a block that is neither listed as habitable nor as bad will smother player)  

2014-10-21  
* implemented spawning high enough above ground to avoid problems like chunk still loading

##ToDo:  
* make sure not spawning in leaves farther above ground than "spawn above ground" value
* make sure spawn above ground is not higher than 255
* make sure grass at 255 or 254 is not used as habitable (avoid smothering)
* change the location and range of WildPortal rectangle via commands or clicks (other than Editing WildPortal.csv while server is down).
* make other version of jar other than compiled against Tekkit.jar (tekkit's modded CB 1.2.5 jar)
* implement checking/modifying AllowedPlayerList field in WildPortal.csv
* account for chunks that are not yet generated
* account for data from WorldBorder plugin
* account for data from Residence plugin
* account for WorldGuard regions
* account for Towny town blocks or plots
* set player's spawn point to the destination (?)
* Implement setting destination world by using command instead of editing csv
* Test world to world teleport

##Troubleshooting:  
If teleporter does not work or you get any "no world named" errors, you probably need to open your CSV file in notepad++ and replace all instances of your incorrect world name and set them to the actual name of the world you are using (this solution will work if the problem was caused by moving to a new world after already having entries in WildPortal.csv) in the SourceWorld and DestinationWorld columns.
