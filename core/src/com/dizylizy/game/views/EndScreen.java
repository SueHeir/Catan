package com.dizylizy.game.views;


import com.badlogic.gdx.Screen;
import com.dizylizy.game.MainGame;

public class EndScreen implements Screen {

	private MainGame parent;
	
	public EndScreen(MainGame box2dTutorial){
		setParent(box2dTutorial);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	public MainGame getParent() {
		return parent;
	}

	public void setParent(MainGame parent) {
		this.parent = parent;
	}

}
