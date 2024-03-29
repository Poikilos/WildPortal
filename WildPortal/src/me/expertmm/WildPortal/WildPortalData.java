package me.expertmm.WildPortal;

import java.io.File;
//import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;



//import java.util.Iterator;
//import java.util.ListIterator;
//import java.util.Set;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class WildPortalData {
	
	private File dataFolder;
	public File playersFile;
	public FileConfiguration players;
	public File portalsFile;
	public FileConfiguration portals;
	//public WildPortalPortal getWildPortalAt(Location location) {
	//	WildPortalPortal result=null;
	//	if (portals.contains(location.getWorld().getName() + "." + WildPortalPortal.getIDFromLocation(location))) {
	//		
	//	}
	//	return result;
	//}	
	//public void deletePortalData(Location location) {
	//	portals.set(WildPortalPortal.getIDFromLocation(location), null);
	//}	
	//public Location getDestionationIfPortalElseNull(Location location) {
	//	Location result=null;
	//	if (isPortal(location)) {
	//		
	//	}
	//	return result;
	//}
	public WildPortalData(File thisDataFolder) {
		this.dataFolder=thisDataFolder;
		try {
			init();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	    load();
	}
	private void init() throws Exception {
		playersFile = new File(this.dataFolder, "players.yml");
		
		if (!playersFile.exists()) {
			playersFile.getParentFile().mkdirs();
	        //copy(getResource("groups.yml"), playersFile);
	    }
	    players = new YamlConfiguration();
		portalsFile = new File(this.dataFolder, "portals.yml");
		if (!portalsFile.exists()) {
			portalsFile.getParentFile().mkdirs();
	        //copy(getResource("groups.yml"), playersFile);
	    }
	    portals = new YamlConfiguration();
	}
	public void load() {
		try {
	        players.load(playersFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		try {
	        portals.load(portalsFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void save() {
	    try {
	    	players.save(playersFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    try {
	    	portals.save(portalsFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public void createPortalToWild(Location location, String destinationKeyword, GroundRect destGroundRect) {
		int sourceX=location.getBlockX();
		int sourceY=location.getBlockY();
		int sourceZ=location.getBlockZ();
		String sourceWorldName=location.getWorld().getName();
		String wpid=WildPortalPortal.getIDFromLocation(location);
		portals.set(sourceWorldName + "." + wpid + ".source.x", sourceX);
		portals.set(sourceWorldName + "." + wpid + ".source.y", sourceY);
		portals.set(sourceWorldName + "." + wpid + ".source.z", sourceZ);
		portals.set(sourceWorldName + "." + wpid + ".source.world", sourceWorldName);
		portals.set(sourceWorldName + "." + wpid + ".destination.world", destGroundRect.worldName);
		portals.set(sourceWorldName + "." + wpid + ".destination.keyword", destinationKeyword);
		portals.set(sourceWorldName + "." + wpid + ".destination.x", destGroundRect.x);
		portals.set(sourceWorldName + "." + wpid + ".destination.z", destGroundRect.z);
		portals.set(sourceWorldName + "." + wpid + ".destination.width", destGroundRect.width);
		portals.set(sourceWorldName + "." + wpid + ".destination.depth", destGroundRect.depth);
		portals.set(sourceWorldName + "." + wpid + ".destination.yaw", destGroundRect.yaw);
		List<String> allowedPlayerList = Arrays.asList("<Everyone>");  // use comma for more
		portals.set(sourceWorldName + "." + wpid + ".allowedPlayerList", allowedPlayerList);
		save();
	}
	public void setPortalDestination(String sourceWorldName, Location sourceLocation, GroundRect rect) {
		portals.set(sourceWorldName + "." + WildPortalPortal.getIDFromLocation(sourceLocation) + ".destination.x", rect.x);
		portals.set(sourceWorldName + "." + WildPortalPortal.getIDFromLocation(sourceLocation) + ".destination.z", rect.z);
		portals.set(sourceWorldName + "." + WildPortalPortal.getIDFromLocation(sourceLocation) + ".destination.width", rect.width);
		portals.set(sourceWorldName + "." + WildPortalPortal.getIDFromLocation(sourceLocation) + ".destination.depth", rect.depth);
		portals.set(sourceWorldName + "." + WildPortalPortal.getIDFromLocation(sourceLocation) + ".destination.world", rect.worldName);
		save();
	}
	public Boolean setPortalDestinationCenter(Location location, int newCenterX, int newCenterZ) {
		Boolean result=false;
		String wpid=WildPortalPortal.getIDFromLocation(location);
		if (portals.contains(location.getWorld().getName() + "." + wpid + ".destination")) {
			result=setPortalDestinationCenter(location.getWorld().getName(), wpid, newCenterX, newCenterZ);
			//setPortalDestinationCenter DOES automatically save
		}
		else {
			main.logWriteLine("setPortalDestinationCenter ERROR: no portal is at "+location.toString());
		}
		return result;
	}
	public Boolean setPortalDestinationCenter(String worldName, String wpid, int newCenterX, int newCenterZ) {
		Boolean result=false;
		if (portals.contains(worldName + "." + wpid + ".destination")) {
			int width = portals.getInt(worldName + "." + wpid + ".destination.width");
			int depth = portals.getInt(worldName + "." +wpid + ".destination.depth");
			portals.set(worldName + "." + wpid + ".destination.x", newCenterX-width/2);
			portals.set(worldName + "." + wpid + ".destination.z", newCenterZ-depth/2);
			portals.set(worldName + "." + wpid + ".destination.width", width);
			portals.set(worldName + "." + wpid + ".destination.depth", depth);
			save();
			result=true;
		}
		else {
			main.logWriteLine("setPortalDestinationCenter ERROR: no portal is at worldName."+wpid);
		}
		return result;
	}
	public Boolean setPortalDestinationWidth(String worldName, String wpid, int newSize) {
		Boolean result=false;
		if (portals.contains(worldName + "." + wpid + ".destination")) {
			int oldSize = portals.getInt(worldName + "." + wpid + ".destination.width");
			int center = portals.getInt(worldName + "." + wpid + ".destination.x") + oldSize/2;
			portals.set(worldName + "." + wpid + ".destination.x", center-newSize/2);
			portals.set(worldName + "." + wpid + ".destination.width", newSize);
			save();
			result=true;
		}
		else {
			main.logWriteLine("setPortalDestinationCenter ERROR: no portal is at worldName."+wpid);
		}
		return result;
	}	
	public Boolean setPortalDestinationDepth(String worldName, String wpid, int newSize) {
		Boolean result=false;
		if (portals.contains(worldName + "." + wpid + ".destination")) {
			int oldSize = portals.getInt(worldName + "." + wpid + ".destination.depth");
			int center = portals.getInt(worldName + "." + wpid + ".destination.z") + oldSize/2;
			portals.set(worldName + "." + wpid + ".destination.z", center-newSize/2);
			portals.set(worldName + "." + wpid + ".destination.depth", newSize);
			save();
			result=true;
		}
		else {
			main.logWriteLine("setPortalDestinationCenter ERROR: no portal is at worldName."+wpid);
		}
		return result;
	}
	public void setPlayerReturnLocation(String playerName, Location location) {
		//NOTE: this is a cross-world configuration--player can only have one home on server.
		players.set(playerName + ".returnLocation.world", location.getWorld().getName());
		players.set(playerName + ".returnLocation.x", location.getX());
		players.set(playerName + ".returnLocation.y", location.getY());
		players.set(playerName + ".returnLocation.z", location.getZ());
		players.set(playerName + ".returnLocation.yaw", location.getYaw());
		save();
	}
	public Boolean deletePortalData(String worldName, String wpid) {
		Boolean result=false;
		String path=worldName+"."+wpid;
		if (portals.contains(path)) {
			portals.set(path, null);
			result=true;
			save();
		}
		return result;
	}
	public Boolean deletePlayerData(String playerName) {
		Boolean result=false;
		if (players.contains(playerName)) {
			players.set(playerName, null);
			result=true;
			save();
		}
		return result;
	}

	public Boolean isPortal(Location location) {
		return portals.contains(location.getWorld().getName() + "." + WildPortalPortal.getIDFromLocation(location));
	}
	/*
	public Location getPlayerReturnLocation(String playerName, Server server) {
		Location location=null;
		if (players.contains(playerName + ".returnLocation")) {
			double x=players.getDouble(playerName + ".returnLocation.x");
			double y=players.getDouble(playerName + ".returnLocation.y");
			double z=players.getDouble(playerName + ".returnLocation.z");
			String worldName=players.getString(playerName + ".returnLocation.world");
			location=new Location(server.getWorld(worldName), x, y, z);
		}
		return location;//?(players.get(playerName + ".returnLocation")):null;
	}
	*/
	public Location getPlayerReturnLocation(String playerName, Server thisServer) {
		Location location=null;
		if (players.contains(playerName + ".returnLocation")) {
			String worldName = players.getString(playerName + ".returnLocation.world");
			World world = thisServer.getWorld(worldName);
			double x = players.getDouble(playerName + ".returnLocation.x");
			double y = players.getDouble(playerName + ".returnLocation.y");
			double z = players.getDouble(playerName + ".returnLocation.z");
			float yaw = (float)players.getDouble(playerName + ".returnLocation.yaw");
			location = new Location(world, x, y, z);
			location.setYaw(yaw);
		}
		return location;
	}	
	public GroundRect getPortalDestination(String worldName, Location location) {
		String wpid=WildPortalPortal.getIDFromLocation(location);
		return getPortalDestination(worldName, wpid);
	}
	public GroundRect getPortalDestination(String worldName, String wpid) {
		GroundRect result=null; //new GroundRect(0, 0, 0, 0);
		if ((wpid!=null) && portals.contains(worldName + "." + wpid + ".destination")) {
			int x = portals.getInt(worldName + "." + wpid + ".destination.x");
			int z = portals.getInt(worldName + "." + wpid + ".destination.z");
			int width = portals.getInt(worldName + "." + wpid + ".destination.width");
			int depth = portals.getInt(worldName + "." + wpid + ".destination.depth");
			result = new GroundRect(x,z, width, depth);
			result.yaw=(float)portals.getDouble(worldName + "." + wpid + ".destination.yaw");
			result.key=portals.getString(worldName + "." + wpid + ".destination.keyword");
			String destWorldName=portals.getString(worldName + "." + wpid + ".destination.world");
			if (destWorldName==null) destWorldName="<this>";
			if (destWorldName.equals("<this>")) destWorldName=worldName;
			result.worldName=destWorldName;
		}
		else {
			main.logWriteLine("getPortalDestination ERROR: no portal is at block " + worldName + "."+((wpid!=null)?wpid:"null"));
		}
		return result;
	}
	public Location getDestinationCenterIfPortalElseNull(World world, String wpid, Server server) {
		Location result=null;
		if (portals.contains(world.getName() + "." + wpid)) {
			//result=WildPortalPortal.getLocationFromID(world, wpid);
			GroundRect rect=getPortalDestination(world.getName(), wpid);
			if (rect!=null) result=new Location(server.getWorld(rect.worldName), rect.getCenterX(), 64.0, rect.getCenterZ());
		}
		return result;
	}
	public ConfigurationSection getPortalSectionByID(String worldName, String wpid) {
		ConfigurationSection result=null;
		//WildPortalPortal result=null;
		ConfigurationSection parent = this.portals.getConfigurationSection(worldName);
		//Set<String> items = parent.getKeys(false);
		//for (Iterator<String> iter = items.iterator(); iter.hasNext(); ) {
		//	String elementID = iter.next();
		//	if (elementID.equals(wpid)) {
		//		result = parent.getConfigurationSection(elementID);
		//		break;
		//	}
		//}
		if (parent.contains(wpid)) {
			result = parent.getConfigurationSection(wpid);
		}
		return result;
	}
}
