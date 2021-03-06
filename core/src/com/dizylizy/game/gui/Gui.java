package com.dizylizy.game.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.dizylizy.game.loader.GameAssetsManager;
import com.dizylizy.game.players.Player;
import com.dizylizy.game.views.MainScreen;
import com.dizylizy.game.world.B2dWorld;

public class Gui {
	
	
	 static Label woodLabel;
	 static Label woolLabel;
	 static Label wheatLabel;
	 static Label rockLabel;
	 static Label brickLabel;
	 
     static TextButton diceRoll;
     static TextButton cheat;
     

     static Table nextTurnTable;
     static Table devCardTable;
     static Table playerTable;
     
     static ArrayList<TextButton> ownedDevCards;
     static ArrayList<TextButton> playerButtons;
		    

	public static void show(Stage stage, final Skin skin, GameAssetsManager assMan, Player playerMain, ArrayList<Player> otherPlayerList) {
		ownedDevCards = new ArrayList<TextButton>();
		playerButtons = new ArrayList<TextButton>();
		
		Table exitTable = new Table();
		exitTable.setFillParent(true);
		exitTable.left().bottom();
		stage.addActor(exitTable);
		
		
		devCardTable = new Table();
		devCardTable.setFillParent(true);
		devCardTable.center().left();
		stage.addActor(devCardTable);
		
		nextTurnTable = new Table();
		nextTurnTable.setFillParent(true);
		nextTurnTable.bottom().right();
		stage.addActor(nextTurnTable);
		
		Table invTable = new Table();
		invTable.setFillParent(true);
        invTable.setDebug(false);
        invTable.center().bottom();
        stage.addActor(invTable);
        
        Table cheatTable = new Table();
		cheatTable.setFillParent(true);
        cheatTable.setDebug(false);
        cheatTable.top().left();
        stage.addActor(cheatTable);
        
        playerTable = new Table();
        playerTable.setFillParent(true);
        playerTable.center().right();
        stage.addActor(playerTable);
        
        int i = 0;
        for(final Player x: MainScreen.orderedPlayers) {
        	playerButtons.add(new TextButton(x.getName()+": "+x.getTotalCardCount(),skin));
        	
        	playerButtons.get(i).addListener(new ChangeListener() {
    			@Override
    			public void changed(ChangeEvent event, Actor actor) {
    				if(B2dWorld.gameRunning && MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).getID().equals(MainScreen.getPlayerMain().getID())) {
    					
    					if(x.getCanStealFrom()) {
    						for(Player y: MainScreen.orderedPlayers) {
    							y.setCanStealFrom(false);
    						}
    						String card = x.getRandomCard();
    						x.removeCard(card);
    						MainScreen.otherPlayerUpdateInv(x);
    						MainScreen.getPlayerMain().addCard(card);
    						MainScreen.playerUpdateInv();
    					}
    				}
    				
    				
    			}
    		});
        	
        	playerTable.add(playerButtons.get(i));
        	playerTable.row();
        	i++;
        }
        
        
   
        
        cheat = new TextButton("Cheat Count: 0",skin);
       
              
        
        TextureRegionDrawable wooddraw = new TextureRegionDrawable(new TextureRegion(assMan.manager.get(assMan.WOODImage, Texture.class)));
        wooddraw.setMinHeight(70);
        wooddraw.setMinWidth(70);
        TextureRegionDrawable wooldraw = new TextureRegionDrawable(new TextureRegion(assMan.manager.get(assMan.WOOLImage, Texture.class)));
        wooldraw.setMinHeight(70);
        wooldraw.setMinWidth(70);
        TextureRegionDrawable wheatdraw = new TextureRegionDrawable(new TextureRegion(assMan.manager.get(assMan.WHEATitemImage, Texture.class)));
        wheatdraw.setMinHeight(70);
        wheatdraw.setMinWidth(70);
        TextureRegionDrawable rockdraw = new TextureRegionDrawable(new TextureRegion(assMan.manager.get(assMan.ROCKImage, Texture.class)));
        rockdraw.setMinHeight(70);
        rockdraw.setMinWidth(70);
        TextureRegionDrawable brickdraw = new TextureRegionDrawable(new TextureRegion(assMan.manager.get(assMan.BRICKImage, Texture.class)));
        brickdraw.setMinHeight(70);
        brickdraw.setMinWidth(70);
        
        
        //create buttons
        TextButton developmentCard = new TextButton("D Card", skin);
        developmentCard.setColor(MainScreen.getPlayerMain().getColor());
        TextButton exit = new TextButton("Exit", skin);
        TextButton start = new TextButton("Start", skin);
        TextButton nextTurn = new TextButton("Next Turn",skin);
        diceRoll = new TextButton("Last Roll: ",skin);
       
        ImageButton wood = new ImageButton(wooddraw);
        ImageButton wool = new ImageButton(wooldraw);
        ImageButton wheat = new ImageButton(wheatdraw);
        ImageButton rock = new ImageButton(rockdraw);
        ImageButton brick = new ImageButton(brickdraw);
        
        
	    woodLabel = new Label("",skin);
	    woolLabel = new Label("",skin);
	    wheatLabel = new Label("",skin);
	    rockLabel = new Label("",skin);
	    brickLabel = new Label("",skin);
		    
     
        
        
        //add buttons to table
        invTable.add(developmentCard).fillX().width(100).height(100);
        invTable.add(woodLabel).height(100);
		invTable.add(wood).fillX().width(100).height(100);
		invTable.add(brickLabel).height(100);
		invTable.add(brick).fillX().width(100).height(100);
		invTable.add(wheatLabel).height(100);
		invTable.add(wheat).fillX().width(100).height(100);
		invTable.add(rockLabel).height(100);
		invTable.add(rock).fillX().width(100).height(100);
		invTable.add(woolLabel).height(100);
		invTable.add(wool).fillX().width(100).height(100);
		
		exitTable.add(exit).width(50).height(50);
		
		cheatTable.add(cheat).width(150).height(50);
		
		nextTurnTable.add(nextTurn).height(50);
		nextTurnTable.add(diceRoll).height(50);
		
		// create button listeners
		developmentCard.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(B2dWorld.gameRunning && MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).getID().equals(MainScreen.getPlayerMain().getID())) {
					if(MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).getRock()>=1 &&
						MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).getWheat()>=1 &&
						MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).getWool()>=1) {
					
						
						MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).setRock(MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).getRock()-1);
						MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).setWheat(MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).getWheat()-1);
						MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).setWool(MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).getWool()-1);
					
						
						String dcard = DevelopmentCards.getDCard();
					
						MainScreen.getPlayerMain().addDCard(dcard);
						MainScreen.updateDCardList(dcard);
						updateDisplayTable(skin);
					}
				}
			    
			}

			
		});
		
		
		exit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();	
				System.exit(0);
			}
		});
		
		
		nextTurn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(B2dWorld.gameRunning && MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).getID().equals(MainScreen.getPlayerMain().getID())) {
					MainScreen.gameNextTurn();
				}
				
				
			}
		});
		
		wood.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(MainScreen.getPlayerMain()!=null) {
					MainScreen.getPlayerMain().setWood(MainScreen.getPlayerMain().getWood()+1);
					MainScreen.playerUpdateInv();
					B2dWorld.gameCheatCounter++;
					MainScreen.updateCheatCounter();
					
				}
				
			}
		});
		wood.addListener(new ClickListener(Buttons.RIGHT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(MainScreen.getPlayerMain()!=null) {
					MainScreen.getPlayerMain().setWood(MainScreen.getPlayerMain().getWood()-1);
					MainScreen.playerUpdateInv();
					B2dWorld.gameCheatCounter--;
					MainScreen.updateCheatCounter();
					
				}
				
			}
		});
		
		brick.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(MainScreen.getPlayerMain()!=null) {
					MainScreen.getPlayerMain().setBrick(MainScreen.getPlayerMain().getBrick()+1);
					MainScreen.playerUpdateInv();
					B2dWorld.gameCheatCounter++;
					MainScreen.updateCheatCounter();
				}
				
			}
		});
		brick.addListener(new ClickListener(Buttons.RIGHT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(MainScreen.getPlayerMain()!=null) {
					MainScreen.getPlayerMain().setBrick(MainScreen.getPlayerMain().getBrick()-1);
					MainScreen.playerUpdateInv();
					B2dWorld.gameCheatCounter--;
					MainScreen.updateCheatCounter();
				}
				
			}
		});
		wool.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(MainScreen.getPlayerMain()!=null) {
					MainScreen.getPlayerMain().setWool(MainScreen.getPlayerMain().getWool()+1);
					MainScreen.playerUpdateInv();
					B2dWorld.gameCheatCounter++;
					MainScreen.updateCheatCounter();
				}
				
			}
		});
		wool.addListener(new ClickListener(Buttons.RIGHT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(MainScreen.getPlayerMain()!=null) {
					MainScreen.getPlayerMain().setWool(MainScreen.getPlayerMain().getWool()-1);
					MainScreen.playerUpdateInv();
					B2dWorld.gameCheatCounter--;
					MainScreen.updateCheatCounter();
				}
				
			}
		});
		wheat.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(MainScreen.getPlayerMain()!=null) {
					MainScreen.getPlayerMain().setWheat(MainScreen.getPlayerMain().getWheat()+1);
					MainScreen.playerUpdateInv();
					B2dWorld.gameCheatCounter++;
					MainScreen.updateCheatCounter();
				}
				
			}
		});
		wheat.addListener(new ClickListener(Buttons.RIGHT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(MainScreen.getPlayerMain()!=null) {
					MainScreen.getPlayerMain().setWheat(MainScreen.getPlayerMain().getWheat()-1);
					MainScreen.playerUpdateInv();
					B2dWorld.gameCheatCounter--;
					MainScreen.updateCheatCounter();
				}
				
			}
		});
		rock.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(MainScreen.getPlayerMain()!=null) {
					MainScreen.getPlayerMain().setRock(MainScreen.getPlayerMain().getRock()+1);
					MainScreen.playerUpdateInv();
					B2dWorld.gameCheatCounter++;
					MainScreen.updateCheatCounter();
				}
				
			}
		});
		rock.addListener(new ClickListener(Buttons.RIGHT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(MainScreen.getPlayerMain()!=null) {
					MainScreen.getPlayerMain().setRock(MainScreen.getPlayerMain().getRock()-1);
					MainScreen.playerUpdateInv();
					B2dWorld.gameCheatCounter--;
					MainScreen.updateCheatCounter();
				}
				
			}
		});		
	}

	public static void rendersb(float delta, SpriteBatch sb) {
		if(MainScreen.getPlayerMain()!=null) {
			woodLabel.setText(""+MainScreen.getPlayerMain().getWood());
			woolLabel.setText(""+MainScreen.getPlayerMain().getWool());
			wheatLabel.setText(""+MainScreen.getPlayerMain().getWheat());
			rockLabel.setText(""+MainScreen.getPlayerMain().getRock());
			brickLabel.setText(""+MainScreen.getPlayerMain().getBrick());
		}
		
		int i = 0;
		for(Player  x: MainScreen.orderedPlayers) {
			if(B2dWorld.gameStart) {
				if(x==MainScreen.orderedPlayers.get(B2dWorld.actualCounter)) {
					if(x.getColor()==Color.WHITE) {
						playerButtons.get(i).setColor(Color.GOLD);
					} else {
						playerButtons.get(i).setColor(x.getColor());
					}
				} else {
					playerButtons.get(i).setColor(Color.WHITE);
				}
				i++;
			}
			if(B2dWorld.gameRunning) {
				if(x==MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4)) {
					if(x.getColor()==Color.WHITE) {
						playerButtons.get(i).setColor(Color.GOLD);
					} else {
						playerButtons.get(i).setColor(x.getColor());
					}
				} else {
					playerButtons.get(i).setColor(Color.WHITE);
				}
				
				playerButtons.get(i).setText(x.getName()+ ": "+x.getTotalCardCount());
				i++;
			}
		}
		
		
		diceRoll.setText("Last Roll: "+ B2dWorld.gameDiceRoll);
		cheat.setText("Cheat Count: "+B2dWorld.gameCheatCounter);
		
		
				
		
		
	}
	
	
	private static void updateDisplayTable(final Skin skin) {
		if(ownedDevCards!=null)
		ownedDevCards.clear();
		for(final String x:MainScreen.getPlayerMain().getDevCards()) {
			TextButton tb = new TextButton(x,skin);
			ownedDevCards.add(tb);
			
			tb.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(B2dWorld.gameRunning && MainScreen.orderedPlayers.get(B2dWorld.gameCounter%4).getID().equals(MainScreen.getPlayerMain().getID())) {
						if(x.equals("KNIGHT")) {
							MainScreen.getPlayerMain().removeDCard("KNIGHT");
							B2dWorld.canMoveRobber=true;
							updateDisplayTable(skin);
						}
						if(x.equals("VPOINT")) {
							MainScreen.getPlayerMain().removeDCard("VPOINT");
							updateDisplayTable(skin);
							
						}
						if(x.equals("ROADBUILDER")) {
							MainScreen.getPlayerMain().removeDCard("ROADBUILDER");
							B2dWorld.roadBuilderCount=B2dWorld.roadBuilderCount+2;
							updateDisplayTable(skin);
						}
						if(x.equals("YOFPLENTY")) {
							MainScreen.getPlayerMain().removeDCard("YOFPLENTY");
							B2dWorld.gameCheatCounter=B2dWorld.gameCheatCounter-2;
							MainScreen.updateCheatCounter();
							updateDisplayTable(skin);
						}
						if(x.equals("MONOPOLY")) {
							MainScreen.getPlayerMain().removeDCard("MONOPOLY");
							updateDisplayTable(skin);
						}
					}
					
				}
			});
			
		}
		devCardTable.clear();
		for(TextButton x: ownedDevCards) {
			devCardTable.add(x).height(50).width(100);
			devCardTable.row();
		}
		
		
	}


}
