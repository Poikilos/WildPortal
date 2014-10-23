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
import java.util.List;
import java.util.ArrayList;//implements list (instantiate List objects with this)
//import java.util.ListIterator;
//for handling events other than commands, you need a listener (other class)
public class main extends JavaPlugin { 

	private static Plugin plugin;
	private static Logger thisLogger = null;
	public static Server thisServer = null;
	public static FileConfiguration config = null;
	
	public static void logWriteLine(String msg) {
		if (thisLogger!=null) thisLogger.info(msg);
	}
	public static List<String> getExplodedCSVLine(String line) {
		return getExplodedDelimited(line,',','"');
	}
	public static String FrameworkDummySubstring(String val, int start, int count) {
		return val.substring(start, count-start);
	}
	public static String LiteralFieldToCSVField(String sCSVField, char cFieldDelimX, char cTextDelimX, String newLineBecomesWhat_ElseNullForEscapeSequences) {//aka CSVFieldToString
		String oneQuote=""+cTextDelimX;
		String twoQuotes=""+cTextDelimX+""+cTextDelimX;
		String fieldDelimiterString = ""+cFieldDelimX;
		//String textDelimiterString = ""+cTextDelimX;
		sCSVField=sCSVField.replace(oneQuote, twoQuotes);
		//if (ReplaceNewLineWithTabInsteadOfHTMLBr) {
		//	sCSVField=sCSVField.replace("\r\n", "\t");
		//	sCSVField=sCSVField.replace("\r", "\t");
		//	sCSVField=sCSVField.replace("\n", "\t");
		//}
		/*
		if (ReplaceNewLineWithEscapeSequencesInsteadOfHTMLBr) {
			sCSVField=sCSVField.replace("\r\n", "\t");
			sCSVField=sCSVField.replace("\r", "\t");
			sCSVField=sCSVField.replace("\n", "\t");
		}
		else {
			sCSVField=sCSVField.replace("\r\n", "<br/>");
			sCSVField=sCSVField.replace("\r", "<br/>");
			sCSVField=sCSVField.replace("\n", "<br/>");
		}
		*/
		newLineBecomesWhat_ElseNullForEscapeSequences=newLineBecomesWhat_ElseNullForEscapeSequences.replace("\r","\\r");
		newLineBecomesWhat_ElseNullForEscapeSequences=newLineBecomesWhat_ElseNullForEscapeSequences.replace("\n","\\n");
		if (newLineBecomesWhat_ElseNullForEscapeSequences==null) {
			sCSVField=sCSVField.replace("\r","\\r");
			sCSVField=sCSVField.replace("\n","\\n");
		}
		else {
			sCSVField=sCSVField.replace("\r\n", newLineBecomesWhat_ElseNullForEscapeSequences);
			sCSVField=sCSVField.replace("\r", newLineBecomesWhat_ElseNullForEscapeSequences);
			sCSVField=sCSVField.replace("\n", newLineBecomesWhat_ElseNullForEscapeSequences);
		}
		if (sCSVField.contains(fieldDelimiterString)) {
			sCSVField=oneQuote+sCSVField+oneQuote;
		}
		return sCSVField;
	}
	public static String CSVFieldToLiteralField(String sCSVField, char cFieldDelimX, char cTextDelimX) {//aka CSVFieldToString
		//This logic is consistent with popular proprietary and open source spreadsheet applications.
		String oneQuote=""+cTextDelimX;
		String twoQuotes=""+cTextDelimX+""+cTextDelimX;
		if (sCSVField.contains(oneQuote)) {
			if (sCSVField.length()>=2&&sCSVField.charAt(0)==cTextDelimX&&sCSVField.charAt(sCSVField.length()-1)==cTextDelimX) {
				sCSVField=FrameworkDummySubstring(sCSVField,1,sCSVField.length()-2);//debug performance (recreating string)
			}
			sCSVField=sCSVField.replace(twoQuotes, oneQuote);//debug performance (recreating string, type conversion from char to string)
		}
		//for (int iNow=0; iNow<sarrEscapeLiteral.length(); iNow++) {
		//	sCSVField=RString.Replace(sCSVField,sarrEscapeSymbolic[iNow],sarrEscapeLiteral[iNow]);// (old,new)
		//}
		return sCSVField;//now a raw field
	}
	public static String RowToCSVLine(List<String> sarrLiteralFields, char cFieldDelimX, char cTextDelimX, String newLineBecomesWhat_ElseNullForEscapeSequences) {
		String returnString="";
		if (sarrLiteralFields!=null) returnString=PartialRowToCSVLine(sarrLiteralFields,cFieldDelimX,cTextDelimX,0,sarrLiteralFields.size(),newLineBecomesWhat_ElseNullForEscapeSequences);
		else {
			thisLogger.info("RowToCSVLine cannot convert null row field array to CSV Line");
		}
		return returnString;
	}
	public static String PartialRowToCSVLine(List<String> sarrLiteralFields, char cFieldDelimX, char cTextDelimX, int ColumnStart, int ColumnCount, String newLineBecomesWhat_ElseNullForEscapeSequences) {
		String sReturn="";
		try {
			if (sarrLiteralFields!=null) {
				if (ColumnStart+ColumnCount>sarrLiteralFields.size()) ColumnCount=sarrLiteralFields.size()-ColumnStart;
				int iCol=ColumnStart;
				for (int iColRel=0; iColRel<ColumnCount; iColRel++) {
					sReturn += (iCol>0?(""+cFieldDelimX):"") + LiteralFieldToCSVField(sarrLiteralFields.get(iCol),cFieldDelimX,cTextDelimX,newLineBecomesWhat_ElseNullForEscapeSequences); 
					iCol++;
				}
			}
			else thisLogger.info("PartialRowToCSVLine cannot convert null row field array to CSV Line");
		}
		catch (Exception e)
		{
			thisLogger.info(e.getMessage());//System.err.println(e.getMessage());
		}
		return sReturn;
	}
	
	public static List<String> getExplodedDelimited(String line, char fieldDelimiter, char textDelimiter) {
		List<String> returnList = new ArrayList<String>();
		int index=0;
		boolean IsQuoted=false;
		int start=0;
		int endEx=1;
		//String thisValue="";
		while ( index <= line.length() ) {
			if ( index==line.length()
				|| (line.charAt(index)==fieldDelimiter && !IsQuoted) ) {
				endEx=index;
				returnList.add(main.CSVFieldToLiteralField(line.substring(start, endEx),fieldDelimiter,textDelimiter));//java substring 2nd param is exclusive index
				start=endEx+1;//+1 to avoid delimiter
				endEx=start+1;//+1 since exclusive
				index=endEx;//skip what we already know
			}
			else if (line.charAt(index)==textDelimiter) {
				IsQuoted=!IsQuoted;
			}
			index++;
		}
		return returnList;
	}
	
	public static void broadcast(String msg) {
		if (thisServer!=null) thisServer.broadcastMessage(msg);
	}
	
	private void InitializeConfig() {
		config = getConfig();
		//deprecated config.addDefault("WildPortalAdmin.playerName", "Abiyahh");
		config.options().copyDefaults();
		saveConfig();
	}
	
	public void onEnable () {
		plugin = this;
		InitializeConfig();
		getCommand("wildportal").setExecutor(new MultiEventHandler());
		thisLogger = getLogger();//Bukkit.getLogger();
		thisServer=this.getServer();
		thisLogger.info("WildPortal Enabled!");
		registerEvents(this, new MultiEventHandler());
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
	public static Plugin getPlugin() {
		return plugin;
	}
	
	
}//end class main extends JavaPlugin