package me.expertmm.WildPortal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



//utilities:
import java.util.Random;
//import java.util.Collection;
import java.util.List;
import java.util.ArrayList;//implements list (instantiate List objects with this)
import java.util.ListIterator;

//for handling events other than commands, you need a listener (other class)
public class main extends JavaPlugin { 

	//static Player thisPlayer = null;
	static List<Location> authorizedSignLocationList = null;
	static int LastUsedBlockID = -1;
	static int LastUsedY = -1;
	
	static int StartAboveGroundByInt = 4;
	
	static int defaultX=-142;
	static int defaultY=80;
	static int defaultZ=263;
	
	//TEKKIT block ids:
	static int blockid_air=0;
	static int blockid_stone=1;
	static int blockid_grass=2;
	static int blockid_flowing_water=8;
	static int blockid_water=9;
	static int blockid_flowing_lava=10;
	static int blockid_lava=11;
	static int blockid_sand=12;
	static int blockid_web=30;
	static int blockid_tnt=46;
	static int blockid_fire=51;
	static int blockid_ice=79;
	static int blockid_snow=80;
	static int blockid_mycel=110;
	static int blockid_standingsign=63;
	static int blockid_wallsign=68;
	static Location LastCreatedSignLocation = null;
	
	
	// Minecraft 1.8:			
	//int blockid_tripwire=132;
	//int blockid_barrier=166;//slashed circle or not shown
	
	
	public void onEnable (){
		getLogger().info("WildPortal Enabled!");
		//PluginManager pm = this.getServer().getPluginManager();
        //pm.registerEvents(PlayerInteractEvent, this);
		getLogger().info("Since creating portals is not yet implemented, a standing sign or wall-mounted sign (it can say anything) at "+String.valueOf(defaultX)+","+String.valueOf(defaultY)+","+String.valueOf(defaultZ)+" (which would otherwise be the empty air block in front of the wall block) will be the only WildPortal allowed.");
	}
	
	/* 
	 below is from  Plo124. "On punch block, send a message?" Bukkit.org. <https://forums.bukkit.org/threads/on-punch-block-send-a-message.177435/> 25 Sep 2013. 21 Oct 2014.
	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(event.getMaterial() == Material.GOLD_BLOCK && e.getAction() == Action.LEFT_CLICK_BLOCK){
			player.sendMessage("Stop breaking the budder >:D")
		}
	}
	*/
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		PluginManager plg = Bukkit.getPluginManager(); //Assigns plg to Bukkit.getPluginManager()
		Plugin plgname = plg.getPlugin("WildPortal");
		Player player = ((Player) sender);
		//thisPlayer = player;
		World world = player.getWorld();
		
		if (authorizedSignLocationList==null) {
			LastCreatedSignLocation = new Location(world,(double)defaultX,(double)defaultY,(double)defaultZ);
			authorizedSignLocationList=new ArrayList<Location>(); //because ArrayList is a class which implements the List interface, List objects are instantiated as ArrayList like this.
			authorizedSignLocationList.add(LastCreatedSignLocation);
			//TODO: load some kind of actual list
		}
		
		if (cmd.getName().equalsIgnoreCase("wildportal")) { // If the player typed /wildportal then do the following...
			if(args.length==0){sender.sendMessage(ChatColor.AQUA + "For help, use /wildportal help");}
			if (sender instanceof Player) {
				doWildPortal(player,world);
			}
			else {
				sender.sendMessage("WildPortal: I think the SYSTEM is talking to me.");
			}
		}
		//TODO: save sign list in case any were added
		return true;
	}
	public static void doWildPortal(Player player, World world) {
		int minWorldX=-314; //TODO: find world border or something
		int minWorldZ=70; //TODO: find world border or something
		int maxWorldX=-151; //TODO: find world border or something
		int maxWorldZ=250; //TODO: find world border or something
		int rangeX=maxWorldX-minWorldX;
		int rangeZ=maxWorldZ-minWorldZ;
		Random rnd = new Random();
		int randomCountingNumberX=rnd.nextInt(rangeX+1);//+1 since exclusive
		int randomCountingNumberZ=rnd.nextInt(rangeZ+1);//+1 since exclusive
		int randomIntX=randomCountingNumberX+minWorldX;//allows possibly negative (if minimum is negative)
		int randomIntZ=randomCountingNumberZ+minWorldZ;//allows possibly negative (if minimum is negative)
		
		Location sourceLocation = player.getLocation();
		
		player.sendMessage(player.getName() + " is being born in "+world.getName()+"..."); //TODO: send to all players
		player.setNoDamageTicks(60);
		//Location destLocation = new Location(world, 0,68,0);
		//Location destLocation = new Location(world, sourceLocation.getX(), sourceLocation.getY()+20.0, sourceLocation.getZ() );
		Location randomLocation = new Location(world, (double)randomIntX,64.0,(double)randomIntZ);
		double minY=49.0;
		LastUsedBlockID=-1;
		LastUsedY=-1;
		Location checkedLocation = getLocationOnGround(world, randomLocation, minY, player);
		if (checkedLocation.getY()>=minY) {
			checkedLocation.setY(checkedLocation.getY()+(double)main.StartAboveGroundByInt);
			player.teleport(checkedLocation);
			player.sendMessage(player.getName() + " has arrived."); //TODO: send to all players.
			player.sendMessage("WildPortal destination was: "+checkedLocation.toString()); //TODO: remove this
		}
		else {
			player.sendMessage("WildPortal: Uh oh, portal couldn't find a suitable location for you yet--tried above ("+String.valueOf(checkedLocation.getX() )+","+String.valueOf(checkedLocation.getY())+","+String.valueOf(checkedLocation.getZ())+") [last Y:"+String.valueOf(LastUsedY)+"; last used block id:"+String.valueOf(LastUsedBlockID)+"]. Please try again.");
		}

	}
	public void onDisable() {
		getLogger().info("WildPortal Disabled");
	}
	
	private static Location getLocationOnGround(World world, Location tryLocation_XandZ, double minY, Player player) {
		//tryLocation_XandZ.setY(255);
		int tryX=(int)tryLocation_XandZ.getX();
		int tryY=255;
		int tryZ=(int)tryLocation_XandZ.getZ();
		LastUsedBlockID=-1;
		player.sendMessage("...");//TODO: remove this
		//TODO: prevent ending up back in spawn area, so "being born" message only happens once.
		player.sendMessage("Finding a place for "+player.getName() + " in "+world.getName()+"..."); //TODO: remove this (plays every TRY not just every birth)
		while (tryY>=minY) {
			LastUsedY=tryY;
			int thisBlockTypeID=world.getBlockTypeIdAt(tryX,tryY,tryZ);
			
			
			if (thisBlockTypeID==blockid_grass
					|| thisBlockTypeID==blockid_sand
					|| thisBlockTypeID==blockid_ice
					|| thisBlockTypeID==blockid_snow
					|| thisBlockTypeID==blockid_mycel
					) {
				LastUsedBlockID=thisBlockTypeID;
				break;
			}
			else if (
					thisBlockTypeID==blockid_flowing_lava
					|| thisBlockTypeID==blockid_lava
					|| thisBlockTypeID==blockid_flowing_water
					|| thisBlockTypeID==blockid_water
					|| thisBlockTypeID==blockid_tnt
					|| thisBlockTypeID==blockid_web
					|| thisBlockTypeID==blockid_fire
					/*
				minecraft 1.7+
				|| thisBlockTypeID==blockid_tripwire
				minecraft 1.8+
				|| thisBlockTypeID==blockid_barrier
				*/
					) {
				LastUsedBlockID=thisBlockTypeID;
				//Double.toString(tryX)
				if (player!=null) {
					player.sendMessage("WildPortal: Skipping a potentially covered area because of dangerous block (ID "+String.valueOf(LastUsedBlockID)+") at ("+String.valueOf(tryX)+","+String.valueOf(tryY)+","+String.valueOf(tryZ)+")...");//TODO: remove this
				}
				tryY=0;//forces "not found" condition
				break;
			}
			//TODO: also make sure it is "level" ground (check for 9x9 area with only levels only differing by up to 1.0)
			tryY-=1.0;
		}
		if (tryY<minY) tryLocation_XandZ.setY(0.0);//0.0 is a FLAG that there is no ground here
		else tryLocation_XandZ.setY(tryY);
		return tryLocation_XandZ;
	}
}