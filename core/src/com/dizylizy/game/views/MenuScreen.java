package com.dizylizy.game.views;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dizylizy.game.MainGame;

public class MenuScreen implements Screen{
	
	private MainGame parent;
	private Stage stage;
	private Skin skin;
	private String name,address,colorOfPlayer;
	
	public MenuScreen(MainGame box2dTutorial){
		parent = box2dTutorial;
		
		/// create stage and set it as input processor
		stage = new Stage(new ScreenViewport());
		
		parent.assMan.queueAddSkin();
		parent.assMan.manager.finishLoading();
		skin = parent.assMan.manager.get("skin/uiskin.json");

		
		 
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage); 
		// Create a table that fills the screen. 
		Table table = new Table();
		table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);
        
    	
        
        
        //create Join Lobby and Exit buttons
        TextButton joinLobby = new TextButton("Join Lobby", skin);
        TextButton exit = new TextButton("Exit", skin);
        
        //Create display of Name: [Enter Player Name]
        Label nameLabel = new Label("Name:",skin);
        final TextField nameText = new TextField("", skin);
        
        //Create display of Address: [Enter Address]
        Label addressLabel = new Label("Address:",skin);
        final TextField addressText = new TextField("", skin);
        
        //sets the default address in the address text field
        addressText.setText("http://10.0.0.54:8080");
        address = "http://10.0.0.54:8080";	
        
        
        //Need to remove and add this to lobby window
        colorOfPlayer = "Brown";
        final SelectBox<String> colorBox = new SelectBox<String>(skin);
        colorBox.setItems("Brown","Red","White","Blue");
        
        
        //add Buttons and Text Fields to table
        table.add(joinLobby).fillX().uniformX();
        table.add(exit).fillX().uniformX();
		table.row();
		table.add(nameLabel);
		table.add(nameText);
		
		//need to remove this
		table.add(colorBox);
		
		table.row();
		table.add(addressLabel);
		table.add(addressText);
		
		
		//********* create button listeners****************//
		//Updates name TextField
		nameText.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				name = nameText.getText();				
			}

		});
		
		//Updates address TextField
		addressText.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				address = addressText.getText();				
			}

		});
		
		//Updates color selection
		colorBox.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				colorOfPlayer = colorBox.getSelected();	
			}

		});
		
		//exits application
		exit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();	
				System.exit(0);
			}
		});
		
		//joins lobby
		joinLobby.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				if(name!=null && address!=null ) {
					
					parent.changeScreen(MainGame.LOBBY,address,name,colorOfPlayer);
					//parent.changeScreen(MainGame.APPLICATION,address,name,colorOfPlayer);
				}
			}
		});
		
	}

	@Override
	public void render(float delta) {
		// clear the screen ready for next set of images to be drawn
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// tell our stage to do actions and draw itself
		stage.act(Math.min(delta, 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// change the stage's viewport when teh screen size is changed
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
		// dispose of assets when not needed anymore
		stage.dispose();
	}

}
