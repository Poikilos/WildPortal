/**
 * @author Jacob Gustafson
 *
 */
package me.expertmm.WildPortal;

/*
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

//import java.util.Collection;

*/

//or just:
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;
//import java.util.List;
//import java.util.ArrayList;//implements list (instantiate List objects with this)
//import java.util.ListIterator;
//for handling events other than commands, you need a listener (other class)
public class main extends JavaPlugin { 

	//private static main thisMain = null;
	private static main plugin=null;//private static Plugin plugin=null;
	private static Logger thisLogger = null;
	private static MultiEventHandler meh = null;
	//private Server thisServer = null;
	//private FileConfiguration config = null;
	
	public static void logWriteLine(String msg) {
		if (thisLogger!=null) thisLogger.info(msg);
	}
	
	//public static void broadcast(String msg) {
	//	if (thisServer!=null) thisServer.broadcastMessage(msg);
	//}
	
	private void InitializeConfig() {
		//thisMain = this;
		//config = getConfig();
		//config.addDefault("WildPortalAdmin.playerName", "Abiyahh");//deprecated
		//config.addDefault("Debug.IsVerbose", "False");
		//MultiEventHandler.IsVerbose=config.getBoolean("Debug.IsVerbose");
		//config.options().copyDefaults();
		
	}
	
	//public static void saveThisConfig() {
	//	thisMain.saveConfig();
	//}
	
	public void onEnable () {
		thisLogger = getLogger();//Bukkit.getLogger();
		plugin = this;
		InitializeConfig();
		thisLogger.info("creating event handler...");
		meh = new MultiEventHandler();
		getCommand("wildportal").setExecutor(meh);//new MultiEventHandler()
		//thisServer=this.getServer();
		thisLogger.info("WildPortal Enabled!");
		registerEvents(this, meh);//new MultiEventHandler()
		//or registerEvents(this, new ClickListener(), new ListenerClass1(), new SomeOtherListener());
		//PluginManager pm = this.getServer().getPluginManager();
        //pm.registerEvents(PlayerInteractEvent, this);
		//getLogger().info("Since creating portals is not yet implemented, a standing sign or wall-mounted sign (it can say anything) at "+String.valueOf(defaultX)+","+String.valueOf(defaultY)+","+String.valueOf(defaultZ)+" (which would otherwise be the empty air block in front of the wall block) will be the only WildPortal allowed.");
	}
	
	public void onDisable() {
		plugin = null; //for garbage collection
		getLogger().info("WildPortal Disabled");
	}

	public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}
	
	//so other classes can use plugin:
	public static main getPlugin() {//public static Plugin getPlugin() {
		return plugin;
	}
	
	
}//end class main extends JavaPlugin