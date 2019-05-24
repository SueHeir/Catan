package com.dizylizy.game;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.dizylizy.game.loader.GameAssetsManager;
import com.dizylizy.game.players.Player;
import com.dizylizy.game.views.*;

import io.socket.client.Socket;



public class MainGame extends Game {
	
	private LoadingScreen loadingScreen;
	private LobbyScreen lobbyScreen;
	private MenuScreen menuScreen;
	private MainScreen mainScreen;
	private EndScreen endScreen;
	private AppPreferences preferences;
	public GameAssetsManager assMan = new GameAssetsManager();

	 
	public final static int MENU = 0;
	public final static int LOBBY = 1;
	public final static int APPLICATION = 2;
	public final static int ENDGAME = 3;
	
	@Override
	public void create () {
		
		loadingScreen = new LoadingScreen(this);
		preferences = new AppPreferences();
		setScreen(loadingScreen);
		
		// tells our asset manger that we want to load the images set in loadImages method
		assMan.queueAddMusic();
		// tells the asset manager to load the images and wait until finished loading.
		assMan.manager.finishLoading();
		// loads the 2 sounds we use

	}
	public void changeScreen(int screen) {
		changeScreen(screen,null,null,null);
	}
	
	public void changeScreen(int screen, String address, String name, String colorOfPlayer){
		switch(screen){
			case MENU:
				if(menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case LOBBY:
				if(lobbyScreen == null) lobbyScreen = new LobbyScreen(this,address,name,colorOfPlayer);
				this.setScreen(lobbyScreen);
				break;
			case ENDGAME:
				if(endScreen == null) endScreen = new EndScreen(this);
				this.setScreen(endScreen);
				break;
		}
	}
	
	public AppPreferences getPreferences(){
		return this.preferences;
	}
	
	@Override
	public void dispose(){
		assMan.manager.dispose();
	}
	public void changeScreen(int application2, ArrayList<Player> orderedPlayers, Player playerMain,
			ArrayList<String> tileType, ArrayList<Integer> tileValue, Socket socket) {
			if(application2==APPLICATION) {
				if(mainScreen == null) mainScreen = new MainScreen(this, playerMain, orderedPlayers, tileType, tileValue, socket);
				this.setScreen(mainScreen);
			}
	}
	
	
	 
}
