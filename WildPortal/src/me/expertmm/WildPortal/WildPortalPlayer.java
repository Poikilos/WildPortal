package me.expertmm.WildPortal;

import org.bukkit.Location;

public class WildPortalPlayer {
	public String playerName=null;
	public Location returnLocation = null;
	
	public WildPortalPlayer(String set_playerName, Location set_returnLocation) {
		this.playerName=set_playerName;
		this.returnLocation=set_returnLocation;
	}
}
