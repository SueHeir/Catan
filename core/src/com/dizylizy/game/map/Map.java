package com.dizylizy.game.map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dizylizy.game.loader.GameAssetsManager;
import com.dizylizy.game.world.BodyFactory;

public class Map {
	
	//2d array of tile classes
	public static Tile[][] tiles;
	public static Vertex[][] vertexes;
	public static Edge[][] edges;
	
	//added to 1d arraylist for handling
	public static ArrayList<Tile> TileList;
	public static ArrayList<Vertex> VertexList;
	public static ArrayList<Edge> EdgeList;
	
	public static int[] worldSize;
	
	public static float tileSize = 50;

	public static void init(int[] mapsize, BodyFactory bodyFactory, GameAssetsManager assetManager, ArrayList<String> tileType, ArrayList<Integer> tileValue) {		
			MapConditions.init();
			worldSize = mapsize;
			
			tiles = new Tile[worldSize[0]][worldSize[1]];
			vertexes = new Vertex[worldSize[0]*2][worldSize[1]+1];
			edges = new Edge[worldSize[0]*3][worldSize[1]];
			
			TileList = new ArrayList<Tile>();
			VertexList = new ArrayList<Vertex>();
			EdgeList = new ArrayList<Edge>();
		
		//FOR TILES 2D array size of MapSize (see play class)	
			int k=0;
			for(int i=0;i<worldSize[1];++i){
				for(int j=0;j<worldSize[0];++j){
					tiles[j][i] = new Tile(j,i,bodyFactory, assetManager);
					tiles[j][i].setType(tileType.get(k));
					tiles[j][i].setValue(tileValue.get(k));
					TileList.add(tiles[j][i]);
					k++;
				}
			}
		
			
			
		//FOR VERTEXES 2D array size of MapSize
			for(int i=0;i<worldSize[1]+1;++i){
				for(int j=0;j<worldSize[0]*2;++j){
					vertexes[j][i] = new Vertex(j,i,bodyFactory, assetManager);
					VertexList.add(vertexes[j][i]);
				}
			}
			
		//FOR EDGES 2D array size of MapSize
				for(int i=0;i<worldSize[1];++i){
					for(int j=0;j<worldSize[0]*3;++j){
						edges[j][i] = new Edge(j,i,bodyFactory,assetManager);
						EdgeList.add(edges[j][i]);
					}
				} 
		
			
			
				
				
			
			//FOR TILES Sets Location, ID tag, passes tileSize (from play class to basicTile class)
			k=0;
			int j=0;
			for(Tile x: TileList) {
				if((k & 1) == 0) {
				x.setParamaters(1.50f*tileSize*k, 1.73f*tileSize*j, tileSize);
				}
				else {
				x.setParamaters(1.50f*tileSize*k, tileSize*0.86f + 1.73f*tileSize*j, tileSize);
				}

				k++;
				if(k==worldSize[0]) {
					k=0;
					j++;
				}
			}
			
			
			
			/*
			 * FOR VERTEXES 
			 */
			
			k=0; j=0; 
			boolean rotate = true;
			for(Vertex x: VertexList) {
				if(rotate) {
					if((k & 1) == 0) {
						x.setParamaters(1.50f*tileSize*k-0.5f*tileSize, 1.73f*tileSize*j-0.866f*tileSize,  tileSize);
					} else {
						x.setParamaters(1.50f*tileSize*k-0.5f*tileSize, tileSize*0.86f + 1.73f*tileSize*j-0.866f*tileSize, tileSize);
					}
					rotate = false;
					
				} else {
					if((k & 1) == 0) {
						x.setParamaters(1.50f*tileSize*k+0.5f*tileSize, 1.73f*tileSize*j-0.866f*tileSize,  tileSize);
					} else {
						x.setParamaters(1.50f*tileSize*k+0.5f*tileSize, tileSize*0.86f + 1.73f*tileSize*j-0.866f*tileSize, tileSize);
					}
					rotate = true;
					k++;
					if(k==worldSize[0]) {
						k=0;
						j++;
						
					}
				}
			}
			
			
			/*
			 * FOR EDGES 
			 */
			
			k=0; j=0; int side = 0;
			for(Edge x: EdgeList) {
				
				if((k & 1) == 0) {
					
					if(side==0) {
						x.setParamaters(1.50f*tileSize*k - 0.75f*tileSize, 1.73f*tileSize*j - 0.4333f*tileSize, side, tileSize);
					} 
					if(side==1) {
						x.setParamaters(1.50f*tileSize*k, 1.73f*tileSize*j - 0.866f*tileSize, side, tileSize);
					}
					if(side==2) {
						x.setParamaters(1.50f*tileSize*k + 0.75f*tileSize, 1.73f*tileSize*j - 0.4333f*tileSize, side, tileSize);
					}
					++side;
					
				}
				else {

					if(side==0) {
						x.setParamaters(1.50f*tileSize*k - 0.75f*tileSize, tileSize*0.86f + 1.73f*tileSize*j - 0.4333f*tileSize, side, tileSize);
					} 
					if(side==1) {
						x.setParamaters(1.50f*tileSize*k,tileSize*0.86f + 1.73f*tileSize*j - 0.866f*tileSize, side, tileSize);
					}
					if(side==2) {
						x.setParamaters(1.50f*tileSize*k + 0.75f*tileSize, tileSize*0.86f + 1.73f*tileSize*j - 0.4333f*tileSize, side, tileSize);
					}
					++side;
				}
					
				if(side==3) {
					side=0;
					k++;
				}
		
				if(k==worldSize[0]) {
					k=0;
					j++;
				}
			} 
			
			
			for(Edge x1: EdgeList) {
				
				if(x1.getAdjacentTile().size()>1)
				if(x1.getAdjacentTile().get(0).getType().equals("OCEAN") && x1.getAdjacentTile().get(1).getType().equals("OCEAN")) {
					x1.setIsFilled(true);
				}
			}
			
			for(Vertex x1: VertexList) {
				
				if(x1.getAdjacentTile().size()>2)
				if(x1.getAdjacentTile().get(0).getType().equals("OCEAN") && x1.getAdjacentTile().get(1).getType().equals("OCEAN") && x1.getAdjacentTile().get(2).getType().equals("OCEAN")) {
					x1.setIsFilled(true);
				}
			} 
		}
	


	public static void renderpb(float delta, PolygonSpriteBatch pb, OrthographicCamera cam) {
		// Draws tile class
				
				for(Tile x: TileList){ 
					x.renderpb(delta,pb,cam);
				}
				for(Vertex x: VertexList) {
					x.renderpb(delta,pb,cam);
				}
				for(Edge x: EdgeList) {
					x.renderpb(delta,pb,cam);
				}
		
	}

	public static void rendersb(float delta, SpriteBatch sb, BitmapFont font, ShapeRenderer sr) {
				
				// Draws tile class
				for(Tile x: TileList){
					x.rendersb(delta,sb,font,sr);
				}
				for(Vertex x: VertexList) {
					x.rendersb(delta,sb,font,sr);
				}
				for(Edge x: EdgeList) {
					x.rendersb(delta,sb,font,sr);
				}
	}

}
