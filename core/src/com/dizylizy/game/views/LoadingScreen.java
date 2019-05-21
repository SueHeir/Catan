package com.dizylizy.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dizylizy.game.MainGame;

public class LoadingScreen implements Screen {
	private MainGame parent;
	
	public final int IMAGE = 0;		// loading images
	public final int FONT = 1;		// loading fonts
	public final int PARTY = 2;		// loading particle effects
	public final int SOUND = 3;		// loading sounds
	public final int MUSIC = 4;		// loading music
	
	private int currentLoadingStage = 0;
	
	// timer for exiting loading screen
	public float countDown = 3f;
	private Stage stage;

	
	 
	public LoadingScreen(MainGame box2dTutorial){
		parent = box2dTutorial;
		stage = new Stage(new ScreenViewport());
		
		loadAssets();
		// initiate queueing of images but don't start loading
		parent.assMan.queueAddImages();
		System.out.println("Loading images....");		
	}
	
	private void loadAssets() {
		// load loading images and wait until finished
		parent.assMan.queueAddLoadingImages();
		parent.assMan.manager.finishLoading();
				
		// get images used to display loading progress

		
	}

	@Override
	public void show() {
		
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    	
		if (parent.assMan.manager.update()) { // Load some, will return true if done loading
			currentLoadingStage+= 1;
			if(currentLoadingStage <= 5){
	
			}
            switch(currentLoadingStage){
            case FONT:
            	System.out.println("Loading fonts....");
            	parent.assMan.queueAddFonts();
            	break;
            case PARTY:	
            	System.out.println("Loading Particle Effects....");
            	parent.assMan.queueAddParticleEffects();
            	break;
            case SOUND:
            	System.out.println("Loading Sounds....");
            	parent.assMan.queueAddSounds();
            	break;
            case MUSIC:
            	System.out.println("Loading fonts....");
            	parent.assMan.queueAddMusic();
            	break;
            case 5:	
            	System.out.println("Finished");
            	break;
            }
	    	if (currentLoadingStage >5){
	    		countDown -= delta;
	    		currentLoadingStage = 5;
	    		if(countDown < 0){
	    			parent.changeScreen(MainGame.MENU);
	    		}
            }
        }
		
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {		
		stage.getViewport().update(width, height,true);
	}

	@Override
	public void pause() {		
	}

	@Override
	public void resume() {		
	}

	@Override
	public void hide() {		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}