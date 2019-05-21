package com.dizylizy.game.players;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

public class Player {
	
	private String ID,name; 
	private int wood= 0,wheat = 0, brick = 0,wool = 0,rock = 0;
	private String colorText;
	private Color color;
	private int lastDiceRoll;
	
	static ArrayList<String> playerDCardList;
	
	public Player(){
		playerDCardList = new ArrayList<String>();
	}
	public void setID(String id) {
		this.ID = id;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getWheat() {
		return wheat;
	}

	public void setWheat(int wheat) {
		this.wheat = wheat;
	}

	public int getBrick() {
		return brick;
	}
	
	public int getTotalCardCount() {
		return brick+wood+wool+wheat+rock;
		
	}
	
	public String getID() {
		return ID;
	}

	public void setBrick(int brick) {
		this.brick = brick;
	}

	public int getWool() {
		return wool;
	}

	public void setWool(int wool) {
		this.wool = wool;
	}

	public int getRock() {
		return rock;
	}

	public void setRock(int rock) {
		this.rock = rock;
	}
	public void setName(String name) {
		this.name = name;
		
	}
	public String getName() {
		return name;
	}
	public void setColor(String string) {
		this.colorText = string;
		
		if(colorText.equals("Brown"))this.color = Color.BROWN;
		if(colorText.equals("Blue"))this.color = Color.BLUE;
		if(colorText.equals("White"))this.color = Color.WHITE;
		if(colorText.equals("Red"))this.color = Color.RED;
		if(colorText.equals("Orange"))this.color = Color.ORANGE;
		if(colorText.equals("Green"))this.color = Color.GREEN;
		
		
		
	}
	public Color getColor() {
		// TODO Auto-generated method stub
		return color;
	}
	public void setLastDiceRoll(int int1) {
		this.lastDiceRoll = int1;
		
	}
	
	public int getLastDiceRoll() {
		return lastDiceRoll;
		
	}
	public void addDCard(String dcard) {
		playerDCardList.add(dcard);
	}
	public void removeDCard(String dcard) {
		for (String x:playerDCardList) {
			if(x.equals(dcard)) {
				playerDCardList.remove(x);
				return;
			}
		}
	}
	public ArrayList<String> getDevCards() {
		return playerDCardList;
	}
	
	
}
