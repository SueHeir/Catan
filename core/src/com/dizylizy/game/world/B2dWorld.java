package com.dizylizy.game.world;


import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.dizylizy.game.controller.KeyboardController;
import com.dizylizy.game.loader.GameAssetsManager;
import com.dizylizy.game.map.Edge;
import com.dizylizy.game.map.Map;
import com.dizylizy.game.map.Tile;
import com.dizylizy.game.map.Vertex;
import com.dizylizy.game.players.Player;
import com.dizylizy.game.views.MainScreen;


public class B2dWorld {
	public World world;
	private KeyboardController controller;

	static private OrthographicCamera camera;

	public static int[] mapsize= {7,7};
	static public boolean gameQue=true;
	static public boolean gameStart=false;
	static public boolean gameRunning=false;
	static public boolean gameEnd=false;
	static public boolean canMoveRobber=false;
	
	static public int gameCounter=0;
	static public int gameDiceRoll =0;
	static public int gameCheatCounter = 0;
	
	
	static public boolean gameStartBuiltSet = false;
	public static int roadBuilderCount=0;
	
	
	public B2dWorld(KeyboardController cont, OrthographicCamera cam, GameAssetsManager assetManager){
		camera = cam;
		controller = cont;
		world = new World(new Vector2(0,0), true);
		world.setContactListener(new B2dContactListener(this));
		
		
		// get our body factory singleton and store it in bodyFactory
		BodyFactory bodyFactory = BodyFactory.getInstance(world);
		
		/* add a player
		player = bodyFactory.makeCirclePolyBody(0, 0, 2, 0, BodyType.DynamicBody, true);
		*/
		
		Map.init(mapsize,bodyFactory,assetManager);
		
		
		
	}
	
	 
	boolean justClicked = false;
	// our game logic here
	public void logicStep(float delta){
		
		if(!controller.isMouse1Down) {
			justClicked= false;
		}
		
		if(gameQue) {
			gameQue(delta);
		}else if(gameStart) {
			gameStartLogic(delta);
		} else if(gameRunning) {
			gameRunningLogic(delta);
		} else if(gameEnd) {
			gameEndLogic(delta);
		}
		
		if(controller.left){
			camera.position.x -= 3;
			
		}else if(controller.right){
			camera.position.x += 3;
			
		}else if(controller.up){
			camera.position.y += 3;
			
		}else if(controller.down){
			camera.position.y -= 3;
			
		}
		
		world.step(delta , 1, 1);
	}
	
	

	public static void renderpb(float delta, PolygonSpriteBatch pb, OrthographicCamera cam) {
		Map.renderpb(delta,pb,cam);
		
	} 

	public static void rendersb(float delta, SpriteBatch sb,  BitmapFont font, ShapeRenderer sr) {
		Map.rendersb(delta,sb,font,sr);
	}

	
	private void gameQue(float delta) {
		
		
			
		
	}
	int actualCounter;
	private void gameStartLogic(float delta) {
		
		if(gameCounter==4) {  actualCounter = 3;
		}else if(gameCounter==5) {  actualCounter = 2;
		}else if(gameCounter==6) {  actualCounter = 1;
		}else if(gameCounter==7) {  actualCounter = 0;
		}else if(gameCounter==8) {  actualCounter = 0;
			gameStart=false;
			gameRunning=true;
			return;
		}else {
			 actualCounter = gameCounter;
		}
		
		if(gameCounter==8) {
			gameCounter=0;
			
			
		}
	
		for (Vertex x: Map.VertexList) {
			if(pointIntersectsBody(x.getBody(),controller.mouseLocation)){
				x.setHover(true);
				if(MainScreen.getPlayerMain().getID().equals(MainScreen.orderedPlayers[actualCounter].getID())) {
					if(controller.isMouse1Down && !justClicked) {
						if(!x.getHasBuilding()) {
							if(!x.getIsFilled() && !gameStartBuiltSet) {
								x.setColor(MainScreen.getPlayerMain().getColor());
								x.setBuildingName("SETTLEMENT");
								x.setIsFilled(true);
								for(Vertex y: x.getAdjacentVertex()) {
									y.setIsFilled(true);
								}
							
								justClicked = true;
								gameStartBuiltSet = true;
								
								MainScreen.mapUpdateVertex();
								
								
							}
						}
				}
			}
			} else {
				x.setHover(false);
				
			}
			
		}
		
		for (Edge x: Map.EdgeList) {
			if(pointIntersectsBody(x.getBody(),controller.mouseLocation)){
				x.setHover(true);
				
				if(MainScreen.getPlayerMain().getID().equals(MainScreen.orderedPlayers[actualCounter].getID())) {
					if(controller.isMouse1Down && !justClicked) {
						if(!x.getIsFilled() && !x.getHasBuilding() && (x.getAdjacentVertex().get(0).getBuildingName().equals("SETTLEMENT")
											  ||x.getAdjacentVertex().get(1).getBuildingName().equals("SETTLEMENT"))&& (x.getAdjacentVertex().get(0).getColor()==MainScreen.orderedPlayers[actualCounter].getColor()
											  ||x.getAdjacentVertex().get(1).getColor()==MainScreen.orderedPlayers[actualCounter].getColor())) {
							x.setColor(MainScreen.getPlayerMain().getColor());
							x.setBuildingName("ROAD");
							x.setIsFilled(true);
							
							justClicked = true;
							
							MainScreen.mapUpdateEdge();
							MainScreen.gameNextTurn();
						}
					}
				}
			} else {
				x.setHover(false);
			}
			
		}
		
	}
	
	

	private void gameRunningLogic(float delta) {
		
		
		for (Tile x: Map.TileList) {
			if(pointIntersectsBody(x.getBody(),controller.mouseLocation)){
				x.setHover(true);
				if(MainScreen.getPlayerMain().getID().equals(MainScreen.orderedPlayers[(gameCounter%4)].getID()))
				if(controller.isMouse1Down && !justClicked) {
					System.out.println("Clicked");
					if(canMoveRobber) {
						System.out.println("changed");
						canMoveRobber=false;
						for(Tile y:Map.TileList) {
							y.setBuildingName("");
						}
						x.setBuildingName("ROBBER");
						MainScreen.mapUpdateRobber(x.getXCoord(), x.getYCoord());
						
					}
					
				}
			} else {
				x.setHover(false);
			}
			
		}
		
		
		
		for (Vertex x: Map.VertexList) {
			if(pointIntersectsBody(x.getBody(),controller.mouseLocation)){
				x.setHover(true);
				if(MainScreen.getPlayerMain().getID().equals(MainScreen.orderedPlayers[(gameCounter%4)].getID())) {
					if(controller.isMouse1Down && !justClicked) {
						System.out.println("test1");
						if(!x.getHasBuilding() && 
								(x.getAdjacentEdge().get(0).getColor()==MainScreen.getPlayerMain().getColor()
								||x.getAdjacentEdge().get(1).getColor()==MainScreen.getPlayerMain().getColor()
							    ||x.getAdjacentEdge().get(2).getColor()==MainScreen.getPlayerMain().getColor())) {
							System.out.println("test2");
							if(!x.getIsFilled()) {
							    if(MainScreen.getPlayerMain().getBrick()>=1 &&
							       MainScreen.getPlayerMain().getWool()>=1 &&
							   	   MainScreen.getPlayerMain().getWheat()>=1 &&
							       MainScreen.getPlayerMain().getWood()>=1) {
							    	
							    	System.out.println("test3");
							    		MainScreen.getPlayerMain().setBrick(MainScreen.getPlayerMain().getBrick()-1);
							    		MainScreen.getPlayerMain().setWood(MainScreen.getPlayerMain().getWood()-1);
							    		MainScreen.getPlayerMain().setWheat(MainScreen.getPlayerMain().getWheat()-1);
							    		MainScreen.getPlayerMain().setWool(MainScreen.getPlayerMain().getWool()-1);
							    		MainScreen.playerUpdateInv();
							    	
							    	
							    	
		
									x.setColor(MainScreen.getPlayerMain().getColor());
									x.setBuildingName("SETTLEMENT");
									x.setIsFilled(true);
									for(Vertex y: x.getAdjacentVertex()) {
										y.setIsFilled(true);
									}
								
									justClicked = true;
									gameStartBuiltSet = true;
									
									MainScreen.mapUpdateVertex();
								
							    }
							}
						}
						if(x.getBuildingName().equals("SETTLEMENT") && x.getColor()==MainScreen.getPlayerMain().getColor()) {
							if(MainScreen.getPlayerMain().getRock()>=3 &&
							   MainScreen.getPlayerMain().getWheat()>=2) {
								MainScreen.getPlayerMain().setWheat(MainScreen.getPlayerMain().getWheat()-2);
								MainScreen.getPlayerMain().setRock(MainScreen.getPlayerMain().getRock()-3);
								MainScreen.playerUpdateInv();
								x.setBuildingName("CITY");
								justClicked = true;
								
							}
						}
				}
			}
			} else {
				x.setHover(false);
				
			}
			
		}
		
		for (Edge x: Map.EdgeList) {
			if(pointIntersectsBody(x.getBody(),controller.mouseLocation)){
				x.setHover(true);
				
				if(MainScreen.getPlayerMain().getID().equals(MainScreen.orderedPlayers[gameCounter%4].getID())) {
					if(controller.isMouse1Down && !justClicked) {
						
						if(!x.getIsFilled() && !x.getHasBuilding() && 
						  ((x.getAdjacentEdge().get(0).getHasBuilding() && 
					  	    x.getAdjacentEdge().get(0).getColor()==MainScreen.orderedPlayers[gameCounter%4].getColor())
						 ||(x.getAdjacentEdge().get(1).getHasBuilding() && 
						    x.getAdjacentEdge().get(1).getColor()==MainScreen.orderedPlayers[gameCounter%4].getColor())
					     ||(x.getAdjacentEdge().get(2).getHasBuilding() && 
					        x.getAdjacentEdge().get(2).getColor()==MainScreen.orderedPlayers[gameCounter%4].getColor())
					     ||(x.getAdjacentEdge().get(3).getHasBuilding() && 
					        x.getAdjacentEdge().get(3).getColor()==MainScreen.orderedPlayers[gameCounter%4].getColor()))) {
							
							if((MainScreen.getPlayerMain().getBrick()>0 && MainScreen.getPlayerMain().getWood()>0 ) || roadBuilderCount>0) {
								System.out.println("a"+MainScreen.getPlayerMain().getBrick());
								System.out.println("b"+MainScreen.getPlayerMain().getWood());
								System.out.println("c"+roadBuilderCount);
								if(roadBuilderCount>0) {
									roadBuilderCount--;
								} else {
									MainScreen.getPlayerMain().setBrick(MainScreen.getPlayerMain().getBrick()-1);
									MainScreen.getPlayerMain().setWood(MainScreen.getPlayerMain().getWood()-1);
									MainScreen.playerUpdateInv();
								}
								
								
								x.setColor(MainScreen.getPlayerMain().getColor());
								x.setBuildingName("ROAD");
								x.setIsFilled(true);
								
								justClicked = true;
								
								
						
								MainScreen.mapUpdateEdge();
							}
						}
					}
				}
			} else {
				x.setHover(false);
			}
			
		}
		
	}
	
	private void gameEndLogic(float delta) {
		// TODO Auto-generated method stub
		
	}
	
	public static void giveResorces(int diceRoll) {
		
		for(Vertex x: Map.VertexList) {
			if(x.getHasBuilding()) {
				for(Tile y: x.getAdjacentTile()) {
					if(y.getValue()==diceRoll && !y.getBuildingName().equals("ROBBER")) {
						Player player = MainScreen.getPlayerByColor(x.getColor());
						if(y.getType().equals("MUD")) {
							if(x.getBuildingName().equals("SETTLEMENT"))player.setBrick(player.getBrick()+1);
							if(x.getBuildingName().equals("CITY"))player.setBrick(player.getBrick()+2);
						}
						if(y.getType().equals("FOREST")) {
							if(x.getBuildingName().equals("SETTLEMENT"))player.setWood(player.getWood()+1);
							if(x.getBuildingName().equals("CITY"))player.setWood(player.getWood()+2);
						}
						if(y.getType().equals("WHEAT")) {
							if(x.getBuildingName().equals("SETTLEMENT"))player.setWheat(player.getWheat()+1);
							if(x.getBuildingName().equals("CITY"))player.setWheat(player.getWheat()+2);
						}
						if(y.getType().equals("MOUNTAIN")) {
							if(x.getBuildingName().equals("SETTLEMENT"))player.setRock(player.getRock()+1);
							if(x.getBuildingName().equals("CITY"))player.setRock(player.getRock()+2);
						}
						if(y.getType().equals("PLAINS")) {
							if(x.getBuildingName().equals("SETTLEMENT"))player.setWool(player.getWool()+1);
							if(x.getBuildingName().equals("CITY"))player.setWool(player.getWool()+2);
						}
					}
				}
			}
		}
		MainScreen.playerUpdateInv();
	}
	
	
	
	

	public boolean pointIntersectsBody(Body body, Vector2 mouseLocation){
		Vector3 mousePos = new Vector3(mouseLocation,0); //convert mouseLocation to 3D position
		camera.unproject(mousePos); // convert from screen potition to world position
		if(body.getFixtureList().first().testPoint(mousePos.x, mousePos.y)){
			return true;
		}
		return false;
	}
	
	public static void setCameraZoom(float amount) {
		camera.zoom += amount * 0.2f;
	}
	
	public static int rollDice(int i) {
		Random rand = new Random();
		int count =0;
		for(int j=0;j<i;j++) {
		int number = rand.nextInt(6)+1;
		count = number+count;
		}
		return count;
		
	}
	
	
}
