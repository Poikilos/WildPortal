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
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
//utilities:
import java.util.Random;
import java.util.List;
import java.util.ArrayList;//implements list (instantiate List objects with this)
import java.util.ListIterator;


public class MultiEventHandler implements CommandExecutor, Listener {
//public class ClickListener extends JavaPlugin implements Listener {
	
	//NOTE: you canNOT do getConfig from here, nor make this extend JavaPlugin
	
	//Thanks to Both class by JPG2000F on forums.bukkit.org
	
	//static Player thisPlayer = null;
	public static List<String> aboutToMakeTeleporterWithNextClickPlayerList = null;
	static List<WildPortalPortal> wildportalList = null; //static List<Location> authorizedSignLocationList = null;
	static List<WildPortalPlayerData> wildportalPlayerList = null;
	public static String AbiyahhsDebugWorldBeingUsedByPlayerName="Abiyahh";
	
	public static boolean IsPlayerListLoaded() {
		return wildportalPlayerList!=null;
	}
	public static void doLoadPlayerList() {
		if (wildportalPlayerList==null) wildportalPlayerList = new ArrayList<WildPortalPlayerData>();
	}
	public static Location getReturnLocationForPlayerElseNull(String find_playerName) {
		Location returnLocation=null;
		if (!IsPlayerListLoaded()) doLoadPlayerList();
		for (ListIterator<WildPortalPlayerData> iter = wildportalPlayerList.listIterator(); iter.hasNext(); ) {
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
		    	iter.remove();
		    	break;
		    }
		}
		
		return returnLocation;
	}
	
	static int LastUsedBlockID = -1;
	static int LastUsedY = -1;
	
	static int StartAboveGroundByInt = 4;
	
	
	//TEKKIT block ids:
	static boolean IsVerbose=true;
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

    public static String SourceWorldColumnName="SourceWorld";
    public static String SourceXColumnName="SourceX";
    public static String SourceYColumnName="SourceY";
    public static String SourceZColumnName="SourceZ";
    public static String DestinationWorldColumnName="DestinationWorld";
    public static String DestinationKeywordColumnName="DestinationKeyword";
    public static String AllowedPlayerListColumnName="AllowedPlayerList";

	// Minecraft 1.7+:			
	//int blockid_tripwire=132;
	// Minecraft 1.8+(or so):
	//int blockid_barrier=166;//slashed circle or not shown
	
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
    
    public MultiEventHandler() {
		if (aboutToMakeTeleporterWithNextClickPlayerList==null) aboutToMakeTeleporterWithNextClickPlayerList=new ArrayList<String>();
    }
	
	
	//public boolean onClickEvent(PlayerInteractEvent event) {
	@EventHandler
	public void onInteractEvent(PlayerInteractEvent event) {
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
				if (aboutToMakeTeleporterWithNextClickPlayerList==null) aboutToMakeTeleporterWithNextClickPlayerList=new ArrayList<String>();
				if (!aboutToMakeTeleporterWithNextClickPlayerList.contains(player.getName())) {
					
					

					if (!IsWildPortalListLoaded()) doLoadWildPortalData(world);
					if (IsWildPortalListLoaded()) {
						if (wildportalListContainsLocation(clickedBlock.getLocation(),world.getName())) {
							doWildPortal(player,world);
						}
						else {
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
				else {//make WildPortal instead (aboutToMakeTeleporterWithNextClickPlayerList.contains(player.getName()))
					aboutToMakeTeleporterWithNextClickPlayerList.remove(player.getName());
					boolean IsGood=addWildPortal(clickedLocation, world.getName(), world.getName(), "<wild>");
					if (IsGood) {
						player.sendMessage(ChatColor.GREEN+"Created WildPortal");
						//DumpWildPortalList();
					}
					else {
						player.sendMessage(ChatColor.RED+"Failed to create WildPortal. That location may already be a WildPortal.");
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
		if (!IsWildPortalListLoaded()) {
			if (IsVerbose) main.logWriteLine("about to call IsWildPortalListLoaded");
			doLoadWildPortalData(world);
		}
		
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
							//TODO: make this actually check args
							if (IsVerbose) main.logWriteLine("about to check whether player has WildPortalReturn permission");
							if (player.hasPermission("WildPortalReturn")){
								if (IsVerbose) main.logWriteLine("about to check whether player "+player.getName()+" is Abiyahh (to create settings for author's debug world)");
								Location returnLocation=null;
								
								if (player.getName().startsWith(AbiyahhsDebugWorldBeingUsedByPlayerName) && player.getName().length()==AbiyahhsDebugWorldBeingUsedByPlayerName.length()) {//NOTE: == does NOT work since that is the identity operator
									if (IsVerbose) main.logWriteLine("about to create teleport destination for player using Abiyahh's debug world");
									returnLocation = new Location(world,-143.0,80.0,263.0);
									if (IsVerbose) main.logWriteLine("about to teleport player using Abiyahh's debug world");
								}
								else {
									if (IsVerbose) main.logWriteLine("Player "+player.getName()+" is not "+AbiyahhsDebugWorldBeingUsedByPlayerName+", so program will operate in standard way.");
									returnLocation = getReturnLocationForPlayerElseNull(player.getName());
								}
								if (returnLocation!=null) player.teleport(returnLocation);
							}
							else {
								player.sendMessage(ChatColor.RED+"You can't go back.");
							}
						}
						else {
							player.sendMessage(ChatColor.YELLOW+"Unknown WildPortal parameters. Try /wildportal or /wildportal return");
						}
					}
					else {
						if (IsVerbose) main.logWriteLine("about to check whether player has create wildportal permission");
						if (player.hasPermission("WildPortalCreateWildPortal")){
							//do something
							//doWildPortal(player,world);
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

	public static boolean addWildPortal(Location thisSignLocation, String thisSignWorldName, String destinationWorldName, String destinationKeyword) {
		boolean IsGood=false;
		try {
			if (!wildportalListContainsLocation(thisSignLocation,thisSignWorldName)) {
				WildPortalPortal newWildportalportal = new WildPortalPortal(thisSignLocation,thisSignWorldName, destinationWorldName,destinationKeyword);
				wildportalList.add(newWildportalportal);
				IsGood=true;
		    	if (IsVerbose) {
		    		main.logWriteLine("verbose message: wildportalList has "+String.valueOf(wildportalList.size())+" elements.");
		    	}
			}
		}
		catch (Exception e) {
			main.logWriteLine("Could not finish addWildPortal:"+e.getMessage());
		}
	 	return IsGood;
	}
	public static boolean IsWildPortalListLoaded() {
		return wildportalList!=null;
	}
	public static boolean wildportalListContains(WildPortalPortal thisWildportalportal) {
		return wildportalListContainsLocation(thisWildportalportal.getLocation(main.thisServer), thisWildportalportal.sourceWorld);
	}
	public static boolean wildportalListContainsLocation(Location thisLocation, String sourceSignInThisWorldName) {
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
		    if (thisLocation.equals(element.getLocation(main.thisServer))
		    	&& sourceSignInThisWorldName==element.sourceWorld ) {
		    	IsInList=true;
		    	if (IsVerbose) {
		    		main.logWriteLine("verbose message: checking current world "+sourceSignInThisWorldName+" against WildPortal source "+String.valueOf(element.X)+","+String.valueOf(element.Y)+","+String.valueOf(element.Z)+" in world \""+element.sourceWorld+"\" was true.");
		    	}
		    	break;
		    }
		    else {
		    	if (IsVerbose) {
		    		main.logWriteLine("verbose message: checking current world "+sourceSignInThisWorldName+" against WildPortal source "+String.valueOf(element.X)+","+String.valueOf(element.Y)+","+String.valueOf(element.Z)+" in world \""+element.sourceWorld+"\" was false.");
		    	}
		    }
		}
		return IsInList;
	}
	
	public void doLoadWildPortalData(World world) {
		if (wildportalList==null) wildportalList=new ArrayList<WildPortalPortal>(); //because ArrayList is a class which implements the List interface, List objects are instantiated as ArrayList like this.
		else wildportalList.clear();
		LastCreatedSignLocation=null;
		WildPortalPortal LastCreatedWildPortal=null;
		//TODO?: need to collect garbage (LastCreatedSignLocation)?
		//LastCreatedSignLocation = new Location(world,(double)main.defaultX,(double)main.defaultY,(double)main.defaultZ);
		//authorizedSignLocationList.add(LastCreatedSignLocation);
		
//answered Mar 27 '13 at 4:24 by DarkStar. stackoverflow.com. <http://stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java> 27 Mar 2013. 21 Oct 2014.
		InputStream ins = null; // raw byte-stream
		Reader r = null; // cooked reader
		BufferedReader br = null; // buffered for readLine()
		int rowIndex=-1;//column names on first row so start at -1
	    int sourceFileLineNumber=1;
	    String csvFileName="WildPortal.csv";
	    String participle="before parsing location integers";
		try {
		    String originalLine;
		    //if (true) {
		    //   String data = "#foobar\t1234\n#xyz\t5678\none\ttwo\n";
		    //    ins = new ByteArrayInputStream(data.getBytes());
		    //} else  {
		        ins = new FileInputStream(csvFileName);
		    //} 
		    r = new InputStreamReader(ins, "UTF-8"); // leave charset out for default
		    br = new BufferedReader(r);
		    String trimmedLine;
		    int Column_SourceWorld=-1;
		    int Column_SourceX=-1;
		    int Column_SourceY=-1;
		    int Column_SourceZ=-1;
		    int Column_DestinationWorld=-1;
		    int Column_DestinationKeyword=-1;
		    int Column_AllowedPlayerList=-1;
		    while ((originalLine = br.readLine()) != null) {
		    	try {
			    	trimmedLine=originalLine.trim();
			    	if (trimmedLine.length()>0) {
			    		List<String> fields = main.getExplodedCSVLine(trimmedLine);
			    		if (rowIndex==-1) {
			    			//column names on first row (-1 since the wildportalList item 0 is after that row)
			    			Column_SourceWorld=fields.indexOf(SourceWorldColumnName);
			    			if (Column_SourceWorld<0) {
			    				main.logWriteLine("No column named SourceWorld in "+csvFileName+" was found.");
			    				break;
			    			}
			    			Column_SourceX=fields.indexOf(SourceXColumnName);
			    			if (Column_SourceX<0) {
			    				main.logWriteLine("No column named "+SourceXColumnName+" in "+csvFileName+" was found.");
			    				break;
			    			}
			    			Column_SourceY=fields.indexOf(SourceYColumnName);
			    			if (Column_SourceY<0) {
			    				main.logWriteLine("No column named "+SourceYColumnName+" in "+csvFileName+" was found.");
			    				break;
			    			}
			    			Column_SourceZ=fields.indexOf("SourceZ");
			    			if (Column_SourceZ<0) {
			    				main.logWriteLine("No column named "+SourceZColumnName+" in "+csvFileName+" was found.");
			    				break;
			    			}
			    			Column_DestinationWorld=fields.indexOf(DestinationWorldColumnName);
			    			if (Column_DestinationWorld<0) {
			    				main.logWriteLine("No column named "+DestinationWorldColumnName+" in "+csvFileName+" was found.");
			    				break;
			    			}
			    			Column_DestinationKeyword=fields.indexOf(DestinationKeywordColumnName);
			    			if (Column_DestinationKeyword<0) {
			    				main.logWriteLine("No column named "+DestinationKeywordColumnName+" in "+csvFileName+" was found.");
			    				break;
			    			}
			    			Column_AllowedPlayerList=fields.indexOf(AllowedPlayerListColumnName);
			    			if (Column_AllowedPlayerList<0) {
			    				main.logWriteLine("No column named "+AllowedPlayerListColumnName+" in "+csvFileName+" was found.");
			    				break;
			    			}
			    		}
			    		else {
			    			String sourceWorldName = fields.get(Column_SourceWorld);
			    			if (sourceWorldName!=null) {
				    			World sourceWorld = main.thisServer.getWorld(sourceWorldName);
				    			if (sourceWorld!=null) {
				    				//participle="getting world name field value";
				    				String destWorldName=fields.get(Column_DestinationWorld);
				    				if (destWorldName!=null) {
				    					//participle="getting world";
					    				World destWorld = main.thisServer.getWorld(destWorldName);
					    				if (destWorld!=null) {
					    					participle="parsing location integers";
							    			Location sourceLocation = new Location(sourceWorld,
							    					(double) Integer.parseInt(fields.get(Column_SourceX)), //parse as int for safety, since was saved without decimal point
							    					(double) Integer.parseInt(fields.get(Column_SourceY)), //parse as int for safety, since was saved without decimal point
							    					(double) Integer.parseInt(fields.get(Column_SourceZ)) //parse as int for safety, since was saved without decimal point
							    					);
							    			participle="after parsing location integers";
							    			if (sourceLocation!=null) {
							    				String destinationKeyword=fields.get(Column_DestinationKeyword);
							    				if (destinationKeyword!=null) {
									    			WildPortalPortal thisWildPortal = new WildPortalPortal(sourceLocation, sourceWorldName, destWorldName, destinationKeyword);
									    			participle="adding wildportal to list";
									    			wildportalList.add(thisWildPortal);
									    			sourceLocation = null;
									    			thisWildPortal = null;
							    				}
								    			else {
								    				main.logWriteLine("Column "+String.valueOf(Column_DestinationKeyword)+" ("+DestinationKeywordColumnName+") was read as null in "+csvFileName+" line "+String.valueOf(sourceFileLineNumber));
								    			}
							    			}
							    			else {
							    				main.logWriteLine("Columns "+String.valueOf(Column_SourceX)+","+String.valueOf(Column_SourceY)+" and "+String.valueOf(Column_SourceZ)+" (SourceX,SourceY,SourceZ) resulted in null Location in "+csvFileName+" line "+String.valueOf(sourceFileLineNumber));
							    			}
					    				}
						    			else {
						    				main.logWriteLine("No actual world named \""+destWorldName+"\" for DestinationWorld in "+csvFileName+" line "+String.valueOf(sourceFileLineNumber)+".");
						    			}
				    				}
					    			else {
					    				main.logWriteLine("Column "+String.valueOf(Column_DestinationWorld)+" ("+DestinationWorldColumnName+") was read as null in "+csvFileName+" line "+String.valueOf(sourceFileLineNumber));
					    			}
				    			}
				    			else {
				    				main.logWriteLine("No actual world named \""+sourceWorldName+"\" for "+SourceWorldColumnName+" in "+csvFileName+" line "+String.valueOf(sourceFileLineNumber)+".");
				    			}
			    			}
			    			else {
			    				main.logWriteLine("Column "+String.valueOf(Column_SourceWorld)+" ("+SourceWorldColumnName+") was read as null in "+csvFileName+" line "+String.valueOf(sourceFileLineNumber));
			    			}
			    		}
			    		rowIndex++;
			    	}//end if trimmedLine is not blank
		    	}
		    	catch (Exception e) {
				    main.logWriteLine("doLoadWildPortalData could not finish parsing line "+String.valueOf(sourceFileLineNumber)+":"+e.getMessage());//System.err.println(e.getMessage());
		    	}
		    }//end while lines in file
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
	
	public static void doWildPortal(Player player, World world) {
		
		//deprecated String WildPortalAdminName = main.config.getString("WildPortalAdmin.playerName");
		//deprecated main.logWriteLine("WildPortalAdmin.playerName is "+WildPortalAdminName);
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
		
		//Location sourceLocation = player.getLocation();
		
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
			checkedLocation.setY(checkedLocation.getY()+(double)StartAboveGroundByInt);
			player.teleport(checkedLocation);
			//Bukkit.broadcast(player.getName() + " has arrived.");
			main.broadcast((player.getName() + " has arrived."));
			main.logWriteLine("WildPortal destination for "+player.getName()+" was: "+checkedLocation.toString());
		}
		else {
			player.sendMessage("WildPortal: Uh oh, portal couldn't find a suitable location for you yet--tried above ("+String.valueOf(checkedLocation.getX() )+","+String.valueOf(checkedLocation.getY())+","+String.valueOf(checkedLocation.getZ())+") [last Y:"+String.valueOf(LastUsedY)+"; last used block id:"+String.valueOf(LastUsedBlockID)+"]. Please try again.");
		}

	}//end doWildPortal
	
	private static Location getLocationOnGround(World world, Location tryLocation_XandZ, double minY, Player player) {
		//tryLocation_XandZ.setY(255);
		int tryX=(int)tryLocation_XandZ.getX();
		int tryY=255;
		int tryZ=(int)tryLocation_XandZ.getZ();
		LastUsedBlockID=-1;
		//TODO: prevent ending up back in spawn area, so "being born" message only happens once.
		main.logWriteLine("Finding a place for "+player.getName() + " in "+world.getName()+"..."); //TODO: remove this (plays every TRY not just every birth)
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
					player.sendMessage("WildPortal: Skipping a potentially habitable area because of smothering block (ID "+String.valueOf(LastUsedBlockID)+") at ("+String.valueOf(tryX)+","+String.valueOf(tryY)+","+String.valueOf(tryZ)+")...");//TODO: remove this
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
	}//end getLocationOnGround
	
	
}//end class ClickListener
