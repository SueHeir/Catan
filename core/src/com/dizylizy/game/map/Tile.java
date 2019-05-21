package com.dizylizy.game.map;



import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.physics.box2d.Body;
import com.dizylizy.game.loader.GameAssetsManager;
import com.dizylizy.game.world.BodyFactory;
import com.dizylizy.game.world.B2dWorld;


	public class Tile extends MapElement{
		
		private int Value, XCoord, YCoord, ID;
		private float XCenter,YCenter,R, tileSize; 
		private boolean hover = false, selected = false, hasBuilding=false;
		private String Name = "TILE", Type = "", Adjacency = "", BuildingName = "";
		private Body hexegonBody;
		
		
		
		private PolygonSprite poly; // To assign at the beginning
		private Texture texture, robberTexture;
		private GameAssetsManager assetManager;
		private BodyFactory bodyFactory;


		
		
		
		/*
		 * Basic Tile Constructor Sets (x,y) coordinates
		 */
		public Tile(int j, int i, BodyFactory bodyFactory, GameAssetsManager assetManager) {
			XCoord = j;
			YCoord = i;
			this.assetManager=assetManager;
			this.bodyFactory= bodyFactory;
			
		}

		/*
		 * Sets Paramaters for Tile
		 */
		public void setParamaters(float x, float y, float tilesize){
			//sets valves to private class
			XCenter=x;
			YCenter=y;
			R= tileSize*0.8f;
			this.tileSize=tilesize;
			
			String tex = "images/"+Type+".PNG";
			texture = assetManager.manager.get(tex, Texture.class);
			robberTexture = assetManager.manager.get(GameAssetsManager.ROBBERImage, Texture.class);
			
			TextureRegion textureRegion = new TextureRegion(texture);
			
			float size = 1.005f; 
			float[] vertices = {
					-tileSize*size+tileSize*size,0+tileSize*size,            // Vertex 0         3--2
				    -0.5f*tileSize*size+tileSize*size,-0.866f*tileSize*size+tileSize*size,          // Vertex 1         | /|
				    0.5f*tileSize*size+tileSize*size,-0.866f*tileSize*size+tileSize*size,        // Vertex 2         |/ |
				    tileSize*size+tileSize*size,0+tileSize*size, // Vertex 3         0--1
				    0.5f*tileSize*size+tileSize*size,0.866f*tileSize*size+tileSize*size, // Vertex 4 
				    -0.5f*tileSize*size+tileSize*size,0.866f*tileSize*size+tileSize*size,
			};
		
			
			PolygonRegion polyReg = new PolygonRegion(textureRegion,
					  vertices, new EarClippingTriangulator().computeTriangles(vertices).toArray());
			poly = new PolygonSprite(polyReg);
		    poly.setOrigin( - 0.5f*tileSize*size, -0.5f*tileSize*size);
			poly.setBounds(XCenter, YCenter, tileSize*size*2, tileSize*size*2);
			poly.setPosition(XCenter, YCenter);
		    
			
			setBody(bodyFactory.makeCircleMapElement(XCenter+tileSize*size, YCenter+tileSize*size, tileSize*0.75f));
			
			if(Type.equals("DESERT")) {
				BuildingName="ROBBER";
			} else {
				BuildingName="";
			}
			
			
		}
		
		public void setParamaters() {
			setParamaters(XCenter,YCenter,tileSize);
		}
		
		/* 
		 * Draws Tile
		 */
		public void renderpb(float delta, PolygonSpriteBatch pb, OrthographicCamera cam) {
			if(!BuildingName.equals("")) {
				hasBuilding=true;
			} else {
				hasBuilding=false;
			}
			
			poly.draw(pb);
			
		}
		public void rendersb(float delta, SpriteBatch sb, BitmapFont font, ShapeRenderer sr) {
			if(BuildingName.equals("ROBBER")) {
				sb.draw(robberTexture, XCenter+tileSize-5, YCenter+tileSize-5, 0,0,10,10);
			}
			if(Value!=0)
			font.draw(sb,""+Value, XCenter+tileSize,YCenter+tileSize);
			if(hover) {
			sr.setColor(Color.GREEN);
			sr.circle(XCenter+tileSize*1.01f, YCenter+tileSize*1.01f, tileSize*0.75f);
			}
			
		}
		
		
		public Vertex getVertexNextToCorner(int i) {
			if(i==0) {
				return getWestVertex();
			}
			if(i==1) {
				return getNorthwestVertex();
			}
			if(i==2) {
				return getNortheastVertex();
			}
			if(i==3) {
				return getEastVertex();
			}
			if(i==4) {
				return getSoutheastVertex();
			}
			if(i==5) {
				return getSouthwestVertex();
			}
			return null;
			
		}
		
		
		public ArrayList<Vertex> getAdjacentVertex() {
			ArrayList<Vertex> list = new ArrayList<Vertex>();
			if(this.getWestVertex()!=null) {
				list.add(this.getWestVertex());
				list.get(list.size()-1).setAdjacency("West");
			}
			if(this.getEastVertex()!=null) {
				list.add(this.getEastVertex());
				list.get(list.size()-1).setAdjacency("East");
			}
			if(this.getNorthwestVertex()!=null) {
				list.add(this.getNorthwestVertex());
				list.get(list.size()-1).setAdjacency("Northwest");
			}
			if(this.getNortheastVertex()!=null) {
				list.add(this.getNortheastVertex());
				list.get(list.size()-1).setAdjacency("Northeast");
			}
			if(this.getSouthwestVertex()!=null) {
				list.add(this.getSouthwestVertex());
				list.get(list.size()-1).setAdjacency("Southwest");
			}
			if(this.getSoutheastVertex()!=null) {
				list.add(this.getSoutheastVertex());
				list.get(list.size()-1).setAdjacency("Southeast");
			}
			
			
			return list;
		}
		
		public Vertex getWestVertex() {
			if((XCoord & 1)==1) {
				//W Vertex
				if(XCoord*2-1>=0 && YCoord+1>=0 && XCoord*2-1<B2dWorld.mapsize[0]*2 && YCoord+1<=B2dWorld.mapsize[1])
					return Map.vertexes[XCoord*2-1][YCoord+1];
			} else {
				//W Vertex
				if(XCoord*2-1>=0 && YCoord>=0 && XCoord*2-1<B2dWorld.mapsize[0]*2 && YCoord<=B2dWorld.mapsize[1])
					return Map.vertexes[XCoord*2-1][YCoord];
			}
			return null;
		}
		
		public Vertex getEastVertex() {
			if((XCoord & 1)==1) {
				//E Vertex
				if(XCoord*2+2>=0 && YCoord+1>=0 && XCoord*2+2<B2dWorld.mapsize[0]*2 && YCoord+1<=B2dWorld.mapsize[1])
					return Map.vertexes[XCoord*2+2][YCoord+1];
			} else {
				//E Vertex
				if(XCoord*2+2>=0 && YCoord>=0 && XCoord*2+2<B2dWorld.mapsize[0]*2 && YCoord<=B2dWorld.mapsize[1])
					return Map.vertexes[XCoord*2+2][YCoord];
			}
			return null;
		}
		
		public Vertex getNortheastVertex() {
			//NE Vertex
			if(XCoord*2+1>=0 && YCoord>=0 && XCoord*2+1<B2dWorld.mapsize[0]*2 && YCoord<=B2dWorld.mapsize[1])
				return Map.vertexes[XCoord*2+1][YCoord];
				
			return null;
		}
		
		public Vertex getNorthwestVertex() {
			//NW Vertex
			if(XCoord*2>=0 && YCoord>=0 && XCoord*2<B2dWorld.mapsize[0]*2 && YCoord<=B2dWorld.mapsize[1])
				return Map.vertexes[XCoord*2][YCoord];		
			
			return null;
		}
		
		public Vertex getSoutheastVertex() {
			//SE Vertex
			if(XCoord*2+1>=0 && YCoord+1>=0 && XCoord*2+1<B2dWorld.mapsize[0]*2 && YCoord+1<=B2dWorld.mapsize[1])
				return Map.vertexes[XCoord*2+1][YCoord+1];
			
			return null;
		}
		
		public Vertex getSouthwestVertex() {
			//SW Vertex	
			if(XCoord*2>=0 && YCoord+1>=0 && XCoord*2<B2dWorld.mapsize[0]*2 && YCoord+1<=B2dWorld.mapsize[1])
				return Map.vertexes[XCoord*2][YCoord+1];
	
			return null;
		}
		
		
		public ArrayList<Edge> getAdjacentEdge() {
			ArrayList<Edge> list = new ArrayList<Edge>();

			if(this.getNorthTile()!=null) {
				list.add(this.getNorthEdge());
				list.get(list.size()-1).setAdjacency("North");
			}
			if(this.getSouthTile()!=null) {
				list.add(this.getSouthEdge());
				list.get(list.size()-1).setAdjacency("South");
			}
			if(this.getNorthwestTile()!=null) {
				list.add(this.getNorthwestEdge());
				list.get(list.size()-1).setAdjacency("Northwest");
			}
			if(this.getNortheastTile()!=null) {
				list.add(this.getNortheastEdge());
				list.get(list.size()-1).setAdjacency("Northeast");
			}
			if(this.getSouthwestTile()!=null) {
				list.add(this.getSouthwestEdge());
				list.get(list.size()-1).setAdjacency("Southwest");
			}
			if(this.getSoutheastTile()!=null) {
				list.add(this.getSoutheastEdge());
				list.get(list.size()-1).setAdjacency("Southeast");
			}
			
			return list;
		}
		
		
		public Edge getNorthEdge() {
				//N Edge
				if(XCoord*3+1>=0 && YCoord>=0 && XCoord*3+1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[XCoord*3+1][YCoord];
			
			return null;
		}
		
		public Edge getNorthwestEdge() {
				//NW Edge
				if(XCoord*3>=0 && YCoord>=0 && XCoord*3<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[XCoord*3][YCoord];

			return null;
		}
		
		public Edge getNortheastEdge() {
				//NE Edge
				if(XCoord*3+2>=0 && YCoord>=0 && XCoord*3+2<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[XCoord*3+2][YCoord];
			
			return null;
		}
		
		public Edge getSouthEdge() {	
			//S Edge
			if(XCoord*3+1>=0 && YCoord+1>=0 && XCoord*3+1<B2dWorld.mapsize[0]*3 && YCoord+1<B2dWorld.mapsize[1])
				return Map.edges[XCoord*3+1][YCoord+1];
				
			return null;
		}
		
		public Edge getSouthwestEdge() {
			if((XCoord & 1)==1) {
				//SW Edge
				if(XCoord*3-1>=0 && YCoord+1>=0 && XCoord*3-1<B2dWorld.mapsize[0]*3 && YCoord+1<B2dWorld.mapsize[1])
					return Map.edges[XCoord*3-1][YCoord+1];
				
			} else {
				//SW Edge
				if(XCoord*3-1>=0 && YCoord>=0 && XCoord*3-1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[XCoord*3-1][YCoord];
				}
			
			return null;
		}
		
		public Edge getSoutheastEdge() {
			if((XCoord & 1)==1) {
				//SE Edge
				if(XCoord*3+3>=0 && YCoord+1>=0 && XCoord*3+3<B2dWorld.mapsize[0]*3 && YCoord+1<B2dWorld.mapsize[1])
					return Map.edges[XCoord*3+3][YCoord+1];
				
			} else {
				//SE Edge
				if(XCoord*3+3>=0 && YCoord>=0 && XCoord*3+3<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[XCoord*3+3][YCoord];
				
			}
			
			return null;
		}
		
		
		public ArrayList<Tile> getAdjacentTile() {
			ArrayList<Tile> list = new ArrayList<Tile>();
			
			if(this.getNorthTile()!=null) {
				list.add(this.getNorthTile());
				list.get(list.size()-1).setAdjacency("North");
			}
			if(this.getSouthTile()!=null) {
				list.add(this.getSouthTile());
				list.get(list.size()-1).setAdjacency("South");
			}
			if(this.getNorthwestTile()!=null) {
				list.add(this.getNorthwestTile());
				list.get(list.size()-1).setAdjacency("Northwest");
			}
			if(this.getNortheastTile()!=null) {
				list.add(this.getNortheastTile());
				list.get(list.size()-1).setAdjacency("Northeast");
			}
			if(this.getSouthwestTile()!=null) {
				list.add(this.getSouthwestTile());
				list.get(list.size()-1).setAdjacency("Southwest");
			}
			if(this.getSoutheastTile()!=null) {
				list.add(this.getSoutheastTile());
				list.get(list.size()-1).setAdjacency("Southeast");
			}
			
			return list;
		}
		
		public Tile getNorthTile() {
			//N Tile
			if(XCoord>=0 && YCoord-1>=0 && XCoord<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1])
				return Map.tiles[XCoord][YCoord-1];
			
			return null;
		}
		
		public Tile getSouthTile() {
			//S Tile
			if(XCoord>=0 && YCoord+1>=0 && XCoord<B2dWorld.mapsize[0] && YCoord+1<B2dWorld.mapsize[1])
				return Map.tiles[XCoord][YCoord+1];
			
			return null;
		}
		
		public Tile getNorthwestTile() {
			if((XCoord & 1)==1) {
				//NW Tile
				if(XCoord-1>=0 && YCoord>=0 && XCoord-1<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					return Map.tiles[XCoord-1][YCoord];
			} else {
				//NW Tile
				if(XCoord-1>=0 && YCoord-1>=0 && XCoord-1<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1])
					return Map.tiles[XCoord-1][YCoord-1];
			}
			return null;
		}
		
		public Tile getNortheastTile() {
			if((XCoord & 1)==1) {
				//NE Tile
				if(XCoord+1>=0 && YCoord>=0 && XCoord+1<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					return Map.tiles[XCoord+1][YCoord];
			} else {
				//NE Tile
				if(XCoord+1>=0 && YCoord-1>=0 && XCoord+1<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1])
					return Map.tiles[XCoord+1][YCoord-1];
			}
			return null;
		}
		
		public Tile getSouthwestTile() {
			if((XCoord & 1)==1) {
				//SW Tile
				if(XCoord-1>=0 && YCoord+1>=0 && XCoord-1<B2dWorld.mapsize[0] && YCoord+1<B2dWorld.mapsize[1])
					return Map.tiles[XCoord-1][YCoord+1];
			} else {
				//SW Tile
				if(XCoord-1>=0 && YCoord>=0 && XCoord-1<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					return Map.tiles[XCoord-1][YCoord];
			}
			return null;
		}
		
		public Tile getSoutheastTile() {
			if((XCoord & 1)==1) {
				//SE Tile
				if(XCoord+1>=0 && YCoord+1>=0 && XCoord+1<B2dWorld.mapsize[0] && YCoord+1<B2dWorld.mapsize[1])
					return Map.tiles[XCoord+1][YCoord+1];
			} else {
				//SE Tile
				if(XCoord+1>=0 && YCoord>=0 && XCoord+1<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					return Map.tiles[XCoord+1][YCoord];
				
			}
			return null;
		}
		
		/*
		 * GETTERS 
		 */	
		public  Tile getBasicTile() {
			return this;
		}
		public boolean getIsSelected(){
			return selected;
		}
		public String getAdjacency() {
			return Adjacency;
		}
		public float getX(){
			return XCenter;
		}
		public float getY(){
			return YCenter;
		}
		public int getXCoord(){
			return XCoord;
		}
		public int getYCoord(){
			return YCoord;
		}
		public float getR(){
			return R;
		}
		public int getID(){
			return (int) ID;
		}
		public String getType() {
			return Type;
		}
		public String getName() {
			return Name;
		}
		public int getValue(){
			return Value;
		}

		public boolean getHover() {
			return hover;
		}
		/*
		 * SETTERS 
		 */	
		public void setIsSelected(boolean check){
			selected=check;
		}
		
		public void setAdjacency(String a) {
			this.Adjacency = a;
		}
		public void setX(float x){
			XCenter=x;
		}
		public void setY(float y){
			YCenter=y;
		}
		public void setR(int r){
			R= r;
		}
		public void setID(int id){
			ID=id;
		}
		public void setValue(int val) {
			this.Value = val;
		}
		public void setName(String Name) {
			this.Name = Name;
		}
		
		public void setType(String Type) {
			this.Type=Type;
		}

		@Override
		public boolean getHasBuilding() {
			return hasBuilding;
		}

		@Override
		public String getBuildingName() {
			return BuildingName;
		}
		public void setBuildingName(String s) {
			BuildingName=s;
		}
		
		public void setHover(boolean b) {
			hover=b;
		}

		@Override
		public boolean justUsed() {
			// TODO Auto-generated method stub
			return false;
		}

		public Body getBody() {
			return hexegonBody;
		}

		public void setBody(Body hexegonBody) {
			this.hexegonBody = hexegonBody;
		}
		

}
	


