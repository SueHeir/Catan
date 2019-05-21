package com.dizylizy.game.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.dizylizy.game.world.B2dWorld;

public class KeyboardController  implements InputProcessor {
	public boolean left,right,up,down;
	public boolean isMouse1Down, isMouse2Down,isMouse3Down;
	public boolean isDragged;
	public Vector2 mouseLocation = new Vector2(0,0);

	@Override
	public boolean keyDown(int keycode) {
		boolean keyProcessed = false;
		if(keycode==Keys.A) {
			left = true;	// do this
	        keyProcessed = true;	// we have reacted to a keypress 
	        
		}
		if(keycode==Keys.D) {
            right = true;	// do this
            keyProcessed = true;	// we have reacted to a keypress 
            
		} 
		if(keycode==Keys.W) {
            up = true;		// do this
            keyProcessed = true;	// we have reacted to a keypress 
            
		}
		if(keycode==Keys.S) {
			down = true;	// do this
            keyProcessed = true;
		}
	        
		return keyProcessed;	//  return our peyProcessed flag
	}
	@Override
	public boolean keyUp(int keycode) {
		boolean keyProcessed = false;
		if(keycode==Keys.A) {
			left = false;	// do this
	        keyProcessed = true;	// we have reacted to a keypress 
	        
		}
		if(keycode==Keys.D) {
            right = false;	// do this
            keyProcessed = true;	// we have reacted to a keypress 
            
		}
		if(keycode==Keys.W) {
            up = false;		// do this
            keyProcessed = true;	// we have reacted to a keypress 
            
		}
		if(keycode==Keys.S) {
			down = false;	// do this
            keyProcessed = true;
		}
	        
		return keyProcessed;	//  return our peyProcessed flag
	}
	@Override
	public boolean keyTyped(char character) {
	
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == 0){
			isMouse1Down = true;
		}else if(button == 1){
			isMouse2Down = true;
		}else if(button == 2){
			isMouse3Down = true;
		}
		mouseLocation.x = screenX;
		mouseLocation.y = screenY;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		isDragged = false;
		//System.out.println(button);
		if(button == 0){
			isMouse1Down = false;
		}else if(button == 1){
			isMouse2Down = false;
		}else if(button == 2){
			isMouse3Down = false;
		}
		mouseLocation.x = screenX;
		mouseLocation.y = screenY;
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		isDragged = true;
		mouseLocation.x = screenX;
		mouseLocation.y = screenY;
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mouseLocation.x = screenX;
		mouseLocation.y = screenY;
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public boolean scrolled(int amount) {
		B2dWorld.setCameraZoom(amount);
		return true;
	}
	
	
	
}
