![WildPortal](http://expertmultimedia.com/downloads/images/wildportal-logo-square-small-padded-3xW.png)
**This file is an incomplete overview. Please see README.md in main repository folder for full and up-to-date details.**
## Description  
WildPortal lets you create signs on your server that, when clicked, teleport people to a safe block where they will not be smothered, anywhere in the world within a given range.  

Why another "wild" teleporter? There is a lack of compatible wild teleporters that work on Tekkit Classic Server. One was written in Skript, but I couldn't get Skript to work properly on Tekkit Classic server. Potentially many could be recompiled against Tekkit.jar and work, however people often release the plugin as closed-source and do not. Soon after, I added Java to the list of languages I program (after getting the plugin development environment setup thanks to _Espressobean_), then created this plugin!  

## Usage
* Click a "special" sign to teleport into a "wild" area of the map (future plan is to avoid protected regions automatically).  
* WildPortal allows op or user with wildportal.manage permission to make signs into wild (random safe location) teleporters by typing  
**/wildportal** (then clicking a standing or wall-mounted sign). You can remove the WildPortal from the sign using
**/wildportal remove** (then clicking the sign--the command will not destroy the sign)  
* Also, op or users with wildportal.return permission can return to the location they were last by typing
**/wildportal return**  (this is a this per-user location. As of 2014-10-25 the per-user return location is not available across server restarts unless manager). A "Last Created" sign location is saved to config.yml, in case manager wants to return after server restart (any manager is allowed to return to the per-world "Last Created" sign location which is stored in _**config.yml**_).  

## Technical Details  
For updated feature list and future tasks, please see the [README.md](https://github.com/expertmm/WildPortal/blob/master/README.md) file in the WildPortal master branch on GitHub.  
### Data files:  
plugins/WildPortal/_**config.yml**_  
plugins/WildPortal/_**players.yml**_  
plugins/WildPortal/_**portals.yml**_  
* These are saved whenever data is added or changes, therefore if you manually make changes to these files using your favorite editor, then type /wildportal reload (or edit when server is down) otherwise it may overwrite your changes.  
### Major differences in 1.2.5.1 version:
(these specifics do not apply to later releases)
* To manually edit the files, server must be down
* Player wild location is not saved (is generated again if they click the sign again)
* (in 1.2.5.1, the only way to change the "wild" region's top, left, width, and height is by editing the csv file)  
* WildPortal loads the CSV file and config file at startup then saves either when necessary, such as when a WildPortal is added to or removed from a sign. You could edit live if you don't make changes, such as editing WildPortal.csv (then restart the server before making any further changes so your csv isn't overwritten) to change the Location of the portal to the location of another sign.
* Config files are:
plugins/WildPortal/_**config.yml**_  
plugins/WildPortal/_**WildPortal.csv**_  

###Security 
Protecting the sign with WorldGuard or other method is suggested, but after one touch the person will be teleported, so if you don't use WorldGuard you could enclose the sign in a room made of bedrock to prevent the sign or the block it is on from being blown up from outside. If the sign is broken and placed again, it will still be a WildPortal and have the same settings.
