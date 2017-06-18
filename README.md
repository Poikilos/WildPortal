# WildPortal
==========
Mark a sign as a "WildPortal" to a random place in wild.
Place in your enclosed, protected spawn then place a WildPortal in it!

## Unique features:
* Mark any vanilla sign as a WildPortal, and sign can say anything
* Avoids blocks which would make the immediate area uninhabitable (avoids water, lava, etc).
* Avoids unnatural blocks such as cobble, wood planks, and bricks
* Data is saved whenever changes are made

## Usage:  
### Admin HowTos
* Give players wildportal.return by default so they can type ```/wildportal return``` to get back to their randomly-generated home location
* Give admins wildportal.manage (or op) to view all commands when ```/wildportal help``` is typed, and to use those management commands.
### Player HowTos
* Use the portal by clicking the sign  
* Type ```/wildportal return``` : return to the location given to you by wildportal (if your account/rank wildportal.return permission)

## Developer Info
### Compiling
* Get Eclipse (such as KEPLER)
* make new folder for workspace
* download a workspaces zip from releases page and unzip the two folders starting with dot to same workspace folder as WildPortal folder
* Make sure you haven't created extra subfolders while unzipping (workspace folder should directly contain WildPortal folder, and WildPortal folder should directly contain src folder) 
* change entry (such as . . . /Hexxit.jar) in .classpath to match the location of your minecraft server jar--OR: Right-click project, Properties, Java Build Path, Libraries, remove any old/missing jar, Add External Jars, find your server JAR.
* change version in plugin.yml if necessary
* change usage strings if you changed behaviors: usagePlayerMsg and usageManageMsg in MultiEventHandler.java
* F5 (shortcut for: right-click WildPortal project on left, Refresh): always do this before export to avoid out-of-sync errors during export
* right-click WildPortal project on left, Export, JAR file, change output filename if different version than CB 1.5.2, Finish (do not select runnable jar)
### Turn on autocomplete for Eclipse:
* Window, Preferences
  * Java, Editor, Content Assist
    * Change "Auto activation triggers" to: .abcdefghijklmnopqrstuvwxyz
	  * Advanced
	    * Check everything (except perhaps "Word Proposals") in first list ("Select the proposal kinds contained in the 'default' content assist list:")

## Changes:
### Changes up to 1.5.2.2 are below
* (2017-06-17) changed version in plugin.yml to 1.5.2.3
* (2017-06-17) added command wildportal save : save portals.yml and players.yml
* (2017-06-17) added command wildportal reload : reload portals.yml and players.yml
* (2017-06-17) shows plugin 
* (2017-06-17) updated usage in plugin.yml
* (2017-06-17) now uses plugin.yml version string when showing version
* (2017-06-17) added /wildportal set destination <width|height> <value>
* (2017-06-17) change the location and range of WildPortal rectangle via commands or clicks (other than Editing WildPortal.csv while server is down).
* (2017-06-17) copy TODOs in code to this list
* (2017-06-17) renamed IRect to GroundRect, changed y to z, and added world name; accordingly, changed height to depth; made all members lowercase.
* (2017-06-17) changed function of /wildportal return to become a user feature which returns them to the random point to which they were teleported by the sign. 
* (2017-06-16) changing over from overuse of WildPortal class to directly using yml config object
* (2017-06-15) changing from CSV to yml for location data (eliminated column indices and other csv-related methods)
* (2017-06-15) adding planned YML player data
* (2017-06-14) renamed WildPortalPlayerData to WildPortalPlayer
* (2017-06-14) added command: /wildportal get destination
* (2017-06-14) added command: /wildportal get destination center
* (2017-06-13) added command: /wildportal set destination center <x>,<z> or <x>,<y>,<z> or <worldname>,<x>,<y>,<z>
  (moves rectangle using center--recalculates rectangle using that center and pre-existing width and height)
* (2017-06-13) added command: /wildportal select
* (2017-06-10) Removed use of getBlockTypeIdAt (deprecated by Mojang, so use Bukkit material instead: Material mat =  world.getBlockAt(tryX,tryY,tryZ).getType();) -- see also [https://dev.bukkit.org/projects/supplies/pages/material-list](https://dev.bukkit.org/projects/supplies/pages/material-list)
* (2017-06-10) Changed Destination.Y to Destination.Z to match usage (and Minecraft orientation)
(2017-06-06)
* upgraded Eclipse project format to KEPLER
* changed target (see "change entry" under "Compiling" section above) to CB 1.5.2
* changed version in plugin.yml from 1.0 to 1.5.2.2
* changed myNameAndVersion to 2017-06-06
* changed versioning to Minecraft version then build number (such that build 1 of WildPortal for Hexxit is 1.5.2.1)
### Changes up to 1.2.5.1 (Tekkit Classic) version (formerly named 1.0)
(2014-10-25)
* [resolved: Set player as invulnerable for 20 seconds (20*20 ticks) after teleport.] Not invulnerable as long as expected after teleported
* Added LastCreated WildPortal variables to config, so that return can be used (by someone with manage) even if did not click a sign (last used sign is not saved, only last created sign)
* Added /wildportal verbose (to toggle additional server console logging)
* Finished load and save of WildPortal.csv (along with that, finished implementing RTable and fixed its bugs).
* [resolved: new MultiEventHandler() should be called once] new MultiEventHandler() should be called twice (for setExecutor then registerEvents)
* operating on portal may fail due to finding it in database table by checking double location instead of int location in wildportalListRemoveByLocation and wildportalListContainsLocation
* implement "<this>" keyword  
(2014-10-24)
* Spawn even higher in air to avoid trees and allow chunks more time to load (invincible for 60 seconds)
* implement permissions
* Basic features are implemented (except setting destination range)
* You can create infinite wildportals, but they won't yet be saved on server shutdown.
* check for an air block (account for situations where a block that is neither listed as habitable nor as bad will smother player)  
(2014-10-21)
* implemented spawning high enough above ground to avoid problems like chunk still loading

## Need testing
* account for chunks that are not yet generated
* world to world teleport
* when sign is broken, autoremove wildportal entry from configuration
* see if broadcastMessage is going to all players (such as when player uses wildportal)
* multiple wildportals, and deleting one of them

## Regression tests
* always make sure equalsIngoreCase or equals is used instead of == for strings, since == is the identity operator for Java strings (only results in true if is the same string object in memory), not the comparison operator.
* always make sure player.hasPermission("wildportal.manage") for management commands
* always use getPrettyLocationString which returns string containing the word "null" if null, so no string concatenation with null error occurs

## Known Issues:  
* Check for world border in doWildPortal (use instead of rect?)
* in getLocationOnGround, also make sure it is "level" ground (check for ~9x9 area with only levels only differing by up to 1.0)
* make sure not spawning in leaves farther above ground than "spawn above ground" value
* make sure spawn above ground is not higher than 255
* make sure grass at 255 or 254 is not used as habitable (avoid smothering)
* account for data from WorldBorder plugin
* account for data from Residence plugin
* account for WorldGuard regions
* account for Towny town blocks or plots

## Planned Features
NOTE: planned features may never get done
* implement AllowedPlayerList field in WildPortal.csv
* implement commands for changing allowed player list for each wildportal
* Implement setting destination world by using command instead of editing csv
* sethome automatically after teleport

## Closed, won't fix
* set player's spawn point to the destination (is this possible? maybe with EssentialsSpawn; may not be possible, see [https://bukkit.org/threads/how-to-set-spawn.103950/](https://bukkit.org/threads/how-to-set-spawn.103950/))
* Allow blocks from other mods to be inhabitable [http://hexxit.wikia.com/wiki/Category:Blocks](http://hexxit.wikia.com/wiki/Category:Blocks)
  NOTE: basically unfixable unless there are enums (static members of Material class) in MCPC+ for 1.5.2 which correspond to these blocks, since CB was forced to start using enums since Mojang was moving on from block IDs to compensate for running out of IDs.
	* Allow blocks from Natura to be inhabitable
		* Autumnal Grass 1260:2
		* Bluegrass (id Unknown)
	* Allow blocks from Artifice to be inhabitable
		* Basalt 1004
		

## Troubleshooting:  
If teleporter does not work or you get any "no world named" errors, you probably need to open your portals.yml or players.yml file in your favorite text editor and replace all instances of your incorrect world name and set them to the actual name of the world you are using (this solution will work if the problem was caused by moving to a new world after already having entries in those files) in the world fields.
