package com.dizylizy.game.gui;

import java.util.ArrayList;
import java.util.Random;

public class DevelopmentCards {
	
	static Random rand = new Random();
	static ArrayList<String> dCardList = new ArrayList<String>();
	
	
	
	public static void init() {
		for(int i=0;i<15;i++) {
			dCardList.add(i, "KNIGHT");
		}
		for(int i=15;i<20;i++) {
			dCardList.add(i, "VPOINT");
		}
		for(int i=20;i<22;i++) {
			dCardList.add(i, "ROADBUILDER");
		}
		for(int i=22;i<24;i++) {
			dCardList.add(i, "YOFPLENTY");
		}
		for(int i=24;i<26;i++) {
			dCardList.add(i, "MONOPOLY");
		}
	}
	
	public static String getDCard() {
		
		int randNumber = rand.nextInt(dCardList.size());
		
		String string = dCardList.get(randNumber);
		removeDCard(dCardList.get(randNumber));
		
		return string;
		
	}

	public static void removeDCard(String string) {
		for(String x: dCardList) {
			if(x.equals(string)) {
				dCardList.remove(x);
				return;
			}
		}
		
	}
	

}
