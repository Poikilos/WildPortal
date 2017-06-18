/**
 * 
 */
package me.expertmm.WildPortal;

//import org.bukkit.Location;

/**
 * @author Jacob Gustafson
 *
 */
public class GroundRect {
	public String worldName=null;
	public double yaw=0.0;
	public int x=0;
	public int z=0;
	public int width=1;
	public int depth=1;
	public String key=null;
	
	public GroundRect(int set_X, int set_Z, int set_Width, int set_Depth) {
		this.x=set_X;
		this.z=set_Z;
		this.width=set_Width;
		this.depth=set_Depth;
	}	
	public int getLeft() {
		return this.x;
	}
	public int getTop() {
		return this.z;
	}
	public int getRight() {
		return this.x+this.width;
	}
	public int getRightInclusive() {
		return (this.width>0)?this.x+this.width-1:this.x; //for example if width is 1, return X
	}
	public int getBottom() {
		return this.z+this.depth;
	}
	public int getBottomInclusive() {
		return (this.depth>0)?this.z+this.depth-1:this.z; //for example if height is 1, return X
	}
	public void SetInclusive(int set_Left, int set_Top, int set_Right, int set_Bottom) {
		setRightInclusive(set_Left,set_Right);
		setBottomInclusive(set_Top,set_Bottom);
	}
	public void setRight(int set_Left, int set_Right) {
		this.x=set_Left;
		this.width=set_Right-set_Left;
	}
	public void setRightInclusive(int set_Left, int set_Right) {
		this.x=set_Left;
		this.width=set_Right-set_Left+1;
	}
	public void setBottom(int set_Top, int set_Bottom) {
		this.z=set_Top;
		this.width=set_Bottom-set_Top;
	}
	public void setBottomInclusive(int set_Top, int set_Bottom) {
		this.z=set_Top;
		this.width=set_Bottom-set_Top+1;
	}
	public String toString() {
		return "{"+((worldName!=null)?("world:"+worldName+"; "):"")+"x:" + String.valueOf(x) + ", z:" + String.valueOf(z) +
				", width:" + String.valueOf(width) + ", depth:" + String.valueOf(depth) + "}";
	}
	public String toString(String delimiter) {
		if (delimiter==null) {
			delimiter=",";
			main.logWriteLine("WARNING: Delimiter was null in toString(delimiter), so used '"+delimiter+"' instead.");
		}
		return ((worldName!=null)?("world:"+worldName+"; "):"")
				+delimiter+String.valueOf(x)
				+delimiter+String.valueOf(z)
				+delimiter+String.valueOf(width)
				+delimiter+String.valueOf(depth);
	}
	public String getCenterString() {
		return Integer.toString(x+width/2)+","+Integer.toString(z+depth/2);
	}
	public double getCenterX() {
		return x+width/2;
	}
	public double getCenterZ() {
		return z+depth/2;
	}
}
