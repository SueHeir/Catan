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



public class Vertex extends MapElement{

	private int XCoord, YCoord, ID;
	private float XCenter,YCenter,R;
	private boolean hover = false, isFilled = false, hasBuilding=false;
	private String Name="Vertex",Adjacency = "", BuildingName = "";
	private float tileSize;
	private Body vertexBody;
	private BodyFactory bodyFactory;
	private Color color = Color.WHITE;
	
	
	
	private PolygonSprite settlement;
	private PolygonSprite city1,city2;
	private Texture textureSolid;
	
	public Vertex(int i, int j, BodyFactory bodyFactory, GameAssetsManager assetManager) {
		XCoord = i;
		YCoord = j;
		this.bodyFactory= bodyFactory;
		
		
		
		if(XCoord==0 || YCoord==0 || XCoord==B2dWorld.mapsize[0]*2-1 || YCoord==B2dWorld.mapsize[1]) {
			isFilled = true;
		}		
	}
	
	public void setParamaters(float x, float y, float tileSize){
		
		XCenter=x;
		YCenter=y;
		R = tileSize*0.2f;
		this.tileSize = tileSize;
		
		float size = 1.005f; 
		float[] vertices = {
				-0.1f*tileSize*size, -0.1f*tileSize,            // Vertex 0         
				-0.1f*tileSize*size, 0.08f*tileSize,
				 0,0.14f*tileSize,								// Vertex 1      
				 0.1f*tileSize*size, 0.08f*tileSize,      // Vertex 2         
				 0.1f*tileSize*size,-0.1f*tileSize // Vertex 3       
		};
		
		
		// Creating the color filling (but textures would work the same way)
		Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pix.setColor(color); // DE is red, AD is green and BE is blue.
		pix.fill();
		textureSolid = new Texture(pix);
		PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
				  vertices, new EarClippingTriangulator().computeTriangles(vertices).toArray());
		settlement = new PolygonSprite(polyReg);
		settlement.setPosition(XCenter + tileSize*size, YCenter + tileSize*size);
	    
		city1 = new PolygonSprite(polyReg);
		city1.setPosition(XCenter + tileSize*size-0.05f*tileSize*size, YCenter + tileSize*size+0.05f*tileSize*size);
		
		float[] vertices2 = {
				-0.15f*tileSize*size, -0.1f*tileSize,            // Vertex 0         
				-0.15f*tileSize*size, 0.08f*tileSize,								// Vertex 1      
				 0.15f*tileSize*size, 0.08f*tileSize,      // Vertex 2         
				 0.15f*tileSize*size,-0.1f*tileSize // Vertex 3        Vertex 3       
		};
		
		PolygonRegion polyReg2 = new PolygonRegion(new TextureRegion(textureSolid),
				  vertices2, new EarClippingTriangulator().computeTriangles(vertices2).toArray());
		city2 = new PolygonSprite(polyReg2);
		city2.setPosition(XCenter + tileSize*size, YCenter + tileSize*size);
	    
		
		setBody(bodyFactory.makeCircleMapElement(XCenter+tileSize, YCenter+tileSize, R));
		
	}

	public void renderpb(float delta, PolygonSpriteBatch pb, OrthographicCamera cam) {
		if(BuildingName.equals("SETTLEMENT")) {
			settlement.setColor(color);
			settlement.draw(pb);
		}
		if(BuildingName.equals("CITY")) {
			city1.setColor(color);
			city2.setColor(color);
			city2.draw(pb);
			city1.draw(pb);
			
		}
		
		if(BuildingName.equals("SETTLEMENT") || BuildingName.equals("CITY")) {
			hasBuilding=true;
		} else {
			hasBuilding = false;
		}
		
	}
	
	public void rendersb(float delta, SpriteBatch sb, BitmapFont font, ShapeRenderer sr) {
		if(hover) {
			sr.setColor(Color.GREEN);
			sr.circle(XCenter+tileSize, YCenter+tileSize, R);
		}
		if(isFilled){
			sr.setColor(Color.RED);
			sr.circle(XCenter+tileSize, YCenter+tileSize, R);
		}
	}
	

	
	public ArrayList<Tile> getAdjacentTile() {
		ArrayList<Tile> list = new ArrayList<Tile>();
		if((XCoord & 2)==0) {
			if((XCoord & 1)==0) {
				
				//NE 
				if((XCoord)/2>=0 && YCoord-1>=0 && (XCoord)/2<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1]) {
					list.add(Map.tiles[(XCoord)/2][YCoord-1]);
				}
				//SE 
				if((XCoord)/2>=0 && YCoord>=0 && (XCoord)/2<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1]) {
					list.add(Map.tiles[(XCoord)/2][YCoord]);
				}
				//W 
				if((XCoord-2)/2>=0 && YCoord-1>=0 && (XCoord-2)/2<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1]) {
					list.add(Map.tiles[(XCoord-2)/2][YCoord-1]);
				}
			} else {
				
				//E Vertex
				if((XCoord+1)/2>=0 && YCoord-1>=0 && (XCoord+1)/2<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1]) {
					list.add(Map.tiles[(XCoord+1)/2][YCoord-1]);
					}
				//NW 
				if((XCoord-1)/2>=0 && YCoord-1>=0 && (XCoord-1)/2<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1]) {
					list.add(Map.tiles[(XCoord-1)/2][YCoord-1]);
					}
				
				//SW 
				if((XCoord-1)/2>=0 && YCoord>=0 && (XCoord-1)/2<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1]) {
					list.add(Map.tiles[(XCoord-1)/2][YCoord]);
					}
			}
		} else {
			if((XCoord & 1)==0) {
				
				//NE 
				if((XCoord)/2>=0 && YCoord-1>=0 && (XCoord)/2<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord)/2][YCoord-1]);
				
				//SE 
				if((XCoord)/2>=0 && YCoord>=0 && (XCoord)/2<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord)/2][YCoord]);
				
				//W 
				if((XCoord-2)/2>=0 && YCoord>=0 && (XCoord-2)/2<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord-2)/2][YCoord]);
				
			} else {
				
				//E 
				if((XCoord+1)/2>=0 && YCoord>=0 && (XCoord+1)/2<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord+1)/2][YCoord]);
				
				//NW 
				if((XCoord-1)/2>=0 && YCoord-1>=0 && (XCoord-1)/2<B2dWorld.mapsize[0] && YCoord-1<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord-1)/2][YCoord-1]);
				
				//SW  
				if((XCoord-1)/2>=0 && YCoord>=0 && (XCoord-1)/2<B2dWorld.mapsize[0] && YCoord<B2dWorld.mapsize[1])
					list.add(Map.tiles[(XCoord-1)/2][YCoord]);
				
			}
		}
		return list;
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
		if(this.getNorthVertex()!=null) {
			list.add(this.getNorthVertex());
			list.get(list.size()-1).setAdjacency("North");
		}
		if(this.getSouthVertex()!=null) {
			list.add(this.getSouthVertex());
			list.get(list.size()-1).setAdjacency("South");
		}
		
		return list;
	}
	
	public Vertex getNorthVertex() {
		if((XCoord & 2)==0) {
			if((XCoord & 1)==0) {
				//NW
				if((XCoord-1)>=0 && YCoord-1>=0 && (XCoord-1)<B2dWorld.mapsize[0]*2 && YCoord-1<B2dWorld.mapsize[1]+1)
					return Map.vertexes[(XCoord-1)][YCoord-1];
				
			} else {
				//NE
				if((XCoord+1)>=0 && YCoord-1>=0 && (XCoord+1)<B2dWorld.mapsize[0]*2 && YCoord-1<B2dWorld.mapsize[1]+1)
					return Map.vertexes[(XCoord+1)][YCoord-1];
					
			}
		} else {
			if((XCoord & 1)==0) {
				//NW
				if((XCoord-1)>=0 && YCoord>=0 && (XCoord-1)<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					return Map.vertexes[(XCoord-1)][YCoord];
				
			} else {
				//NE
				if((XCoord+1)>=0 && YCoord>=0 && (XCoord+1)<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					return Map.vertexes[(XCoord+1)][YCoord];
					
			}
		}
		return null;
	}
	
	public Vertex getSouthVertex() {
		if((XCoord & 2)==0) {
			if((XCoord & 1)==0) {
				//SW
				if((XCoord-1)>=0 && YCoord>=0 && (XCoord-1)<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					return Map.vertexes[(XCoord-1)][YCoord];
				
			} else {
				//SE
				if((XCoord+1)>=0 && YCoord>=0 && (XCoord+1)<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					return Map.vertexes[(XCoord+1)][YCoord];	
			}
		} else {
			if((XCoord & 1)==0) {
				//SW
				if((XCoord-1)>=0 && YCoord+1>=0 && (XCoord-1)<B2dWorld.mapsize[0]*2 && YCoord+1<B2dWorld.mapsize[1]+1)
					return Map.vertexes[(XCoord-1)][YCoord+1];
			} else {
				//SE
				if((XCoord+1)>=0 && YCoord+1>=0 && (XCoord+1)<B2dWorld.mapsize[0]*2 && YCoord+1<B2dWorld.mapsize[1]+1)
					return Map.vertexes[(XCoord+1)][YCoord+1];
					
			}
		}
		return null;
	}
	
	public Vertex getEastVertex() {
		if((XCoord & 2)==0) {
			if((XCoord & 1)==0) {
				//E
				if((XCoord+1)>=0 && YCoord>=0 && (XCoord+1)<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					return Map.vertexes[(XCoord+1)][YCoord];
			} 
		} else {
			if((XCoord & 1)==0) {
				//E
				if((XCoord+1)>=0 && YCoord>=0 && (XCoord+1)<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					return Map.vertexes[(XCoord+1)][YCoord];
			} 
		}
		return null;
	}
	
	public Vertex getWestVertex() {
		if((XCoord & 2)==0) {
			if((XCoord & 1)==1) {
				//W
				if((XCoord-1)>=0 && YCoord>=0 && (XCoord-1)<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					return Map.vertexes[(XCoord-1)][YCoord];
			}
		} else {
			if((XCoord & 1)==1) {
				//W
				if((XCoord-1)>=0 && YCoord>=0 && (XCoord-1)<B2dWorld.mapsize[0]*2 && YCoord<B2dWorld.mapsize[1]+1)
					return Map.vertexes[(XCoord-1)][YCoord];
			}
		}
		return null;
	}
	
	public ArrayList<Edge> getAdjacentEdge() {
		ArrayList<Edge> list = new ArrayList<Edge>();
		if(this.getWestEdge()!=null) {
			list.add(this.getWestEdge());
			list.get(list.size()-1).setAdjacency("West");
		}
		if(this.getEastEdge()!=null) {
			list.add(this.getEastEdge());
			list.get(list.size()-1).setAdjacency("East");
		}
		if(this.getNorthEdge()!=null) {
			list.add(this.getNorthEdge());
			list.get(list.size()-1).setAdjacency("North");
		}
		if(this.getSouthEdge()!=null) {
			list.add(this.getSouthEdge());
			list.get(list.size()-1).setAdjacency("South");
		}
		return list;
	}
	
	public Edge getNorthEdge() {
		if((XCoord & 2)==0) {  
			if((XCoord & 1)==0) {
				//NW Edge
				if(((XCoord)/2)*3-1>=0 && YCoord-1>=0 && ((XCoord)/2)*3-1<B2dWorld.mapsize[0]*3 && YCoord-1<B2dWorld.mapsize[1])
					return Map.edges[(((XCoord)/2)*3)-1][YCoord-1];
			} else {
				//NE Edge
				if(((XCoord+1)/2)*3>=0 && YCoord-1>=0 && ((XCoord+1)/2)*3<B2dWorld.mapsize[0]*3 && YCoord-1<B2dWorld.mapsize[1])
					return Map.edges[(((XCoord+1)/2)*3)][YCoord-1];
					
			}
		} else {
			if((XCoord & 1)==0) {
				//NW Edge
				if(((XCoord)/2)*3-1>=0 && YCoord>=0 && ((XCoord)/2)*3-1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[(((XCoord)/2)*3-1)][YCoord];
				
			} else {
				//NE Edge
				if(((XCoord+1)/2)*3>=0 && YCoord>=0 && ((XCoord+1)/2)*3<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[(((XCoord+1)/2)*3)][YCoord];
				
			}
		}
		return null;
	}
	
	public Edge getSouthEdge() {
		if((XCoord & 2)==0) {  
			if((XCoord & 1)==0) {
				//SW Edge
				if(((XCoord)/2)*3>=0 && YCoord>=0 && ((XCoord)/2)*3<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[((XCoord)/2)*3][YCoord];
				
			} else {
				//SE Edge
				if(((XCoord+1)/2)*3-1>=0 && YCoord>=0 && ((XCoord+1)/2)*3-1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[((XCoord+1)/2)*3-1][YCoord];
					
			}
		} else {
			if((XCoord & 1)==0) {
				//SW Edge
				if(((XCoord)/2)*3>=0 && YCoord>=0 && ((XCoord)/2)*3<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[((XCoord)/2)*3][YCoord];
				
			} else {
				//SE Edge
				if(((XCoord+1)/2)*3-1>=0 && YCoord>=0 && ((XCoord+1)/2)*3-1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[((XCoord+1)/2)*3-1][YCoord];
				
			}
		}
		return null;
	}
	
	public Edge getWestEdge() {
		if((XCoord & 2)==0) {  
			if((XCoord & 1)==1) {
				//W Edge
				if(((XCoord+1)/2)*3-2>=0 && YCoord>=0 && ((XCoord+1)/2)*3-2<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[((XCoord+1)/2)*3-2][YCoord];
			}
		} else {
			if((XCoord & 1)==1) {
				//W Edge
				if(((XCoord+1)/2)*3-2>=0 && YCoord>=0 && ((XCoord+1)/2)*3-2<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[((XCoord+1)/2)*3-2][YCoord];
			}
		}
		return null;
	}
	
	public Edge getEastEdge() {
		if((XCoord & 2)==0) {  
			if((XCoord & 1)==0) {
				//E Edge
				if(((XCoord)/2)*3+1>=0 && YCoord>=0 && ((XCoord)/2)*3+1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[((XCoord)/2)*3+1][YCoord];
			} 
		} else {
			if((XCoord & 1)==0) {
				//E Edge
				if(((XCoord)/2)*3+1>=0 && YCoord>=0 && ((XCoord)/2)*3+1<B2dWorld.mapsize[0]*3 && YCoord<B2dWorld.mapsize[1])
					return Map.edges[((XCoord)/2)*3+1][YCoord];
			} 
		}
		return null;
	}
	
	Vertex getThisVertex() {
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

	public boolean contains(float xCenter, float yCenter) {
		
		float xd = XCenter-xCenter;
		float yd = YCenter-yCenter;
		
		
		float distanceSquared = (xd * xd) + (yd * yd);
		float radiusSquared = R*R ;
		
		if(distanceSquared<radiusSquared)return true;
		return false;
	}

	@Override
	public boolean justUsed() {
		// TODO Auto-generated method stub
		return false;
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
	
	public Body getBody() {
		return vertexBody;
	}

	public void setBody(Body hexegonBody) {
		this.vertexBody = hexegonBody;
	}

	public void setHover(boolean b) {
		hover=b;
		
	}

	public void setColor(int i) {
		this.color = new Color(i);
		
	}

	public Color getColor() {
		// TODO Auto-generated method stub
		return color;
	}

	public void setColor(Color color2) {
		color = color2;
		
	}



	
}

	