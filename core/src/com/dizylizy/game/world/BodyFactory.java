package com.dizylizy.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyFactory {

	private static BodyFactory thisInstance;
	private World world;
	private final float DEGTORAD = 0.0174533f; 
		
	private BodyFactory(World world){
		this.world = world;
	}
	
	public static BodyFactory getInstance(World world){
		if(thisInstance == null){
			thisInstance = new BodyFactory(world);
		}
		return thisInstance;
	}

	public Body makeBoxPolyBody(float posx, float posy, float width, float height,int material, BodyType bodyType){
		return makeBoxPolyBody(posx, posy, width, height, material, bodyType, false);
	}
	
	public Body makeBoxPolyBody(float posx, float posy, float width, float height,int material, BodyType bodyType, boolean fixedRotation){
		// create a definition
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyType;
		boxBodyDef.position.x = posx;
		boxBodyDef.position.y = posy;
		boxBodyDef.fixedRotation = fixedRotation;
		
		//create the body to attach said definition
		Body boxBody = world.createBody(boxBodyDef);
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width/2, height/2);
		boxBody.createFixture(makeFixture(material,poly));
		poly.dispose();

		return boxBody;
	}
	
	public Body makeCirclePolyBody(float posx, float posy, float radius, int material){
		return makeCirclePolyBody( posx,  posy,  radius,  material,  BodyType.DynamicBody,  false);
	}
	
	public Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyType bodyType){
		return makeCirclePolyBody( posx,  posy,  radius,  material,  bodyType,  false);
	}
	
	public Body makeBullet(float posx, float posy, float radius, int material, BodyType bodyType){
		Body body = makeCirclePolyBody( posx,  posy,  radius,  material,  bodyType,  false);
		for(Fixture fix :body.getFixtureList()){
			fix.setSensor(true);
		}
		body.setBullet(true);
		return body;
	}
	
	
	public Body makeCircleMapElement(float posx,float posy, float radius) {
		// create a definition
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.StaticBody;
		boxBodyDef.position.x = posx;
		boxBodyDef.position.y = posy;
		boxBodyDef.fixedRotation = true;
						//create the body to attach said definition
		Body boxBody = world.createBody(boxBodyDef);
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(radius);
		boxBody.createFixture(makeSensorFixture(0,circleShape));
		circleShape.dispose();
		return boxBody;
	}
	
	public Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyType bodyType, boolean fixedRotation){
		// create a definition
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyType;
		boxBodyDef.position.x = posx;
		boxBodyDef.position.y = posy;
		boxBodyDef.fixedRotation = fixedRotation;
		
		//create the body to attach said definition
		Body boxBody = world.createBody(boxBodyDef);
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(radius /2);
		boxBody.createFixture(makeFixture(material,circleShape));
		circleShape.dispose();
		return boxBody;
	}
	

	
	static public FixtureDef makeFixture(int material, Shape shape){
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.3f;
		fixtureDef.restitution = 0.1f;
			
		return fixtureDef;
	}
	
	public FixtureDef  makeSensorFixture(float size, Shape shape){
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		fixtureDef.shape = shape;
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0f;
		return fixtureDef;
		
		
	}
	
	public void makeConeSensor(Body body, float size){
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		
		
		PolygonShape polygon = new PolygonShape();
		
		float radius = size;
		Vector2[] vertices = new Vector2[5];
		vertices[0] = new Vector2(0,0);
		for (int i = 2; i < 6; i++) {
		    float angle = (float) (i  / 6.0 * 145 * DEGTORAD); // convert degrees to radians
		    vertices[i-1] = new Vector2( radius * ((float)Math.cos(angle)), radius * ((float)Math.sin(angle)));
		}
		polygon.set(vertices);
		//polygon.setRadius(size);
		fixtureDef.shape = polygon;
		body.createFixture(fixtureDef);
		polygon.dispose();
	}
	
	/*
	 * Make a body from a set of vertices
	 */
	public Body makePolygonShapeBody(Vector2[] vertices, float posx, float posy, int material, BodyType bodyType){
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyType;
		boxBodyDef.position.x = posx;
		boxBodyDef.position.y = posy;
		Body boxBody = world.createBody(boxBodyDef);
		
		PolygonShape polygon = new PolygonShape();
		polygon.set(vertices);
		boxBody.createFixture(makeFixture(material,polygon));
		polygon.dispose();
		
		return boxBody;
	}
	
	public void makeAllFixturesSensors(Body bod){
		for(Fixture fix :bod.getFixtureList()){
			fix.setSensor(true);
		}
	}
	
	public void setAllFixtureMask(Body bod, Short filter){
		Filter fil = new Filter();
		fil.groupIndex = filter;
		for(Fixture fix :bod.getFixtureList()){
			fix.setFilterData(fil);
		}
	}
	
	public Body addCircleFixture(Body bod, float x, float y, float size){
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(size);
		circleShape.setPosition(new Vector2(x,y));
		bod.createFixture(makeFixture(0,circleShape));
		circleShape.dispose();
		return bod;
	}
	
}