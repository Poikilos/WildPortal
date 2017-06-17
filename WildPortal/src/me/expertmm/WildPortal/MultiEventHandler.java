/**
 * @author Jacob Gustafson
 *
 */
package me.expertmm.WildPortal;

/*

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
*/
//this paradigm requires:
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.event.player.PlayerQuitEvent; //if you want to do public void onQuit9PlayerQuitEvent event) {}
import org.bukkit.configuration.file.FileConfiguration;

import me.expertmm.WildPortal.WildPortalPortal;







//this specific plugin requires:
//import org.bukkit.Server;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.Plugin;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
//import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
//import java.io.StringWriter;
//utilities:
import java.util.Random;
import java.util.List;
import java.util.ArrayList;//implements list (instantiate List objects with this)
import java.util.ListIterator;
import java.util.logging.Logger;


public class MultiEventHandler implements CommandExecutor, Listener {
//public class ClickListener extends JavaPlugin implements Listener {
	
	//NOTE: you canNOT do getConfig from here, nor make this extend JavaPlugin
	
	//Thanks to Both class by JPG2000F on forums.bukkit.org
	
	//static Player thisPlayer = null;
	public static String myNameAndVersion="WildPortal 2014-10-25";
	private main plugin = null; //public main InstanceOfMain = null; 
	private Logger thisLogger = null;
	private Server thisServer = null;
	private FileConfiguration config = null;
	private WildPortalPortal LastCreatedWildPortal = null;
	private Location LastCreatedWildPortalFromPlayerLocation = null;
	
	private static String ConfigVariable_LastCreatedInWorld_VariableName="LastCreatedInWorld";
	
	public List<String> aboutToMakeTeleporterWithNextClickPlayerList = null;
	public List<String> aboutToBreakTeleporterWithNextClickPlayerList = null;
	private List<WildPortalPortal> wildportalList = null; //static List<Location> authorizedSignLocationList = null;
	private List<WildPortalPlayerData> wildportalplayerdataList = null;
	private static String AuthorDebugWorldBeingUsedByPlayerName="Abiyahh";
	//public static String AuthorDebugWorld="WorldLand";
	private static String csvFileFullName = null;
	private static String csvFileName="WildPortal.csv";
	private static String ConfigVariable_IsVerbose_VariableName="Debug.IsVerbose";
	
	static int LastUsedBlockID = -1;
	static int LastUsedY = -1;
	static int StartAboveGroundByInt = 10;
	
	
	//TEKKIT block ids:
	static boolean IsVerbose=false;
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
	//static Location LastCreatedSignLocation = null;


	// Minecraft 1.7+:			
	//int blockid_tripwire=132;
	// Minecraft 1.8+(or so):
	//int blockid_barrier=166;//slashed circle or not shown
	
	
	//constructor
    public MultiEventHandler() {
    	String participle="before initializing";
    	try {
    		participle="getting plugin";
	    	this.plugin=main.getPlugin();
	    	participle="getting server";
	    	thisServer=plugin.getServer();
	    	participle="getting logger";
	    	thisLogger=plugin.getLogger();
	    	thisLogger.info("Loading "+myNameAndVersion+"...");
	    	participle="getting config";
	    	config=plugin.getConfig();
	    	
	    	participle="getting plugin data folder";
	    	File dataFolder = plugin.getDataFolder();
	    	participle="making plugin directory";
	    	dataFolder.mkdirs();
	    	//participle="getting plugin data directory";
	    	//File folderOfThisPluginFullName=new File(dataFolder.getAbsolutePath()+"\\"+csvFileName);//FAILS: new File(dataFolder.getPath()+"/"+csvFileName);
	    	//participle="making plugin data directory";
	    	//folderOfThisPluginFullName.mkdirs();
	    	participle="generating full csv file path";
	    	csvFileFullName=dataFolder.getAbsolutePath() +"/"+csvFileName;
	    	participle="checking config";
	    	if (config!=null) {
				//main.config.addDefault(ConfigVariable_IsVerbose_VariableName, "False");
	    		if (!config.contains(ConfigVariable_IsVerbose_VariableName)) config.set(ConfigVariable_IsVerbose_VariableName, IsVerbose);
	    		//LastCreatedWildPortal stuff is loaded from a player so that context of world is used
				MultiEventHandler.IsVerbose=config.getBoolean(ConfigVariable_IsVerbose_VariableName);
	    	}
	    	else {
	    		main.logWriteLine("ERROR: MultiEventHandler constructor could not load config variables because main.config was null");
	    	}
	    	participle="making lists";
			if (aboutToMakeTeleporterWithNextClickPlayerList==null) aboutToMakeTeleporterWithNextClickPlayerList=new ArrayList<String>();
			if (aboutToBreakTeleporterWithNextClickPlayerList==null) aboutToBreakTeleporterWithNextClickPlayerList=new ArrayList<String>();
	    	participle="loading data";
			doLoadWildPortalData();
    	}
    	catch (Exception e) {
    		this.thisLogger.info("Could not finish "+participle+" in MultiEventHandler constructor: "+e.getMessage());
    	}
    }
    
    private String ConfigVariable_LastCreated_CategoryName="LastCreatedWildPortal";//world name
    private String ConfigVariable_LastCreatedFromWorld_SubCategoryName="<this>";//world name
    private String ConfigVariable_LastCreatedFromX_VariableName="LastCreatedFromX";
    private String ConfigVariable_LastCreatedFromY_VariableName="LastCreatedFromY";
    private String ConfigVariable_LastCreatedFromZ_VariableName="LastCreatedFromZ";
    
    public void SaveOptionsToConfig() {
    	if (plugin!=null) {
    		if (config!=null) {
    			if (LastCreatedWildPortalFromPlayerLocation!=null) {
    				config.set(ConfigVariable_LastCreated_CategoryName+"."+ConfigVariable_LastCreatedInWorld_VariableName, LastCreatedWildPortalFromPlayerLocation.getWorld().getName());
    				ConfigVariable_LastCreatedFromWorld_SubCategoryName=LastCreatedWildPortalFromPlayerLocation.getWorld().getName();
    				config.set(ConfigVariable_LastCreated_CategoryName+"."+ConfigVariable_LastCreatedFromWorld_SubCategoryName+"."+ConfigVariable_LastCreatedFromX_VariableName, LastCreatedWildPortalFromPlayerLocation.getBlockX());
    				config.set(ConfigVariable_LastCreated_CategoryName+"."+ConfigVariable_LastCreatedFromWorld_SubCategoryName+"."+ConfigVariable_LastCreatedFromY_VariableName, LastCreatedWildPortalFromPlayerLocation.getBlockY());
    				config.set(ConfigVariable_LastCreated_CategoryName+"."+ConfigVariable_LastCreatedFromWorld_SubCategoryName+"."+ConfigVariable_LastCreatedFromZ_VariableName, LastCreatedWildPortalFromPlayerLocation.getBlockZ());
    			}
    			config.set(ConfigVariable_IsVerbose_VariableName, IsVerbose);
    		}
    		else if (thisLogger!=null) thisLogger.info("ERROR in SaveOptionsToConfig: config was null");
    		plugin.saveConfig(); //main.saveThisConfig();
    	}
    	else {
    		if (thisLogger!=null) thisLogger.info("ERROR in SaveOptionsToConfig: plugin was null!");
    	}
    }
	
	public boolean IsPlayerListLoaded() {
		return wildportalplayerdataList!=null;
	}
	private void doLoadPlayerList() {
		if (wildportalplayerdataList==null) wildportalplayerdataList = new ArrayList<WildPortalPlayerData>();
	}
	public Location getReturnLocationForPlayerElseNull(String find_playerName, boolean IsToBeRemoved) {
		Location returnLocation=null;
		if (!IsPlayerListLoaded()) doLoadPlayerList();
		for (ListIterator<WildPortalPlayerData> iter = wildportalplayerdataList.listIterator(); iter.hasNext(); ) {
			WildPortalPlayerData element = iter.next();
		    // 1 - can call methods of element
		    // 2 - can use iter.remove() to remove the current element from the list
		    // 3 - can use iter.add(...) to insert a new element into the list
		    //     between element and iter->next()
		    // 4 - can use iter.set(...) to replace the current element
		    //thanks Dave Newton. http://stackoverflow.com/questions/18410035/ways-to-iterate-over-a-list-in-java
		    // ...
		    if (element.playerName==find_playerName) {
		    	returnLocation=element.returnLocation;
		    	if (IsToBeRemoved) iter.remove();
		    	break;
		    }
		}
		
		return returnLocation;
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

	//implementations:
	
	
	
	@EventHandler
	public void onInteractEvent(PlayerInteractEvent event) { //formerly onClickEvent(PlayerInteractEvent event) {
		//boolean IsGood=true;
		Player player = event.getPlayer();
		World world = player.getWorld();
		Action action = event.getAction();
		
		///NOTE: in 1.7+ (ID doesn't exist, and) you do stuff like:
		//Material[] blacklist = {Material.OBSIDIAN, Material.GLASS, Material.GRASS};
		//@EventHandler (priority = EventPriority.NORMAL)
		//public void onBlockBreak (BlockBreakEvent event) {
		//	for (Material blacklisted : blacklist) {
	    //    if (event.getBlock().getType() == blacklisted) {
	    //        event.getPlayer().sendMessage(ChatColor.YELLOW + "You can't break that block!");
		//        event.setCancelled(true);
	    //    }
	    //	}
		//}
		
		if (action==Action.LEFT_CLICK_BLOCK) {
			Block clickedBlock = event.getClickedBlock();
			Location clickedLocation = clickedBlock.getLocation();
			if (clickedBlock.getTypeId()==blockid_wallsign
				|| clickedBlock.getTypeId()==blockid_standingsign) {
				//if (aboutToMakeTeleporterWithNextClickPlayerList==null) aboutToMakeTeleporterWithNextClickPlayerList=new ArrayList<String>();
				//if (aboutToBreakTeleporterWithNextClickPlayerList==null) aboutToBreakTeleporterWithNextClickPlayerList=new ArrayList<String>();
				if (aboutToMakeTeleporterWithNextClickPlayerList.contains(player.getName())) {//make WildPortal instead (aboutToMakeTeleporterWithNextClickPlayerList.contains(player.getName()))
					aboutToMakeTeleporterWithNextClickPlayerList.remove(player.getName());
					boolean IsGood=addWildPortal(clickedLocation, world.getName(), "<wild>",-500,-500,1000,1000);
					if (IsGood) {
						player.sendMessage(ChatColor.GREEN+"Created WildPortal");
						LastCreatedWildPortalFromPlayerLocation=player.getLocation();
						SaveOptionsToConfig();
						//DumpWildPortalList();
					}
					else {
						player.sendMessage(ChatColor.RED+"Failed to create WildPortal. That location may already be a WildPortal.");
					}
				}
				else if (aboutToBreakTeleporterWithNextClickPlayerList.contains(player.getName())) {
					aboutToBreakTeleporterWithNextClickPlayerList.remove(player.getName());
					String doneLocationString = clickedBlock.getLocation().toString();
					boolean IsRemoved=wildportalListRemoveByLocation(clickedBlock.getLocation());
					//clickedBlock.breakNaturally();
					if (IsRemoved) player.sendMessage(ChatColor.GREEN+"Block at "+doneLocationString+" is no longer a WildPortal.");
					else player.sendMessage(ChatColor.RED+"Block at "+doneLocationString+" was not a WildPortal or failed to be removed.");
				}
				else { //use the portal
					//if (!IsWildPortalListLoaded()) doLoadWildPortalData();
					if (IsWildPortalListLoaded()) {
						if (IsVerbose) main.logWriteLine("Checking whether sign is a WildPortal...");
						WildPortalPortal goWildPortal = getWildPortalAt(clickedBlock.getLocation());
						if (goWildPortal!=null) { //if (wildportalListContainsLocation(clickedBlock.getLocation())) {
							if (IsVerbose) main.logWriteLine("Yes (is WildPortal).");
							doWildPortal(player,goWildPortal);
						}
						else {
							if (IsVerbose) main.logWriteLine("No (not WildPortal).");
							//player.sendMessage("That sign ("+String.valueOf(clickedLocation.getX())+","+clickedLocation.getY()+","+clickedLocation.getZ()+") is not a WildPortal");
							//TODO: getLogger().info("WildPortal onClickEvent: the sign is not a WildPortal (this is normal for when anyone clicks a sign unless it was supposed to be a WildPortal)"); //TODO: remove this line
						}
					}
					else {
						player.sendMessage(ChatColor.RED+"ERROR: WildPortalList is not loaded.");
						//player.sendMessage("WildPortal onClickEvent: ERROR--the sign location list is not loaded.");
						//TODO: getLogger().info("WildPortal onClickEvent: ERROR--the sign location list is not loaded."); //TODO: remove this line
					}
					
				}
			}//if standing sign or wall sign
			else {
				if (aboutToMakeTeleporterWithNextClickPlayerList!=null
						&& aboutToMakeTeleporterWithNextClickPlayerList.contains(player.getName())) {
					aboutToMakeTeleporterWithNextClickPlayerList.remove(player.getName());
					player.sendMessage(ChatColor.RED+"You can only make a (standing or wall-mounted) sign into a WildPortal.");
				}
			}
		}//else not a block click action (may be action==Action.LEFT_CLICK_AIR)
		//return IsGood;
	}//end onInteractEvent
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		//PluginManager plg = Bukkit.getPluginManager(); //Assigns plg to Bukkit.getPluginManager()
		//Plugin plgname = main.getPlugin();//plg.getPlugin("WildPortal");
		if (IsVerbose) main.logWriteLine("getting player");
		Player player = ((Player) sender);
		//thisPlayer = player;
		if (IsVerbose) main.logWriteLine("getting world");
		World world = player.getWorld();
		
		if (IsVerbose) main.logWriteLine("about to call IsWildPortalListLoaded");
		//if (!IsWildPortalListLoaded()) {
		//	if (IsVerbose) main.logWriteLine("about to call IsWildPortalListLoaded");
		//	doLoadWildPortalData();
		//}
		
		if (IsVerbose) main.logWriteLine("about to call IsWildPortalListLoaded");
		if (IsWildPortalListLoaded()) {
			if (IsVerbose) main.logWriteLine("about to check whether command is wildportal");
			if (cmd.getName().equalsIgnoreCase("wildportal")) { // If the player typed /wildportal then do the following...
				if (IsVerbose) main.logWriteLine("about to check whether sender is a player");
				if (sender instanceof Player) {
					if (IsVerbose) main.logWriteLine("about to check args!=null && args.length>0");
					//if(args.length==0){sender.sendMessage(ChatColor.AQUA + "For help, use /wildportal help");}
					if (args!=null && args.length>0) {
						if (IsVerbose) main.logWriteLine("about to check whether args[0] is return");
						if (args[0].equalsIgnoreCase("return")) {
							if (IsVerbose) main.logWriteLine("about to check whether player has WildPortalReturn permission");
							if (player.hasPermission("wildportal.return")){
								if (IsVerbose) main.logWriteLine("about to check whether player "+player.getName()+" is "+AuthorDebugWorldBeingUsedByPlayerName+" (to create settings for author's debug world)");
								Location returnLocation=null;
								
								boolean IsToBeRemoved=true;
								returnLocation = getReturnLocationForPlayerElseNull(player.getName(),IsToBeRemoved);
								
								if (returnLocation!=null) {
									player.teleport(returnLocation);
								}
								else {
									boolean IsReturned=false;
									if (player.hasPermission("wildportal.manage")) { //need wildportal.manage to use a default portal
										if (LastCreatedWildPortalFromPlayerLocation!=null) {
											if (player.hasPermission("wildportal.manage")) { //need wildportal.manage to use a default portal
												player.teleport(LastCreatedWildPortalFromPlayerLocation);
												player.sendMessage(ChatColor.GRAY+"You have to use a WildPortal first during this run of the server in order to go back to where you were.");
												player.sendMessage(ChatColor.GRAY+"Since you have wildportal.manage, you have been sent to where player was standing who created the last-created WildPortal"+ChatColor.YELLOW+" (used location stored from this run of the server).");
												IsReturned=true;
											}
										}
										else if (config.contains(ConfigVariable_LastCreated_CategoryName)) {
							    			if (player.hasPermission("wildportal.manage")) {
							    				String config_returnWorldName=null;
							    				World config_returnWorld=null;
							    				boolean IsCompleteReturnData=true;
							    				config_returnWorldName=config.getString(ConfigVariable_LastCreated_CategoryName+"."+ConfigVariable_LastCreatedInWorld_VariableName);
							    				ConfigVariable_LastCreatedFromWorld_SubCategoryName=config_returnWorldName;
							    				if (config_returnWorldName!=null) {
							    					config_returnWorld=thisServer.getWorld(config_returnWorldName);
							    					if (config_returnWorld!=null) {
							    						if (!config.contains(ConfigVariable_LastCreated_CategoryName+"."+ConfigVariable_LastCreatedFromWorld_SubCategoryName+"."+ConfigVariable_LastCreatedFromX_VariableName)) IsCompleteReturnData=false;
							    						if (!config.contains(ConfigVariable_LastCreated_CategoryName+"."+ConfigVariable_LastCreatedFromWorld_SubCategoryName+"."+ConfigVariable_LastCreatedFromY_VariableName)) IsCompleteReturnData=false;
							    						if (!config.contains(ConfigVariable_LastCreated_CategoryName+"."+ConfigVariable_LastCreatedFromWorld_SubCategoryName+"."+ConfigVariable_LastCreatedFromZ_VariableName)) IsCompleteReturnData=false;
							    						if (IsCompleteReturnData) {
										    				int config_returnX=config.getInt(ConfigVariable_LastCreated_CategoryName+"."+ConfigVariable_LastCreatedFromWorld_SubCategoryName+"."+ConfigVariable_LastCreatedFromX_VariableName);
										    				int config_returnY=config.getInt(ConfigVariable_LastCreated_CategoryName+"."+ConfigVariable_LastCreatedFromWorld_SubCategoryName+"."+ConfigVariable_LastCreatedFromY_VariableName);
										    				int config_returnZ=config.getInt(ConfigVariable_LastCreated_CategoryName+"."+ConfigVariable_LastCreatedFromWorld_SubCategoryName+"."+ConfigVariable_LastCreatedFromZ_VariableName);
										    				LastCreatedWildPortalFromPlayerLocation = new Location(config_returnWorld, (double)config_returnX, (double)config_returnY, (double)config_returnZ);
										    				player.teleport(LastCreatedWildPortalFromPlayerLocation);
										    				player.sendMessage(ChatColor.GRAY+"You have to use a WildPortal first during this run of the server in order to go back to where you were.");
															player.sendMessage(ChatColor.GRAY+"Since you have wildportal.manage, you have been sent to where player was standing who created the last-created WildPortal"+ChatColor.YELLOW+" (loaded location from config).");
										    				IsReturned=true;
							    						}
							    						else {
							    							thisLogger.info("Not all coordinates were in config for /wildportal return, though the world from config named "+config_returnWorldName+" currently exists on the server.");
							    						}
							    					}
							    					else {
							    						IsCompleteReturnData=false;
							    						thisLogger.info("The world named "+config_returnWorldName+" for /wildportal return was not found on the server, though a "+ConfigVariable_LastCreated_CategoryName+"."+ConfigVariable_LastCreatedInWorld_VariableName+" exists.");
							    					}
							    					//else thisLogger.info("The world name "++" for /wildportal return was not found");
							    				}
							    				else {
							    					IsCompleteReturnData=false;
							    					thisLogger.info(ConfigVariable_LastCreated_CategoryName+" exists for /wildportal return, but the world name was null");
							    				}
							    			}
							    		}
									}
									/*
									if (!IsReturned && player.getName().equals(AuthorDebugWorldBeingUsedByPlayerName)) {//NOTE: == does NOT work since that is the identity operator
										if (IsVerbose) main.logWriteLine("about to create teleport destination for player who is using author's debug world");
										returnLocation = new Location(world,-143.0,80.0,263.0);
										if (IsVerbose) main.logWriteLine("about to teleport player who is using author's debug world");
										player.teleport(returnLocation);
										IsReturned=true;
									}
									*/
									
									if (!IsReturned) {
										if (IsVerbose) main.logWriteLine("Player "+player.getName()+" is not "+AuthorDebugWorldBeingUsedByPlayerName+", so program will operate in standard way.");
										player.sendMessage(ChatColor.RED+"You have to use a WildPortal first.");
										player.sendMessage(ChatColor.GRAY+"This command goes back to last used WildPortal.");
									}
								}//else player does not have own return location
							}
							else {
								player.sendMessage(ChatColor.RED+"You can't go back.");
							}
						}
						else if (args[0].equalsIgnoreCase("remove")) {
							if (player.hasPermission("wildportal.manage")){
								//if (aboutToBreakTeleporterWithNextClickPlayerList==null) aboutToBreakTeleporterWithNextClickPlayerList=new ArrayList<String>();
								if (!aboutToBreakTeleporterWithNextClickPlayerList.contains(player.getName())) aboutToBreakTeleporterWithNextClickPlayerList.add(player.getName());
								player.sendMessage(ChatColor.AQUA+"Click a sign to remove the WildPortal");
								player.sendMessage(ChatColor.GRAY+"(will not destroy the sign)...");
							}
						}
						else if (args[0].equalsIgnoreCase("verbose")) {
							if (player.hasPermission("wildportal.manage")){
								boolean previousIsVerbose=IsVerbose;
								if (args.length>1) {
									IsVerbose=FrameworkDummy.Convert_ToBoolean(args[1]);
									player.sendMessage(ChatColor.GREEN+"WildPortal verbose debugging mode set to "+(IsVerbose?ChatColor.WHITE+"ON":ChatColor.RED+"OFF"));
								}
								else {
									IsVerbose=!IsVerbose;
									player.sendMessage(ChatColor.GREEN+"WildPortal verbose debugging mode toggled "+(IsVerbose?ChatColor.WHITE+"ON":ChatColor.RED+"OFF"));
								}
								if (previousIsVerbose!=IsVerbose) SaveOptionsToConfig();
							}
						}
						else {
							player.sendMessage(ChatColor.YELLOW+"Unknown WildPortal parameters. Try /wildportal or /wildportal return or /wildportal remove");
						}
					}
					else {
						if (IsVerbose) main.logWriteLine("about to check whether player has create wildportal permission");
						if (player.hasPermission("wildportal.manage")){
							//if (aboutToMakeTeleporterWithNextClickPlayerList==null) aboutToMakeTeleporterWithNextClickPlayerList=new ArrayList<String>();
							if (!aboutToMakeTeleporterWithNextClickPlayerList.contains(player.getName())) aboutToMakeTeleporterWithNextClickPlayerList.add(player.getName());
							player.sendMessage(ChatColor.AQUA+"Click a sign to make it into a WildPortal");
							player.sendMessage(ChatColor.GRAY+"(Its text can say anything and won't be changed)...");
						}
						else {
							player.sendMessage(ChatColor.RED+"WildPortal: Sorry you are not authorized to create a WildPortal.");
						}
					}//end else no args
				}
				else {
					//sender.sendMessage
					main.logWriteLine("WildPortal: I think the SYSTEM is talking to me.");
				}
			}
		}
		else {
			player.sendMessage(ChatColor.RED+"ERROR: WildPortalList is not loaded.");
		}
		//TODO: save sign list in case any were added
		return true;
	}//end onCommand
	
	
	//general methods:
	
	//public void DumpWildPortalList() {
		
	//}

	public boolean addWildPortal(Location thisSignLocation, String destinationWorldName, String destinationKeyword, int dest_X, int dest_Y, int dest_Width, int dest_Height) {
		boolean IsGood=false;
		try {
			if (!wildportalListContainsLocation(thisSignLocation)) {
				WildPortalPortal newWildportalportal = new WildPortalPortal(thisSignLocation.getBlockX(), thisSignLocation.getBlockY(), thisSignLocation.getBlockZ(), thisSignLocation.getWorld().getName(), destinationWorldName,destinationKeyword,dest_X,dest_Y,dest_Width,dest_Height);
				wildportalList.add(newWildportalportal);
				IsGood=true;
		    	if (IsVerbose) {
		    		main.logWriteLine("verbose message: wildportalList has "+String.valueOf(wildportalList.size())+" elements.");
		    	}
		    	doSaveWildPortalData();
			}
		}
		catch (Exception e) {
			IsGood=false;
			main.logWriteLine("Could not finish addWildPortal:"+e.getMessage());
		}
	 	return IsGood;
	}
	public boolean IsWildPortalListLoaded() {
		return wildportalList!=null;
	}
	public WildPortalPortal getWildPortalAt(Location thisLocation) {
		WildPortalPortal returnWildPortal = null;
    	if (IsVerbose) {
    		main.logWriteLine("verbose message: wildportalList has "+String.valueOf(wildportalList.size())+" elements.");
    	}
		for (ListIterator<WildPortalPortal> iter = wildportalList.listIterator(); iter.hasNext(); ) {
			WildPortalPortal element = iter.next();
		    // 1 - can call methods of element
		    // 2 - can use iter.remove() to remove the current element from the list
		    // 3 - can use iter.add(...) to insert a new element into the list
		    //     between element and iter->next()
		    // 4 - can use iter.set(...) to replace the current element
		    //thanks Dave Newton. http://stackoverflow.com/questions/18410035/ways-to-iterate-over-a-list-in-java
		    // ...
		    if (element.IsLikeSource(thisLocation)) {//if (thisLocation.equals(element.getLocation(main.thisServer, thisLocation))) {
		    	//&& sourceSignInThisWorldName==element.sourceWorld ) { //NOTE: the Location has the world object
		    	returnWildPortal=element;
		    	if (IsVerbose) {
		    		main.logWriteLine("verbose message: checking current world "+thisLocation.getWorld().getName()+" against WildPortal source "+element.getLocationCoords()+" in world \""+element.sourceWorldName+"\" was true.");
		    	}
		    	break;
		    }
		    else {
		    	if (IsVerbose) {
		    		main.logWriteLine("verbose message: checking current world "+thisLocation.getWorld().getName()+" against WildPortal source "+element.getLocationCoords()+" in world \""+element.sourceWorldName+"\" was false.");
		    	}
		    }
		}//end for iter
		return returnWildPortal;
	}
	private boolean wildportalListContainsLocation(Location thisLocation) {
		WildPortalPortal returnWildPortal = getWildPortalAt(thisLocation);
		//boolean IsInList=false;
		return returnWildPortal!=null;//return IsInList;
	}//end wildPortalListContainsLocation

	private boolean wildportalListContains(WildPortalPortal thisWildPortalPortal) {
		boolean IsInList=false;
    	if (IsVerbose) {
    		main.logWriteLine("verbose message: wildportalList has "+String.valueOf(wildportalList.size())+" elements.");
    	}
		for (ListIterator<WildPortalPortal> iter = wildportalList.listIterator(); iter.hasNext(); ) {
			WildPortalPortal element = iter.next();
		    // 1 - can call methods of element
		    // 2 - can use iter.remove() to remove the current element from the list
		    // 3 - can use iter.add(...) to insert a new element into the list
		    //     between element and iter->next()
		    // 4 - can use iter.set(...) to replace the current element
		    //thanks Dave Newton. http://stackoverflow.com/questions/18410035/ways-to-iterate-over-a-list-in-java
		    // ...
		    if (element.equalsWildPortalSource(thisWildPortalPortal)) {
		    	//&& sourceSignInThisWorldName==element.sourceWorld ) { //NOTE: the Location has the world object
		    	IsInList=true;
		    	if (IsVerbose) {
		    		main.logWriteLine("verbose message: checking current world "+thisWildPortalPortal.sourceWorldName+" against WildPortal source "+element.getLocationCoords()+" in world \""+element.sourceWorldName+"\" was true.");
		    	}
		    	break;
		    }
		    else {
		    	if (IsVerbose) {
		    		main.logWriteLine("verbose message: checking current world "+thisWildPortalPortal.sourceWorldName+" against WildPortal source "+element.getLocationCoords()+" in world \""+element.sourceWorldName+"\" was false.");
		    	}
		    }
		}
		return IsInList;
	}//end wildportalListContains
	public boolean wildportalListRemoveByLocation(Location thisLocation) {
		boolean IsRemoved=false;
    	if (IsVerbose) {
    		main.logWriteLine("verbose message: wildportalList has "+String.valueOf(wildportalList.size())+" elements.");
    	}
		for (ListIterator<WildPortalPortal> iter = wildportalList.listIterator(); iter.hasNext(); ) {
			WildPortalPortal element = iter.next();
		    if (element.IsLikeSource(thisLocation)) { //if (thisLocation.equals(element.getLocation(main.thisServer))) {
		    	iter.remove();
		    	IsRemoved=true;
		    	doSaveWildPortalData();
		    	break;
		    }
		}
		return IsRemoved;
	}
	
	
	
	private void doSaveWildPortalData() {
		int triedCount=0;
		int savedCount=0;
		String participle="before initializing";
		if (wildportalList!=null) {
			//StringWriter sw =null;
			//BufferedWriter bw = null;
			//sw = new StringWriter();
			File file=null;
			FileWriter fileWriter = null;
			BufferedWriter bufferedWriter = null;
			try {
				participle="accessing filename";
				main.logWriteLine("Saving WildPortalPortal list \""+csvFileFullName+"\"...");
				participle="examining File";
				file = new File(csvFileFullName);
				participle="opening FileWriter";
				fileWriter = new FileWriter(file);
				participle="opening bufferedWriter";
				bufferedWriter = new BufferedWriter(fileWriter);
				participle="writing title row";
				bufferedWriter.write(WildPortalPortal.toCSVTitleRow());
				bufferedWriter.newLine();
				int index=0;
				for (ListIterator<WildPortalPortal> iter = wildportalList.listIterator(); iter.hasNext(); ) {
					participle="getting element "+String.valueOf(index);
					WildPortalPortal element = iter.next();
					participle="converting element to CSVLine"+String.valueOf(index);
					String thisLine=element.toCSVLine();
					if (thisLine!=null) {
						participle="writing element "+String.valueOf(index);
						bufferedWriter.write(thisLine);
						bufferedWriter.newLine();
						savedCount++;
					}
					triedCount++;
				}
				participle="closing buffered writer";
				bufferedWriter.close();
				//bufferedWriter=null;
				main.logWriteLine("OK (saved "+String.valueOf(savedCount)+" of "+String.valueOf(triedCount)+" WildPortal portals).");
			}
			catch (Exception e) {
				main.logWriteLine("Could not finish "+participle+" while saving WildPortalPortal list:"+e.getMessage());
			}
		}
		else {
			main.logWriteLine("OK (nothing to save).");
		}
	}//end doSaveWildPortalData
	
	private void doLoadWildPortalData() {
	    String participle="before parsing location integers";
		//LastCreatedSignLocation=null;
		//LastCreatedWildPortal=null;
		//LastCreatedSignLocation = new Location(world,(double)main.defaultX,(double)main.defaultY,(double)main.defaultZ);
		//authorizedSignLocationList.add(LastCreatedSignLocation);
		
//answered Mar 27 '13 at 4:24 by DarkStar. stackoverflow.com. <http://stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java> 27 Mar 2013. 21 Oct 2014.
		InputStream ins = null; // raw byte-stream
		Reader r = null; // cooked reader
		BufferedReader br = null; // buffered for readLine()
		int rowIndex=-1;//column names on first row so start at -1
	    int sourceFileLineNumber=1;
	    int triedCount=0;
	    int goodCount=0;
		try {
			participle="accessing csv file name";
		    main.logWriteLine("Loading WildPortal list \""+csvFileFullName+"\"...");
		    String originalLine;
		    participle="checking wildportalList";
		    if (wildportalList==null) wildportalList=new ArrayList<WildPortalPortal>(); //because ArrayList is a class which implements the List interface, List objects are instantiated as ArrayList like this.
			else wildportalList.clear();
		    participle="checking file";		    
		    File file = new File(csvFileFullName);
		    participle="looking for file";		    
		    if (file.exists()) {
		    	participle="opening file";
			    //if (true) {
			    //   String data = "#foobar\t1234\n#xyz\t5678\none\ttwo\n";
			    //    ins = new ByteArrayInputStream(data.getBytes());
			    //} else  {
		    		participle="creating file stream";
			        ins = new FileInputStream(csvFileFullName);
			    //} 
			    r = new InputStreamReader(ins, "UTF-8"); // leave charset out for default
		    	participle="creating file buffer";
			    br = new BufferedReader(r);
			    while ((originalLine = br.readLine()) != null) {
		    		if (rowIndex==-1) {
				    	participle="detecting column names on line "+String.valueOf(sourceFileLineNumber);
				    	boolean FoundAll=WildPortalPortal.DetectColumnIndeces(originalLine,csvFileName,sourceFileLineNumber);
				    	if (!FoundAll) {
				    		main.logWriteLine("Didn't find all column names on first row, so cancelling file load.");
				    		break;
				    	}
		    		}
		    		else {
		    			triedCount++;
				    	participle="parsing location integers on line "+String.valueOf(sourceFileLineNumber);
				    	LastCreatedWildPortal=WildPortalPortal.FromCSVLine(originalLine,csvFileName,sourceFileLineNumber);
				    	if (LastCreatedWildPortal!=null) {
				    		wildportalList.add(LastCreatedWildPortal);
				    		goodCount++;
				    	}
		    		}
	    			rowIndex++;
			    }//end while lines in file
			    main.logWriteLine("OK (loaded "+String.valueOf(goodCount)+" of "+String.valueOf(triedCount)+" WildPortal portals).");
		    }//end if file exists
		    else {
		    	main.logWriteLine("Nothing to load (no "+csvFileName+").");
		    }
		}
		catch (Exception e) {
		    main.logWriteLine("doLoadWildPortalData could not finish "+participle+" (was about to start line "+String.valueOf(sourceFileLineNumber)+"):"+e.getMessage());//System.err.println(e.getMessage());
		}
		finally {
		    if (br != null) { try { br.close(); } catch(Throwable t) { /* ensure close happens */ } }
		    if (r != null) { try { r.close(); } catch(Throwable t) { /* ensure close happens */ } }
		    if (ins != null) { try { ins.close(); } catch(Throwable t) { /* ensure close happens */ } }
		}		
	}//end doLoadWildPortalData
	
	private void doWildPortal(Player player, WildPortalPortal thisWildPortal) {
		
		//deprecated String WildPortalAdminName = main.config.getString("WildPortalAdmin.playerName");
		//deprecated main.logWriteLine("WildPortalAdmin.playerName is "+WildPortalAdminName);
		if (IsVerbose) main.logWriteLine("doWildPortal: getting rectangle");
		int minRandomX=thisWildPortal.getDestXMin(); //-314; //TODO: find world border or something
		int minRandomZ=thisWildPortal.getDestZMin(); //70; //TODO: find world border or something
		int maxRandomX=thisWildPortal.getDestXMax(); //-151; //TODO: find world border or something
		int maxRandomZ=thisWildPortal.getDestZMax(); //250; //TODO: find world border or something
		int rangeX=maxRandomX-minRandomX;
		int rangeZ=maxRandomZ-minRandomZ;
		Random rnd = new Random();
		int randomCountingNumberX=0;
		int randomCountingNumberZ=0;
		int randomIntX=0;
		int randomIntZ=0;
		
		//Location sourceLocation = player.getLocation();
		
		if (IsVerbose) main.logWriteLine("doWildPortal: getting destination world");
		World destWorld=thisWildPortal.getDestinationWorld(thisServer,player.getWorld());
		//Location destLocation = new Location(world, 0,68,0);
		//Location destLocation = new Location(world, sourceLocation.getX(), sourceLocation.getY()+20.0, sourceLocation.getZ() );
		Location randomLocation = null;
		double minY=49.0;
		LastUsedBlockID=-1;
		LastUsedY=-1;
		Location checkedLocation=null;
		int MaxTryCount=100;
		int TryCount=0;//tries so far
		
		boolean IsTeleported=false;
		while (TryCount<MaxTryCount) {
			if (IsVerbose) main.logWriteLine("doWildPortal: getting location on ground if ground is ok");
			randomCountingNumberX=rnd.nextInt(rangeX+1);//+1 since exclusive
			randomCountingNumberZ=rnd.nextInt(rangeZ+1);//+1 since exclusive
			randomIntX=randomCountingNumberX+minRandomX;//allows possibly negative (if minimum is negative)
			randomIntZ=randomCountingNumberZ+minRandomZ;//allows possibly negative (if minimum is negative)
			randomLocation = new Location(destWorld, (double)randomIntX,64.0,(double)randomIntZ);
			checkedLocation = getLocationOnGround(destWorld, randomLocation, minY, player);
			if (checkedLocation.getY()>=minY) {
				checkedLocation.setY(checkedLocation.getY()+(double)StartAboveGroundByInt);
				boolean IsToBeRemoved=true;
				Location deleteOldReturnLocation=getReturnLocationForPlayerElseNull(player.getName(),IsToBeRemoved);
				wildportalplayerdataList.add(new WildPortalPlayerData(player.getName(),player.getLocation()));
				if (thisServer!=null) thisServer.broadcastMessage(player.getName() + " is being born in "+destWorld.getName()+"..."); //TODO: send to all players
				int NoDamageSeconds=20;
				int TicksPerSecond=20;
				int NoDamageTicks=NoDamageSeconds*TicksPerSecond;
				if (player.getMaximumNoDamageTicks()<NoDamageTicks) player.setMaximumNoDamageTicks(NoDamageTicks);
				player.setNoDamageTicks(NoDamageTicks);//tick is 1/20th of a second (50ms)
				player.teleport(checkedLocation);
				IsTeleported=true;
				//Bukkit.broadcast(player.getName() + " has arrived.");
				if (thisServer!=null) thisServer.broadcastMessage((player.getName() + " has arrived."));
				main.logWriteLine("WildPortal destination in range ("+thisWildPortal.toString_DestRectOnly_AsRangePair()+") for "+player.getName()+" was: "+checkedLocation.toString());
				break;
			}
			TryCount++;
		}
		if (!IsTeleported) {
			player.sendMessage("WildPortal: Uh oh, portal couldn't find a suitable location for you yet--tried "+String.valueOf(TryCount)+"times--last try was at or above ("+String.valueOf(checkedLocation.getX() )+","+String.valueOf(checkedLocation.getY())+","+String.valueOf(checkedLocation.getZ())+") [last Y:"+String.valueOf(LastUsedY)+"; last used block id:"+String.valueOf(LastUsedBlockID)+"]. Please try again.");
		}
	}//end doWildPortal
	
	private static Location getLocationOnGround(World world, Location tryLocation_XandZ, double minY, Player player) {
		//tryLocation_XandZ.setY(255);
		int tryX=(int)tryLocation_XandZ.getX();
		int tryY=255;
		int tryZ=(int)tryLocation_XandZ.getZ();
		LastUsedBlockID=-1;
		//TODO: prevent ending up back in spawn area, so "being born" message only happens once.
		if (IsVerbose) main.logWriteLine("Finding a place for "+player.getName() + " in "+world.getName()+"..."); //TODO: remove this (plays every TRY not just every birth)
		while (tryY>=minY) {
			LastUsedY=tryY;
			int thisBlockTypeID=world.getBlockTypeIdAt(tryX,tryY,tryZ);
			
			
			if (thisBlockTypeID==blockid_grass
					|| thisBlockTypeID==blockid_sand
					|| thisBlockTypeID==blockid_ice
					|| thisBlockTypeID==blockid_snow
					|| thisBlockTypeID==blockid_mycel
					) {
				int block1AboveID=world.getBlockTypeIdAt(tryX,tryY+1,tryZ);
				int block2AboveID=world.getBlockTypeIdAt(tryX,tryY+2,tryZ);
				if (block1AboveID==0 && block2AboveID==0) {
					LastUsedBlockID=thisBlockTypeID;
					tryY+=1;//move from grass block to air block above it
					break;
				}
				else {
					if (IsVerbose) main.logWriteLine("WildPortal: Skipping a potentially habitable area because of smothering block (ID "+String.valueOf(LastUsedBlockID)+") at ("+String.valueOf(tryX)+","+String.valueOf(tryY)+","+String.valueOf(tryZ)+")...");//TODO: remove this
				}
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
				//if (player!=null) {
				if (IsVerbose) main.logWriteLine("WildPortal: Skipping a potentially covered area because of dangerous block (ID "+String.valueOf(LastUsedBlockID)+") at ("+String.valueOf(tryX)+","+String.valueOf(tryY)+","+String.valueOf(tryZ)+")...");//TODO: remove this
				//}
				tryY=0;//forces "not found" condition
				break;
			}
			//TODO: also make sure it is "level" ground (check for 9x9 area with only levels only differing by up to 1.0)
			tryY-=1.0;
		}
		if (tryY<minY) tryLocation_XandZ.setY(0.0);//0.0 is a FLAG that there is no ground here
		else tryLocation_XandZ.setY(tryY);
		return tryLocation_XandZ;
	}//end getLocationOnGround
	
	
}//end class ClickListener