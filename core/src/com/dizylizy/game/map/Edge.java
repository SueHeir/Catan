package com.dizylizy.game.map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
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
import com.dizylizy.game.world.B2dWorld;
import com.dizylizy.game.world.BodyFactory;

public class Edge extends MapElement{

	private int XCoord, YCoord, rotation, ID;
	private float XCenter,YCenter, R;
	private boolean hover = false, isFilled = false, hasBuilding=false;
	private String Name="EDGE",Adjacency = "", BuildingName="";
	private BodyFactory bodyFactory;
	
	private PolygonSprite poly1;
	PolygonRegion polyReg;
	private Texture textureSolid;
	private float tileSize;
	private Body EdgeBody;
	
	private Color color= Color.WHITE;
	public Edge(int j, int i, BodyFactory bodyFactory, GameAssetsManager assetManager) {
		
		XCoord = j;
		YCoord = i;
		
		this.bodyFactory= bodyFactory;
		
		
		
	}
	
	public void setParamaters(float x, float y, int r, float tileSize){
		
		XCenter=x;
		YCenter=y;
		rotation=r; 
		R = tileSize*0.065f;
		this.tileSize=tileSize;
		
		
		float size = 1.005f; 
		float[] vertices = {
				-0.3f*tileSize*size,-0.05f*tileSize,            // Vertex 0         
				-0.3f*tileSize*size, 0.05f*tileSize,        // Vertex 1      
				 0.3f*tileSize*size, 0.05f*tileSize,      // Vertex 2         
				 0.3f*tileSize*size,-0.05f*tileSize // Vertex 3       
		};
		
		
		// Creating the color filling (but textures would work the same way)
		Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pix.setColor(color); // DE is red, AD is green and BE is blue.
		pix.fill();
		textureSolid = new Texture(pix);
		PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
				  vertices, new EarClippingTriangulator().computeTriangles(vertices).toArray());
		poly1 = new PolygonSprite(polyReg);
		poly1.setPosition(XCenter + tileSize*size, YCenter + tileSize*size);
	    
		if(rotation==0) {
			poly1.setRotation(120);
		}if(rotation==2) {
			poly1.setRotation(60);
		}
		
		
		setBody(bodyFactory.makeCircleMapElement(XCenter+tileSize, YCenter+tileSize, R*2));
		
		
		}
	
	
	public void renderpb(float delta, PolygonSpriteBatch pb, OrthographicCamera cam) {
		
		if(BuildingName.equals("ROAD")) {
			poly1.setColor(color);
			poly1.draw(pb);
		}
		
		if(BuildingName.equals("ROAD")) {
			hasBuilding=true;
		}
		
		
	}
	
	public void rendersb(float delta, SpriteBatch sb, BitmapFont font, ShapeRenderer sr) {
		if(hover) {
			sr.setColor(Color.GREEN);
			sr.circle(XCenter+tileSize, YCenter+tileSize, R*2);
			
		}
	}
	
	
	public ArrayList<Tile> getAdjacentTile() {
		ArrayList<Tile> list = new ArrayList<Tile>();
		if(rotation==0) {
			if((XCoord & 1)==1) {
				//NW tile
				if(XCoord/3-1>=0 && YCoord>=0 && XCoord/3-1<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					list.add(Map.tiles[XCoord/3-1][YCoord]);
				//SE tile
				if(XCoord/3>=0 && YCoord>=0 && XCoord/3<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					list.add(Map.tiles[XCoord/3][YCoord]);
			} else {
				//NW tile
				if(XCoord/3-1>=0 && YCoord-1>=0 && XCoord/3-1<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1])
					list.add(Map.tiles[XCoord/3-1][YCoord-1]);
				//SE tile
				if(XCoord/3>=0 && YCoord>=0 && XCoord/3<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					list.add(Map.tiles[XCoord/3][YCoord]);
			}
			
		} else if(rotation==1) {
			if((XCoord & 1)==1) {
				//S tile
				if((XCoord-1)/3>=0 && YCoord-1>=0 && (XCoord-1)/3<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord-1)/3][YCoord-1]);
				//N tile
				if((XCoord-1)/3>=0 && YCoord>=0 && (XCoord-1)/3<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord-1)/3][YCoord]);
			} else {
				//S tile
				if((XCoord-1)/3>=0 && YCoord-1>=0 && (XCoord-1)/3<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord-1)/3][YCoord-1]);
				//N tile
				if((XCoord-1)/3>=0 && YCoord>=0 && (XCoord-1)/3<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord-1)/3][YCoord]);
			}
		} else {
			if((XCoord & 1)==1) {
				//NW tile
				if((XCoord-2)/3+1>=0 && YCoord>=0 && (XCoord-2)/3+1<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord-2)/3+1][YCoord]);
				//SE tile
				if((XCoord-2)/3>=0 && YCoord>=0 && (XCoord-2)/3<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord-2)/3][YCoord]);
			} else {
				//NW tile
				if((XCoord-2)/3>=0 && YCoord>=0 && (XCoord-2)/3<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord-2)/3][YCoord]);
				//SE tile
				if((XCoord-2)/3+1>=0 && YCoord-1>=0 && (XCoord-2)/3+1<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord-2)/3+1][YCoord-1]);
			}
		}
		return list;
		
	}

	public ArrayList<Vertex> getAdjacentVertex() {
		ArrayList<Vertex> list = new ArrayList<Vertex>();
		if(rotation==0) {
			if((XCoord & 1)==1) {
				//SW Vertex
				if((XCoord/3)*2-1>=0 && YCoord+1>=0 && (XCoord/3)*2-1<B2dWorld.mapsize[0]*2 && YCoord+1<B2dWorld.mapsize[1]+1)
					list.add(Map.vertexes[(XCoord/3)*2-1][YCoord+1]);
				
				//NE Vertex
				if((XCoord/3)*2>=0 && YCoord>=0 && (XCoord/3)*2<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					list.add(Map.vertexes[(XCoord/3)*2][YCoord]);
				
			} else {
				// SW Vertex
				if((XCoord/3)*2-1>=0 && YCoord>=0 && (XCoord/3)*2-1<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					list.add(Map.vertexes[(XCoord/3)*2-1][YCoord]);
				
				//NE Vertex
				if((XCoord/3)*2>=0 && YCoord>=0 && (XCoord/3)*2<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					list.add(Map.vertexes[(XCoord/3)*2][YCoord]);
				
			}
			
		} else if(rotation==1) {
			if((XCoord & 1)==1) {
				//E Vertex
				if(((XCoord-1)/3)*2+1>=0 && YCoord+1>=0 && ((XCoord-1)/3)*2+1<B2dWorld.mapsize[0]*2 && YCoord+1<B2dWorld.mapsize[1]+1)
					list.add(Map.vertexes[((XCoord-1)/3)*2+1][YCoord]);
				
				//W Vertex
				if(((XCoord-1)/3)*2>=0 && YCoord>=0 && ((XCoord-1)/3)*2<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					list.add(Map.vertexes[((XCoord-1)/3)*2][YCoord]);
				
			} else {
				//E Vertex
				if(((XCoord-1)/3)*2+1>=0 && YCoord>=0 && ((XCoord-1)/3)*2+1<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					list.add(Map.vertexes[((XCoord-1)/3)*2+1][YCoord]);
				
				//W Vertex
				if(((XCoord-1)/3)*2>=0 && YCoord>=0 && ((XCoord-1)/3)*2<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					list.add(Map.vertexes[((XCoord-1)/3)*2][YCoord]);
				
			}
		} else {
			if((XCoord & 1)==1) {
				//NW Vertex
				if(((XCoord-2)/3)*2+1>=0 && YCoord+1>=0 && ((XCoord-2)/3)*2+1<B2dWorld.mapsize[0]*2 && YCoord+1<B2dWorld.mapsize[1]+1)
					list.add(Map.vertexes[((XCoord-2)/3)*2+1][YCoord]);
				
				//SE Vertex
				if(((XCoord-2)/3)*2+2>=0 && YCoord+1>=0 && ((XCoord-2)/3)*2+2<B2dWorld.mapsize[0]*2 && YCoord+1<B2dWorld.mapsize[1]+1)
					list.add(Map.vertexes[((XCoord-2)/3)*2+2][YCoord+1]);
				
			} else {
				//NW Vertex
				if(((XCoord-2)/3)*2+1>=0 && YCoord>=0 && ((XCoord-2)/3)*2+1<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					list.add(Map.vertexes[((XCoord-2)/3)*2+1][YCoord]);
			
				//SE Vertex
				if(((XCoord-2)/3)*2+2>=0 && YCoord>=0 && (XCoord/3)*2+2<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					list.add(Map.vertexes[((XCoord-1)/3)*2+2][YCoord]);
	
			}
		}
		
		return list;
	}
	
	public ArrayList<Edge> getAdjacentEdge() {
		ArrayList<Edge> list = new ArrayList<Edge>();
		if(rotation==0) {
			if((XCoord & 1)==1) {
				//N Edge
				if(XCoord-1>=0 && YCoord>=0 && XCoord-1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord-1][YCoord]);
				
				//E Edge 
				if(XCoord+1>=0 && YCoord>=0 && XCoord+1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord+1][YCoord]);
				
				//S Edge
				if(XCoord-1>=0 && YCoord+1>=0 && XCoord-1<B2dWorld.mapsize[0]*3 && YCoord+1<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord-1][YCoord+1]);
				
				//W Edge
				if(XCoord-2>=0 && YCoord+1>=0 && XCoord-2<B2dWorld.mapsize[0]*3 && YCoord+1<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord-2][YCoord+1]);
				
			} else {
				//S
				if(XCoord-1>=0 && YCoord>=0 && XCoord-1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord-1][YCoord]);
				
				//W
				if(XCoord-2>=0 && YCoord>=0 && XCoord-2<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord-2][YCoord]);
				
				//E
				if(XCoord+1>=0 && YCoord>=0 && XCoord+1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord+1][YCoord]);
				
				//N
				if(XCoord-1>=0 && YCoord-1>=0 && XCoord-1<B2dWorld.mapsize[0]*3 && YCoord-1<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord-1][YCoord-1]);
				
			}
			
		} else if(rotation==1) {
			if((XCoord & 1)==1) {
				//SW Edge
				if(XCoord-1>=0 && YCoord>=0 && XCoord-1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord-1][YCoord]);
				
				//SE Edge 
				if(XCoord+1>=0 && YCoord>=0 && XCoord+1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord+1][YCoord]);
				
				//NW Edge
				if(XCoord-2>=0 && YCoord-1>=0 && XCoord-2<B2dWorld.mapsize[0]*3 && YCoord-1<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord-2][YCoord-1]);
				
				//NE Edge
				if(XCoord+2>=0 && YCoord-1>=0 && XCoord+2<B2dWorld.mapsize[0]*3 && YCoord-1<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord+2][YCoord-1]);
				
			} else {
				//SW
				if(XCoord-1>=0 && YCoord>=0 && XCoord-1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord-1][YCoord]);
				
				//NW
				if(XCoord-2>=0 && YCoord>=0 && XCoord-2<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord-2][YCoord]);
				
				//SE
				if(XCoord+1>=0 && YCoord>=0 && XCoord+1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord+1][YCoord]);
				
				//NE
				if(XCoord+2>=0 && YCoord>=0 && XCoord+2<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord+2][YCoord]);
				
			}
		} else {
			if((XCoord & 1)==1) {
				//W Edge
				if(XCoord-1>=0 && YCoord>=0 && XCoord-1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord-1][YCoord]);
				
				//N Edge 
				if(XCoord+1>=0 && YCoord>=0 && XCoord+1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord+1][YCoord]);
				
				//S Edge
				if(XCoord+1>=0 && YCoord+1>=0 && XCoord+1<B2dWorld.mapsize[0]*3 && YCoord+1<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord+1][YCoord+1]);
				
				//E Edge
				if(XCoord+2>=0 && YCoord+1>=0 && XCoord+2<B2dWorld.mapsize[0]*3 && YCoord+1<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord+2][YCoord+1]);
				
			} else {
				//W
				if(XCoord-1>=0 && YCoord>=0 && XCoord-1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord-1][YCoord]);
				
				//N
				if(XCoord+1>=0 && YCoord-1>=0 && XCoord+1<B2dWorld.mapsize[0]*3 && YCoord-1<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord+1][YCoord-1]);
				
				//S
				if(XCoord+1>=0 && YCoord>=0 && XCoord+1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord+1][YCoord]);
				
				//E
				if(XCoord+2>=0 && YCoord>=0 && XCoord+2<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					list.add(Map.edges[XCoord+2][YCoord]);
				
			}
		}
		
		return list;
	}

	

	Edge getThisEdge() {
		return this;
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

	public void setIsFilled(boolean b) {
		this.isFilled=b;
		
	}

	public boolean getIsFilled() {
		
		return this.isFilled;
	}

	public int getXCoord() {
		return XCoord;
	}
	public int getYCoord() {
		return YCoord;
	}
	
	public void setAdjacency(String a) {
		this.Adjacency = a;  
	}

	@Override
	public String getName() {
		
		return Name;
	}

	@Override
	public boolean getHasBuilding() {
		return hasBuilding;
	}

	@Override
	public String getBuildingName() {
		return BuildingName;
	}

	@Override
	public int getID() {
		return ID;
	}
	public void setBuildingName(String s) {
		BuildingName=s;
	}
	public void setHasBuilding(boolean b) {
		hasBuilding=b;
	}

	@Override
	public boolean justUsed() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isHover() {
		return hover;
	}

	public void setHover(boolean hover) {
		this.hover = hover;
	}
	public Body getBody() {
		return EdgeBody;
	}

	public void setBody(Body hexegonBody) {
		this.EdgeBody = hexegonBody;
	}

	public void setColor(Color color2) {
		this.color = color2;
		
	}

	public Color getColor() {
		// TODO Auto-generated method stub
		return color;
	}

	public void setColor(int int1) {
		this.color = new Color(int1);
		
	}

	public String getColorString() {
		if(color==Color.BROWN)return "Brown";
		if(color==Color.WHITE)return "White";
		if(color==Color.BLUE)return "Blue";
		if(color==Color.RED)return "Red";
		if(color==Color.ORANGE)return "Orange";
		if(color==Color.GREEN)return "Green";
		return "Black";
	}
	
	public void setColorString(String string) {
		if(string.equals("Brown"))color=Color.BROWN;
		if(string.equals("White"))color=Color.WHITE ;
		if(string.equals("Blue"))color= Color.BLUE;
		if(string.equals("Red"))color= Color.RED;
		if(string.equals("Orange"))color=Color.ORANGE;
		if(string.equals("Green"))color=Color.GREEN ;
	}


	
}
