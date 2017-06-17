/**
 * 
 */
package me.expertmm.WildPortal;

/**
 * @author Jacob Gustafson
 *
 */
public class FrameworkDummy {
	public static String Substring(String val, int start, int count) {
		return val.substring(start, count-start);
	}

	public static boolean Convert_ToBoolean(String val) {
		return !  ( (val==null) || (val.length()==0) || val.equalsIgnoreCase("false") || val.equalsIgnoreCase("no") || val.equalsIgnoreCase("0"));
	}
}
