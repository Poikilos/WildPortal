package me.expertmm.WildPortal;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

public class WildPortalPortal {
	public int X=-143;
	public int Y=80;
	public int Z=263;
	public String sourceWorld="<this>";
	public String destinationWorld="<this>";
	public String destinationKeyword="<wild>";
	public String allowedPlayerList="<Everyone>";
	
	public WildPortalPortal(Location set_blockLocation, String set_sourceWorldName, String set_destinationWorld, String set_destinationKeyword) {
		this.Set(set_blockLocation, set_sourceWorldName, set_destinationWorld, set_destinationKeyword);
	}
	
	public boolean IsLike(Location location) {
		return ( ((int)location.getX()==this.X)
				&& ((int)location.getY()==this.Y)
				&& ((int)location.getZ()==this.Z) );
	}
	public Location getLocation(Server server) {
		Location thisLocation=null;
		if (server!=null) {
			World world=server.getWorld(destinationWorld);
			if (world!=null) {
				thisLocation = new Location(world, this.X, this.Y, this.Z);
			}
			else {
				main.logWriteLine("ERROR: WildPortal getLocation failed to find a world matching the portal's destination named \""+this.destinationWorld+"\")");
			}
		}
		return thisLocation;
	}
	public void Set(Location set_blockLocation, String set_sourceWorldName, String set_destinationWorld, String set_destinationKeyword) {
		this.X=(int)set_blockLocation.getX();
		this.Y=(int)set_blockLocation.getY();
		this.Z=(int)set_blockLocation.getZ();
		this.sourceWorld=set_sourceWorldName;
		this.destinationWorld=set_destinationWorld;
		this.destinationKeyword=set_destinationKeyword;
	}
}
