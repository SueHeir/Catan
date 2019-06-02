package com.dizylizy.game.views;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dizylizy.game.MainGame;
import com.dizylizy.game.controller.KeyboardController;
import com.dizylizy.game.gui.DevelopmentCards;
import com.dizylizy.game.gui.Gui;
import com.dizylizy.game.loader.GameAssetsManager;
import com.dizylizy.game.map.Map;
import com.dizylizy.game.map.Tile;
import com.dizylizy.game.map.Vertex;
import com.dizylizy.game.map.Edge;
import com.dizylizy.game.players.Player;
import com.dizylizy.game.world.B2dWorld;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;



public class MainScreen implements Screen {
	private MainGame parent;
	private B2dWorld model;
	private OrthographicCamera cam;
	private KeyboardController controller;
	private SpriteBatch sb;
	private PolygonSpriteBatch pb;
	private ShapeRenderer sr;
	private BitmapFont font;
	private Stage stage;
	private Skin skin;
	protected Player playerToRemove;
	static private Socket socket;
	private static Player playerMain;
	public static ArrayList<Player> orderedPlayers;
	
	
	
	public MainScreen(MainGame box2d, Player playerMain, ArrayList<Player> orderedPlayers, 
			ArrayList<String> tileType, ArrayList<Integer> tileValue, Socket sockett) {
		parent = box2d;
		this.playerMain=playerMain;
		this.orderedPlayers=orderedPlayers;
		this.socket = sockett;
		
		DevelopmentCards.init();
		
		
		
		// Constructs a new OrthographicCamera, using the given viewport width and height
		// Height is multiplied by aspect ratio.
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(500,500*(h/w));
		cam.translate(250,250);
		cam.update();
		
		
		controller = new KeyboardController();
		
		
		//debugRenderer = new Box2DDebugRenderer(true,true,true,true,true,true);
	
		sb = new SpriteBatch();
		pb = new PolygonSpriteBatch();
		sr = new ShapeRenderer();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		
		stage = new Stage(new ScreenViewport());
		skin = parent.assMan.manager.get("skin/uiskin.json");
		model = new B2dWorld(controller,cam,parent.assMan, tileType, tileValue);
		
		configSocketEvents();
	}
	
	
	@Override
	public void show() {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage); // set stage as first input processor
		multiplexer.addProcessor(controller);  // set your game input precessor as second
		Gdx.input.setInputProcessor(multiplexer);	
		
		Gui.show(stage,skin,parent.assMan,playerMain,orderedPlayers);
		
		
	}

	@Override
	public void render(float delta) {
		
		
		
	
		sb.setProjectionMatrix(cam.combined);
		pb.setProjectionMatrix(cam.combined);
		sr.setProjectionMatrix(cam.combined);
		cam.update();
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		model.logicStep(delta);
		
		sb.begin();
		sb.draw(parent.assMan.manager.get(GameAssetsManager.OCEANImage, Texture.class), -Gdx.graphics.getWidth()/2, -Gdx.graphics.getWidth()/2,  Gdx.graphics.getWidth()*2,Gdx.graphics.getWidth()*2);
		sb.end();
		
		pb.begin();
		B2dWorld.renderpb(delta, pb,cam);
		pb.end(); 
		
		sb.begin();
		sr.begin(ShapeType.Line);
		
		font.getData().setScale(0.5f,0.5f);
		B2dWorld.rendersb(delta,sb,font,sr);
		Gui.rendersb(delta,sb);
		
		sb.end();
		sr.end();
		//debugRenderer.render(model.world, new Matrix4(cam.combined));
		
		// tell our stage to do actions and draw itself
		
		
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		
		if(playerToRemove!=null) {
			//eventually remove player here
			playerToRemove = null;
		}
	}
	
	
	
	public static void mapUpdateVertex() {
		JSONArray data = new JSONArray();
		
		try {int i=0;
			for(Vertex x: Map.VertexList) {
				JSONObject obj = new JSONObject();
				obj.put("BuildingName", x.getBuildingName());
				obj.put("Color", x.getColorString());
				data.put(i,obj);
				i++;
			}
			socket.emit("mapUpdateVertex", data);
		} catch(JSONException e) {
			Gdx.app.log("SOCKET.IO", "Error updated Vertices");
		}
	}
	
	public static void mapUpdateEdge() {
		JSONArray data = new JSONArray();
		
		try {int i=0;
			for(Edge x: Map.EdgeList) {
				JSONObject obj = new JSONObject();
				obj.put("BuildingName", x.getBuildingName());
				obj.put("Color", x.getColorString());
				data.put(i,obj);
				i++;
			}
			socket.emit("mapUpdateEdge", data);
		} catch(JSONException e) {
			Gdx.app.log("SOCKET.IO", "Error updated Edges");
		}
	}
	
	
	static public void gameNextTurn() {	
		JSONObject data = new JSONObject();
		int diceRoll = B2dWorld.rollDice(2);
		try {
			data.put("NextRoll", diceRoll);
			socket.emit("gameNextTurn", data);
			B2dWorld.gameCounter++;
			B2dWorld.gameStartBuiltSet=false;
			B2dWorld.gameDiceRoll = diceRoll;
			
			if(B2dWorld.gameRunning||B2dWorld.gameCounter==0||B2dWorld.gameCounter==8) {
				B2dWorld.giveResorces(diceRoll);
				if(diceRoll==7) {
				for(Player player: orderedPlayers) {
					if(player.getTotalCardCount()>7) {
						int add = (int)Math.floor(player.getTotalCardCount() /2);
						B2dWorld.gameCheatCounter=B2dWorld.gameCheatCounter+add;
						updateCheatCounter();
					}
				}
			}
			}
			
			
			
			
			
		} catch(Exception e) {
			
		}
	}
	
	static public void mapUpdateRobber(int x,int y) {	
		JSONObject data = new JSONObject();
		try {
			data.put("XCoord", x);
			data.put("YCoord", y);
			socket.emit("mapUpdateRobber", data);
			
			
		} catch(Exception e) {
			
		}
	}
	
	static public void updateDCardList(String string) {	
		JSONObject data = new JSONObject();
		try {
			data.put("dCard", string);
			socket.emit("updateDCardList", data);
		} catch(Exception e) {
			
		}
	}
	static public void playerUpdateInv() {	
		JSONObject data = new JSONObject();
		try {
			data.put("wood", playerMain.getWood());
			data.put("wool", playerMain.getWool());
			data.put("wheat", playerMain.getWheat());
			data.put("rock", playerMain.getRock());
			data.put("brick", playerMain.getBrick());
			socket.emit("playerUpdateInv", data);
		} catch(Exception e) {
			
		}
	}
	
	static public void otherPlayerUpdateInv(Player player) {	
		JSONObject data = new JSONObject();
		try {
			data.put("id", player.getID());
			data.put("wood", player.getWood());
			data.put("wool", player.getWool());
			data.put("wheat", player.getWheat());
			data.put("rock", player.getRock());
			data.put("brick", player.getBrick());
			socket.emit("otherPlayerUpdateInv", data);
		} catch(Exception e) {
			
		}
	}
	
	static public void updateCheatCounter() {	
		JSONObject data = new JSONObject();
		try {
			data.put("cheatCounter", B2dWorld.gameCheatCounter);
			socket.emit("updateCheatCounter", data);
		} catch(Exception e) {
			
		}
	}
	
	private void configSocketEvents() {
		LobbyScreen.getSocket().on("gameNextTurn", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				
				B2dWorld.gameCounter++;
				B2dWorld.gameStartBuiltSet=false;
				
				try {
					int roll = data.getInt("NextRoll");
					B2dWorld.gameDiceRoll=roll;
					if(B2dWorld.gameRunning || B2dWorld.gameCounter==8||B2dWorld.gameCounter==0) {
						B2dWorld.giveResorces(roll);
						
						if(roll==7) {
							B2dWorld.canMoveRobber=true;
						}
					}
				
							
				} catch(JSONException e) {
					
				}			
				
				
				
			}
		}).on("mapUpdateVertex", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONArray objects = (JSONArray) args[0];
				try {
					for(int i = 0;i < objects.length();i++) {
						
						
						Map.VertexList.get(i).setBuildingName(objects.getJSONObject(i).getString("BuildingName"));
						if(!Map.VertexList.get(i).getBuildingName().equals("")) {
							Map.VertexList.get(i).setIsFilled(true);
							for(Vertex y: Map.VertexList.get(i).getAdjacentVertex()) {
								y.setIsFilled(true);
							}
							Map.VertexList.get(i).setColorString(objects.getJSONObject(i).getString("Color"));
						}
					}
					
				} catch(JSONException e) {
					
				}
				
			}
		}).on("mapUpdateEdge", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONArray objects = (JSONArray) args[0];
				try {
					for(int i = 0;i < objects.length();i++) {
						Map.EdgeList.get(i).setBuildingName(objects.getJSONObject(i).getString("BuildingName"));
						
						if(!(Map.EdgeList.get(i).getBuildingName().equals(""))) {
							Map.EdgeList.get(i).setIsFilled(true);
							Map.EdgeList.get(i).setColorString(objects.getJSONObject(i).getString("Color"));
						
						}
					}
					
				} catch(JSONException e) {
					
				}
				
			}
		}).on("mapUpdateRobber", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					for(Tile x:Map.TileList) {
						x.setBuildingName("");
					}
					
					Map.tiles[data.getInt("XCoord")][data.getInt("YCoord")].setBuildingName("ROBBER");
					B2dWorld.canMoveRobber=false;
					
					
				} catch(JSONException e) {
					
				}
				
			}
		}).on("playerUpdateInv", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					for(Player x: orderedPlayers) {
						if(x.getID().equals(data.get("id"))) {
							x.setBrick(data.getInt("brick"));
							x.setWood(data.getInt("wood"));
							x.setWheat(data.getInt("wheat"));
							x.setRock(data.getInt("rock"));
							x.setWool(data.getInt("wool"));
						}
							
					}	
					
				} catch(JSONException e) {
					
				}
				
			}
		}).on("updateCheatCounter", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					B2dWorld.gameCheatCounter = data.getInt("cheatCounter");
					
				} catch(JSONException e) {
					
				}
				
			}
		}).on("updateDCardList", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					 DevelopmentCards.removeDCard(data.getString("dCard"));
					
				} catch(JSONException e) {
					
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
						for(Player x: orderedPlayers) {
							if(x.getID().equals(id)) {
								playerToRemove=x;
								System.out.println("Player Disconnected");
							}
						}
						
					} catch (JSONException e) {
						Gdx.app.log("SocketIO", "Error player Disconnect");			
					}
				}
		});
		
	}

	
	

	@Override
	public void resize(int width, int height) {
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
	
	public static Player getPlayerByColor(Color color) {
		for(Player x: orderedPlayers) {
			if(x.getColor()==color) {
				return x;
			}
		}
		
		return null;
	}
	
	
	public static Player getPlayerMain() {
		return playerMain;
	}
	
}
