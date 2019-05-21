package com.dizylizy.game.map;

public abstract class MapElement {
	
	private String Name;
	
	public String getName() {
		return Name;
	}
	
	public abstract boolean getHasBuilding();
	public abstract String getBuildingName();
	
	public abstract int getXCoord();
	public abstract int getYCoord();

	public abstract int getID(); 
	
	public abstract boolean justUsed();
	
	
	
}
