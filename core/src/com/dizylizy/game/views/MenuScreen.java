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
		// Create a table that fills the screen. Everything else will go inside this table.
		Table table = new Table();
		table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);
        
    	
        
        
        //create buttons
        TextButton newGame = new TextButton("New Game", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);
        
        Label nameLabel = new Label("Name:",skin);
        final TextField nameText = new TextField("", skin);
        Label addressLabel = new Label("Address:",skin);
        final TextField addressText = new TextField("", skin);
        addressText.setText("https://localhost:8080");
        address = "http://localhost:8080";	
        colorOfPlayer = "Brown";
        final SelectBox<String> colorBox = new SelectBox<String>(skin);
        colorBox.setItems("Brown","Red","White","Blue","Green","Orange");
        
        //add buttons to table
        table.add(newGame).fillX().uniformX();
		table.row().pad(10, 0, 10, 0);
		table.add(nameLabel);
		table.add(nameText);
		table.add(colorBox);
		table.row();
		table.add(addressLabel);
		table.add(addressText);
		table.row();
		table.add(preferences).fillX().uniformX();
		table.row();
		table.add(exit).fillX().uniformX();
		
		
		nameText.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				name = nameText.getText();				
			}

		});
		
		addressText.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				address = addressText.getText();				
			}

		});
		
		colorBox.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				colorOfPlayer = colorBox.getSelected();	
				System.out.println(colorOfPlayer);
			}

		});
		
		
		// create button listeners
		exit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();	
				System.exit(0);
			}
		});
		
		newGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				if(name!=null && address!=null ) {
					
					parent.changeScreen(MainGame.APPLICATION,address,name,colorOfPlayer);
				}
			}
		});
		
		preferences.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(MainGame.PREFERENCES, null, null,null);
			}
		});
		
	}

	@Override
	public void render(float delta) {
		// clear the screen ready for next set of images to be drawn
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// tell our stage to do actions and draw itself
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
