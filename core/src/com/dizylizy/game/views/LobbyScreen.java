package com.dizylizy.game.views;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dizylizy.game.MainGame;
import com.dizylizy.game.gui.Gui;
import com.dizylizy.game.map.Map;
import com.dizylizy.game.map.MapConditions;
import com.dizylizy.game.map.Tile;
import com.dizylizy.game.players.Player;
import com.dizylizy.game.world.B2dWorld;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LobbyScreen implements Screen{

	private static MainGame parent;
	private Stage stage;
	protected boolean shouldStartGame = false;
	private static  Socket socket;
	private static Player playerMain;
	private static ArrayList<Player> allPlayerList;
	private static ArrayList<Player> orderedPlayers;
	private static Table playerTable;
	private static ArrayList<String> tileType;
	private static ArrayList<Integer> tileValue;
	
	

	
	public LobbyScreen(MainGame box2dTutorial, String address, String name, String colorOfPlayer){
		//gets MainGame for later use to change screenview
		parent = box2dTutorial;
		
		//Initialize variables
		allPlayerList = new ArrayList<Player>();
		orderedPlayers = new ArrayList<Player>();
		tileType = new ArrayList<String>();
		tileValue = new ArrayList<Integer>();
		
		//create stage and set it as input processor
		stage = new Stage(new ScreenViewport());
		
		//Roll of two dice to see who goes first 
		int whoGoesFirst = B2dWorld.rollDice(2);
		
		//Connects to server with address from MenuScreen
		connectSocket(address);
		
		Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		//configersSocket events for just lobby
		configSocketEvents(name, colorOfPlayer, whoGoesFirst,skin);
		
		//updates others with player name color and a number of to determine who goes first
		playerInfoUpdate(name, colorOfPlayer, whoGoesFirst);
		
	}

	@Override
	public void show() {
		//clears Last Screen's Stage
		stage.clear();
		
		//sets input listener to stage
		Gdx.input.setInputProcessor(stage);
		
		// Create a table that fills the screen.
		playerTable = new Table();
		playerTable.setFillParent(true);
		playerTable.center().center();
		stage.addActor(playerTable);

		// temporary until we have asset manager in
		Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

		//sets up header of table
		playerTable.add(new Label("Name: ",skin));
		playerTable.add(new Label("Color: ",skin));
	}

	@Override
	public void render(float delta) {
		// clear the screen ready for next set of images to be drawn
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// tell our stage to do actions and draw itself
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		
		if(shouldStartGame) {
			startGame();
			shouldStartGame=false;
		}
		
		
	}

	@Override
	public void resize(int width, int height) {
		// change the stage's viewport when the screen size is changed
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		
	}
	

	//called from LobbyScreen Constructor
	private void connectSocket(String address) {
		
		//Tries to connect to socket with address from MenuScreen
		try {
			setSocket(IO.socket(address));
			getSocket().connect();
		} catch(Exception e) {
			System.out.println(e);
		}
		
		
	}
	
	//called from LobbyScreen Constructor
	private void configSocketEvents(final String name, final String colorOfPlayer, final int whoGoesFirst, final Skin skin) {
		
		//Event on Server Connect
		getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "Connected");
				
				//Creates the Main player for this client, then adds it to allPlayerList
				playerMain = new Player();
				playerMain.setColor(colorOfPlayer);
				playerMain.setLastDiceRoll(whoGoesFirst);
				playerMain.setName(name);
				allPlayerList.add(playerMain);
				updateDisplayTable(skin);
			}
			
		//Event on Server sending Socket Information	
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				
				try {
					//gets socket ID and sets Main Players ID 
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "My ID: "+id);
					playerMain.setID(id);
					
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting ID");			
				}	
			}
			
		//Event on Server sending A newly added Player
		//Note the name and color of this player has not yet been sent to server so it is only updating ID
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				
				try {
					//Gets socket ID of new Player and creates new player, then adds it allPlayerList
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "New Player Connected: "+id);
					Player player = new Player();
					player.setID(id);
					allPlayerList.add(player);
					
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting new Player ID");			
				}	
			}

		//Event on player Disconnected 
		}).on("playerDisconnected", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					
					//Removes disconnected Player from Server
					String id = data.getString("id");
					for(Player x: allPlayerList) {
						if(x.getID()==id) {
							allPlayerList.remove(x);
						}
					}
					
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting new Player ID");			
				}
			}
			
		//Event Called when other player calls playerInfoUpdate function
		}).on("playerInfoUpdate", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					for(int i =0; i< allPlayerList.size();i++) {
						if(allPlayerList.get(i).getID().equals(data.getString("id"))) {
							allPlayerList.get(i).setName(data.getString("name"));
							allPlayerList.get(i).setColor(data.getString("colorOfPlayer"));
							allPlayerList.get(i).setLastDiceRoll(data.getInt("diceRoll"));
						}
					}
					updateDisplayTable(skin);
					
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting Name");			
				}	
			}
			
		//Event Called when other player changes color or Name in lobby
		}).on("playerColorNameUpdate", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					for(int i =0; i< allPlayerList.size();i++) {
						if(allPlayerList.get(i).getID().equals(data.getString("id"))) {
							allPlayerList.get(i).setName(data.getString("name"));
							allPlayerList.get(i).setColor(data.getString("colorOfPlayer"));
						}
					}
					updateDisplayTable(skin);
					
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting Name");			
				}	
			}
			
		//Server sends all currently connected players information
		// Including id, name, colorOfPlayer, and lastDiceRoll to determine who goes first
		}).on("getPlayers", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONArray objects = (JSONArray) args[0];
				try {
					for(int i = 0;i < objects.length();i++) {
						Player player = new Player();
						player.setID(objects.getJSONObject(i).getString("id"));
						player.setName(objects.getJSONObject(i).getString("name"));
						player.setColor(objects.getJSONObject(i).getString("colorOfPlayer"));
						player.setLastDiceRoll(objects.getJSONObject(i).getInt("lastDiceRoll"));
						allPlayerList.add(player);
						updateDisplayTable(skin);
						
					}
				} catch(JSONException e) {
					
				}
			}
			
		//Server Tries to send map information to each player,
		//If map information has not been made (on first client connect) then the map i made and sent to the server
		}).on("mapSync", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONArray objects = (JSONArray) args[0];
				try {
					if(objects.length()==0) {
						System.out.println("Updating Server Map");
						mapSync();
						return;
					}
					for(int i = 0;i < objects.length();i++) {
						tileType.add(i, objects.getJSONObject(i).getString("Type"));
						tileValue.add(i, objects.getJSONObject(i).getInt("Value"));
					}
					System.out.println("Server Map Updated");
				} catch(JSONException e) {
				
				}
			}
			
		//Event when another client clicks start game	
		}).on("startGame", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				
				try {
					shouldStartGame=true;
					
				} catch(Exception e) {
					
				}
			}
		});
		
	}
	
	
	private void playerInfoUpdate(String name, String colorOfPlayer, int whoGoesFirst) {
		
		JSONObject data = new JSONObject();
		try {
			//Added name, colorString, and dice roll to new JSON object
			data.put("name", name);
			data.put("colorOfPlayer", colorOfPlayer);
			data.put("diceRoll", whoGoesFirst);
			
			//sends JSON object to server
			getSocket().emit("playerInfoUpdate", data);
		} catch(JSONException e) {
			Gdx.app.log("SOCKET.IO", "Error updating ");
		}
		
	}
	
	private static void updateDisplayTable(Skin skin) {
		
		//Clears table before recreating it
		playerTable.clear();
		
		//sets up header of table
		playerTable.add(new Label("Name: ",skin));
		playerTable.add(new Label("Color: ",skin));
		
		//add element for each Player
		if(allPlayerList.size()>0) {
			for(final Player x: allPlayerList) {
				playerTable.row();
				if(x==playerMain) {
					final TextField nameText = new TextField(x.getName(), skin);
					playerTable.add(nameText);
					
					nameText.addListener(new ChangeListener() {

						@Override
						public void changed(ChangeEvent event, Actor actor) {
							x.setName(nameText.getText());	
							playerColorNameUpdate(x.getColorString(),x.getName());
						}

					});
					
					final SelectBox<String> colorBox = new SelectBox<String>(skin);
			        colorBox.setItems("Brown","Red","White","Blue");
			        colorBox.setSelected(x.getColorString());
			        playerTable.add(colorBox);
			        
			        colorBox.addListener(new ChangeListener() {

						@Override
						public void changed(ChangeEvent event, Actor actor) {
							x.setColor(colorBox.getSelected());
							playerColorNameUpdate(x.getColorString(),x.getName());
						}

					});
			        
				} else {
					playerTable.add(new Label(x.getName()+" ",skin));
					playerTable.add(new Label(x.getColorString(),skin));
				}
				
			}
		}
		
		//Button to start Game
		TextButton startGame = new TextButton("Start Game",skin);
		
		//start game button listener
		startGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				getSocket().emit("startGame", new JSONObject());
				startGame();
			}

			
		});
		
		//Adds start Button to Table 
		playerTable.row().pad(10, 0, 10, 0);
		playerTable.add(startGame);
		
		
	}
	
	protected static void playerColorNameUpdate(String colorString, String name) {
		JSONObject data = new JSONObject();
		try {
			data.put("colorOfPlayer", colorString);
			data.put("name", name);
			socket.emit("playerColorNameUpdate", data);
		} catch(JSONException e){
			
		}
		
	}

	public void mapSync() {
		//Initializes arrays of specific types and values for map
		//both sends them to others and updates current client with info
		MapConditions.init();
		JSONArray data = new JSONArray();
		
		try {
			for(int i=0;i<B2dWorld.mapsize[0];i++) {
				for(int j=0;j<B2dWorld.mapsize[1];j++) {
					JSONObject obj = new JSONObject();
					String type = MapConditions.randomTileType(j,i);
					obj.put("Type", type);
					tileType.add(type);
					int value = MapConditions.randomNumber(j,i,type);
					obj.put("Value", value);
					tileValue.add(value);
					data.put(obj);
				}
			}
			getSocket().emit("mapSync", data);
		} catch(JSONException e) {
			Gdx.app.log("SOCKET.IO", "Error syncing Map");
		}
	}
	
	private static void startGame() {
		reCalcFirstPlayer();
		
		parent.changeScreen(MainGame.APPLICATION, orderedPlayers, playerMain, tileType, tileValue, getSocket());
				
	}
	
	private static void reCalcFirstPlayer() {
		Player firstPlayer = null;
		int maxValue = 0;
		for(Player x: allPlayerList) {
			if(x.getLastDiceRoll()>maxValue) {
				maxValue = x.getLastDiceRoll();
				firstPlayer = x;
			} else if (x.getLastDiceRoll()==maxValue) {
				if(x.getID().charAt(0)>firstPlayer.getID().charAt(0)) {
					firstPlayer = x;
				}
			}
		}
		orderThePlayers(firstPlayer);
	}
	
	
	private static void orderThePlayers(Player player) {
		
		if(allPlayerList.size()==4) {
			if(player.getColor()==Color.BROWN) {
				orderedPlayers.add(0,player);
				orderedPlayers.add(1,getPlayerByColor(Color.BLUE));
				orderedPlayers.add(2,getPlayerByColor(Color.RED));
				orderedPlayers.add(3,getPlayerByColor(Color.WHITE));
			}
			if(player.getColor()==Color.BLUE) {
				orderedPlayers.add(0,player);
				orderedPlayers.add(1,getPlayerByColor(Color.RED));
				orderedPlayers.add(2,getPlayerByColor(Color.WHITE));
				orderedPlayers.add(3,getPlayerByColor(Color.BROWN));
			}
			if(player.getColor()==Color.RED) {
				orderedPlayers.add(0,player);
				orderedPlayers.add(1,getPlayerByColor(Color.WHITE));
				orderedPlayers.add(2,getPlayerByColor(Color.BROWN));
				orderedPlayers.add(3,getPlayerByColor(Color.BLUE));
			}
			if(player.getColor()==Color.WHITE) {
				orderedPlayers.add(0,player);
				orderedPlayers.add(1,getPlayerByColor(Color.BROWN));
				orderedPlayers.add(2,getPlayerByColor(Color.BLUE));
				orderedPlayers.add(3,getPlayerByColor(Color.RED));
			}
		}
	}
	
	public static Player getPlayerByColor(Color color) {
		for(Player x: allPlayerList) {
			if(x.getColor()==color) {
				return x;
			}
		}
		
		return null;
	}

	public static Socket getSocket() {
		return socket;
	}

	public static void setSocket(Socket socket) {
		LobbyScreen.socket = socket;
	}
}
