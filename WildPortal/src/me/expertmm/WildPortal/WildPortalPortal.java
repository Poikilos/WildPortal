/**
 * @author Jacob Gustafson
 *
 */
package me.expertmm.WildPortal;

//import java.awt.Rectangle;
//import java.util.List;
//import java.util.ListIterator;





//import javax.print.attribute.standard.Destination;

//import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
//import org.omg.CORBA.PUBLIC_MEMBER;


import com.google.common.base.Strings;

public class WildPortalPortal {
	
	//LOCAL:
	private int x=-143;
	private int y=80;
	private int z=263;
	public String sourceWorldName="world";
	private String destinationWorldName="<this>";
	public String destinationKeyword="<wild>";
	public String allowedPlayerList="<Everyone>";
	private GroundRect destRect = null;
	
	public static String getIDFromLocation(Location location) {
		//NOTE: This is not a universally unique ID, even as applies to server.
		//      It is saved under world name to make it unique.
		return "x" + Integer.toString(location.getBlockX()).replace('-', '_') + 
		       "y" + Integer.toString(location.getBlockX()).replace('-', '_') +
		       "z" + Integer.toString(location.getBlockZ()).replace('-', '_');
	}
	public static void setLocationByID(Location location, String wpid) {
		int x_i=-1;
		int y_i=-1;
		int z_i=-1;
		if (!Strings.isNullOrEmpty(wpid)) {
			x_i = wpid.indexOf("x");
			y_i = wpid.indexOf("y");
			z_i = wpid.indexOf("z");
		}
		if (x_i>=0 && y_i>x_i && z_i>y_i) {
			location.setX((double)Integer.parseInt(wpid.substring(x_i+1, y_i).replace('_', '-')));
			location.setY((double)Integer.parseInt(wpid.substring(y_i+1, z_i).replace('_', '-')));
			location.setZ((double)Integer.parseInt(wpid.substring(z_i+1).replace('_', '-')));
		}
		else {
			if (wpid!=null) main.logWriteLine("ERROR: setLocationByID could not parse '" + wpid + "'");
			else main.logWriteLine("ERROR: setLocationByID could not parse null id");
		}
	}
	public static Location getLocationFromID(World world,
			String wpid) {
		Location location=null;
		int x_i=-1;
		int y_i=-1;
		int z_i=-1;
		if (!Strings.isNullOrEmpty(wpid)) {
			x_i = wpid.indexOf("x");
			y_i = wpid.indexOf("y");
			z_i = wpid.indexOf("z");
		}
		if (x_i>=0 && y_i>x_i && z_i>y_i) {
			double x=(double)Integer.parseInt(wpid.substring(x_i+1, y_i).replace('_', '-'));
			double y=(double)Integer.parseInt(wpid.substring(y_i+1, z_i).replace('_', '-'));
			double z=(double)Integer.parseInt(wpid.substring(z_i+1).replace('_', '-'));
			location=new Location(world,x,y,z);
		}
		else {
			if (wpid!=null) main.logWriteLine("ERROR: setLocationByID could not parse '" + wpid + "'");
			else main.logWriteLine("ERROR: setLocationByID could not parse null id");
		}
		return location;
	}	
    public String getLocationCoords() {
		return String.valueOf(this.x)+","+String.valueOf(this.y)+","+String.valueOf(this.z);
	}
	public boolean equalsWildPortalSource(WildPortalPortal check_wildPortalPortal) {
		return (check_wildPortalPortal.getBlockX()==x) && (check_wildPortalPortal.getBlockY()==y) && (check_wildPortalPortal.getBlockZ()==z) && (check_wildPortalPortal.sourceWorldName.equalsIgnoreCase(sourceWorldName));
	}
	public boolean equalsBlockLocation(int check_X, int check_Y, int check_Z, String check_sourceWorldName) {
		return (check_X==x) && (check_Y==y) && (check_Z==z) && (check_sourceWorldName.equalsIgnoreCase(sourceWorldName));
	}
	public int getBlockX() {
		return x;
	}
	public int getBlockY() {
		return y;
	}
	public int getBlockZ() {
		return z;
	}
	public WildPortalPortal(int setX, int setY, int setZ, String set_sourceWorldName, String set_destinationWorldName, String set_destinationKeyword, int set_X, int set_Y, int set_Width, int set_Depth) {
		this.Set(setX, setY, setZ, set_sourceWorldName, set_destinationWorldName, set_destinationKeyword,set_X,set_Y,set_Width,set_Depth);
	}
	public String getDestinationWorldName() {
		return this.destinationWorldName;
	}
	
	public World getDestinationWorld(Server server, World playerCurrentWorld) {
		//Location returnLocation=null;
		World returnWorld=null;
		if (server!=null) {
			if (playerCurrentWorld!=null) {
				//World world=null;
				if (destinationWorldName.equalsIgnoreCase("<this>")) returnWorld=playerCurrentWorld;
				else returnWorld=server.getWorld(destinationWorldName);
				if (returnWorld!=null) {
					//returnLocation = new Location(world, this.x, this.y, this.z);
				}
				else {
					main.logWriteLine("ERROR: WildPortal getDestinationWorld failed to find a world matching the portal's destination world named \""+this.destinationWorldName+"\")");
				}
			}
			else {
				main.logWriteLine("ERROR: WildPortal needs non-null playerCurrentWorld object to get world object by name (in case value of destinationWorldName field is <this> instead of a world name).");
			}
		}
		else {
			main.logWriteLine("ERROR: WildPortal needs non-null server object to get world object by name.");
		}
		return returnWorld;//return returnLocation;
	}
	public void SetDestAsWild(String set_destinationWorldName, int set_X, int set_Z) {
		this.destinationWorldName=set_destinationWorldName;
		this.destRect=new GroundRect(set_X-this.destRect.width/2, set_Z-this.destRect.depth/2, this.destRect.width, this.destRect.depth);
		this.destinationKeyword="<wild>";
	}
	public void Set(int set_X, int set_Y, int set_Z, String set_sourceWorldName, String set_destinationWorldName, String set_destinationKeyword, int dest_X, int dest_Y, int dest_Width, int dest_Depth) {
		this.x=set_X;
		this.y=set_Y;
		this.z=set_Z;
		this.sourceWorldName=set_sourceWorldName;
		this.destinationWorldName=set_destinationWorldName;
		this.destinationKeyword=set_destinationKeyword;
		this.destRect=new GroundRect(dest_X,dest_Y,dest_Width,dest_Depth);
	}
	//public WildPortalPortal(Location set_blockLocation, String set_destinationWorldName, String set_destinationKeyword) {
	//	this.Set(set_blockLocation, set_destinationWorldName, set_destinationKeyword);
	//}
	//public void Set(Location set_blockLocation, String set_destinationWorldName, String set_destinationKeyword) {
	//	this.x=(int)set_blockLocation.getX();
	//	this.y=(int)set_blockLocation.getY();
	//	this.z=(int)set_blockLocation.getZ();
	//	this.sourceWorldName=set_blockLocation.getWorld().getName();
	//	this.destinationWorldName=set_destinationWorld;
	//	this.destinationKeyword=set_destinationKeyword;
	//}
	
	public boolean IsLikeSource(Location location) {
		return ( (location.getBlockX()==this.x)
				&& (location.getBlockY()==this.y)
				&& (location.getBlockZ()==this.z) 
				&& ( this.sourceWorldName.equalsIgnoreCase(location.getWorld().getName()) )
				);
	}
	public Location getSourceLocation(Server server) {
		Location thisLocation=null;
		if (server!=null) {
			World world=null;
			world=server.getWorld(destinationWorldName);
			if (world!=null) {
				thisLocation = new Location(world, this.x, this.y, this.z);
			}
			else {
				main.logWriteLine("ERROR: WildPortal getLocation failed to find a world matching the portal's destination named \""+this.destinationWorldName+"\")");
			}
		}
		return thisLocation;
	}
	public String toString_SignPointOnly() {
		String result=Integer.toString(this.x)+","+Integer.toString(this.y)+","+Integer.toString(this.z);
		return result;
	}
	public String toString_Destination() {
		return destinationKeyword+" in "+destinationWorldName+" within "+toString_DestRectOnly_AsRangePair();
	}
	public String toString_Destination_center() {
		return destinationKeyword+" in "+destinationWorldName+" around " + 
		       Integer.toString(this.destRect.x+this.destRect.width/2) + "," +
		       Integer.toString(this.destRect.z+this.destRect.depth/2);
	}
	public String toString_DestRectOnly_AsRangePair() {
		String returnString = "";
		if (this.destRect!=null) {
			returnString=this.destRect.getLeft()+" to "+this.destRect.getRightInclusive()
					+","+this.destRect.getTop()+" to "+this.destRect.getBottomInclusive();
		}
		return returnString;
	}
	public static WildPortalPortal getFromConfigurationSection(ConfigurationSection cs, String SayErrorWasInWhatSourceFileName) {
		WildPortalPortal result = null;
		Boolean HasAll=true;
		int sourceX=Integer.MIN_VALUE;
		int sourceY=Integer.MIN_VALUE;
		int sourceZ=Integer.MIN_VALUE;
		String sourceWorldName=null;
		int destX=Integer.MIN_VALUE;
		int destY=Integer.MIN_VALUE;
		int destWidth=Integer.MIN_VALUE;
		int destDepth=Integer.MIN_VALUE;
		if (!cs.contains("source.x")) HasAll=false;
		else sourceX=cs.getInt("source.x");
		if (!cs.contains("source.y")) HasAll=false;
		else sourceY=cs.getInt("source.y");
		if (!cs.contains("source.z")) HasAll=false;
		else sourceZ=cs.getInt("source.z");
		if (!cs.contains("source.world")) HasAll=false;
		else sourceWorldName=cs.getString("source.world");
		if (!cs.contains("destination.x")) HasAll=false;
		else destX=cs.getInt("destination.x");
		if (!cs.contains("destination.z")) HasAll=false;
		else destY=cs.getInt("destination.z");
		if (!cs.contains("destination.width")) HasAll=false;
		else destWidth=cs.getInt("destination.width");
		if (!cs.contains("destination.depth")) HasAll=false;
		else destDepth=cs.getInt("destination.depth");
		if (HasAll) {
			if (!cs.contains("destination.world")) {
				String destinationWorldName=cs.getString("destination.world");
				String destinationKeyword=null;
				if (cs.contains("destination.keyword")) destinationKeyword=cs.getString("destination.keyword");
				else HasAll=false;
				result = new WildPortalPortal(sourceX,sourceY,sourceZ,sourceWorldName, destinationWorldName, destinationKeyword, destX, destY, destWidth, destDepth);
			}
			else {
				HasAll=false;
				main.logWriteLine(" was read missing in "+SayErrorWasInWhatSourceFileName);
			}
		}
		else {
			main.logWriteLine("ERROR: Incomplete source or destination in "+SayErrorWasInWhatSourceFileName);
		}		
		return result;
	}
	public int getDestXMin() {
		return (this.destRect!=null)?this.destRect.getLeft():0;
	}
	public int getDestZMin() {
		// TODO Auto-generated method stub
		return (this.destRect!=null)?this.destRect.getTop():0;
	}
	public int getDestXMax() {
		// TODO Auto-generated method stub
		return (this.destRect!=null)?this.destRect.getRightInclusive():0;
	}
	public int getDestZMax() {
		// TODO Auto-generated method stub
		return (this.destRect!=null)?this.destRect.getBottomInclusive():0;
	}

}
