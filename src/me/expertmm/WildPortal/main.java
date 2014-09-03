package me.expertmm.WildPortal;

import org.
//for handling events other than commands, you need a listener (other class)
public class main extends JavaPlugin { 
	public void onEnable (){
		getLogger().info("WildPortal Enabled!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		PluginManager plg = Bukkit.getPluginManager(); //Assigns plg to Bukkit.getPluginManager()
		Plugin plgname = plg.getPlugin("WildPortal");
		if (cmd.getName().equalsIgnoreCase("wildportal")) { // If the player typed /wildportal then do the following...
			if(args.length==0){sender.sendMessage(ChatColor.AQUA + "For help, use /wildportal help");}
			if (sender instanceof Player){
				
			}
		}
	}
	
	public void onDisable(){
		getLogger().info("WildPortal Disabled");}
}