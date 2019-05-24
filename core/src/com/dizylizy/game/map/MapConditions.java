package com.dizylizy.game.map;


import java.util.ArrayList;
import java.util.Random;



public class MapConditions {
	
	
	static int MUD = 3, DESERT = 1, FOREST = 4, WHEAT = 4, PLAINS = 4, MOUNTAIN = 3;
	
	static Random rand = new Random();
	
	static ArrayList<Integer> numbers = new ArrayList<Integer>();

	static int[] numberlist = {2,3,3,4,4,5,5,6,6,8,8,9,9,10,10,11,11,12};
	public static void init(){
		for(int x:numberlist) {
			numbers.add(x);
		}
	}
	
	
	

	public static String randomTileType (int j, int i) {
	
		if(j==0 || j==6 || i==0 || i==6 || (j==1 && i==1) ||(j==2 && i==1) ||(j==4 && i==1) ||(j==5 && i==1) ||(j==1 && i==5) ||(j==5 && i==5)) {
			return "OCEAN";
		}
		int number = rand.nextInt(6)+1;
		
		
		return tileReturn(number);
		 
	}
	
	public static String tileReturn(int i) {
		
		switch(i) {
		case 1:
			if(MUD>0) {
				MUD--;
				return "MUD";
			} else {
				return tileReturn(i+1);
			}
		case 2:
			if(DESERT>0) {
				DESERT--;
				return "DESERT";
			}else {
				return tileReturn(i+1);
			}
		case 3:
			if(FOREST>0) {
				FOREST--;
				return "FOREST";
			}else {
				return tileReturn(i+1);
			}
		case 4:
			if(WHEAT>0) {
				WHEAT--;
				return "WHEAT";
			}else {
				return tileReturn(i+1);
			}
		case 5:
			if(MOUNTAIN>0) {
				MOUNTAIN--;
				return "MOUNTAIN";
			}else {
				return tileReturn(i+1);
			}
		case 6:
			if(PLAINS>0) {
				PLAINS--;
				return "PLAINS";
			}else {
				return tileReturn(1);
			}
		}
		return "PLAINS";
	}

	public static int randomNumber(int j, int i, String type) {
		if(j==0 || j==6 || i==0 || i==6 || (j==1 && i==1) ||(j==2 && i==1) ||(j==4 && i==1) ||(j==5 && i==1) ||(j==1 && i==5) ||(j==5 && i==5)) {
			return 0;
		}
		if(type=="DESERT") {
			return 0;
		}
		
		
		int randNumber = rand.nextInt(numbers.size());
		
		int usedInt = numbers.get(randNumber);
		numbers.remove(numbers.get(randNumber));
		
		return usedInt;
		 
	}
	
	
 
	
}
