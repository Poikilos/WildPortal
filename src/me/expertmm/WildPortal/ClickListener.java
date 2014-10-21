package me.expertmm.WildPortal;

//for clicking:
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;

//public class ClickListener implements Listener {
public class ClickListener extends JavaPlugin implements Listener {
	@EventHandler
	//public boolean onClickEvent(PlayerInteractEvent event) {
	public void onInteractEvent(PlayerInteractEvent event) {
		boolean IsGood=true;
		Player player = event.getPlayer();
		World world = player.getWorld();
		Action action = event.getAction();
		
		if (action==Action.LEFT_CLICK_BLOCK) {
			Block clickedBlock = event.getClickedBlock();
			Location clickedLocation = clickedBlock.getLocation();
			if (clickedBlock.getTypeId()==main.blockid_wallsign
				|| clickedBlock.getTypeId()==main.blockid_standingsign) {
				boolean IsInList=false;
				if (main.authorizedSignLocationList!=null) {
					for (ListIterator<Location> iter = main.authorizedSignLocationList.listIterator(); iter.hasNext(); ) {
					    Location element = iter.next();
					    // 1 - can call methods of element
					    // 2 - can use iter.remove() to remove the current element from the list
					    // 3 - can use iter.add(...) to insert a new element into the list
					    //     between element and iter->next()
					    // 4 - can use iter.set(...) to replace the current element
					    //thanks Dave Newton. http://stackoverflow.com/questions/18410035/ways-to-iterate-over-a-list-in-java
					    // ...
					    if (clickedLocation.equals(element)) {
					    	IsInList=true;
					    }
					}
					if (IsInList) {
						player.sendMessage("You touched a WildPortal"); //TODO: remove this
						main.doWildPortal(player,world);
					}
					else {
						player.sendMessage("That sign is not a WildPortal"); //TODO: remove this
						//TODO: getLogger().info("WildPortal onClickEvent: the sign is not a WildPortal (this is normal for when anyone clicks a sign unless it was supposed to be a WildPortal)"); //TODO: remove this line
					}
				}
				else {
					player.sendMessage("WildPortal onClickEvent: ERROR--the sign location list is not loaded.");
					//TODO: getLogger().info("WildPortal onClickEvent: ERROR--the sign location list is not loaded."); //TODO: remove this line
				}
			}//if standing sign or wall sign
			else {
				player.sendMessage("You clicked block ID "+String.valueOf(clickedBlock.getTypeId())); //TODO: remove this
			}
		}//else not a block click action (may be action==Action.LEFT_CLICK_AIR)
		//return IsGood;
	}//end onInteractEvent
}//end class ClickListener
