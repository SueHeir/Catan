package com.dizylizy.game.world;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class B2dContactListener implements ContactListener {

	private B2dWorld parent;
	
	public B2dContactListener(B2dWorld parent){
		this.setParent(parent);
	}
	
	@Override
	public void beginContact(Contact contact) {
		
	}
	
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {		
	}
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	public B2dWorld getParent() {
		return parent;
	}

	public void setParent(B2dWorld parent) {
		this.parent = parent;
	}

}
