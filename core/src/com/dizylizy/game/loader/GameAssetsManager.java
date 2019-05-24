package com.dizylizy.game.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;

public class GameAssetsManager {
	
	public final AssetManager manager = new AssetManager();

	// Sounds
	public final String boingSound = "sounds/boing.wav";
	public final String pingSound = "sounds/ping.wav";
	
	// Music
	public final String playingSong = "music/Rolemusic_-_pl4y1ng.mp3";
	
	// Skin
	public final String skin = "skin/uiskin.json";
	
	// Textures
	public final String OCEANImage = "images/OCEAN.PNG";
	public final String PLAINSImage = "images/PLAINS.PNG";
	public final String MOUNTAINImage = "images/MOUNTAIN.PNG";
	public final String MUDImage = "images/MUD.PNG";
	public final String WHEATImage = "images/WHEAT.PNG";
	public final String FORESTImage = "images/FOREST.PNG";
	public final String DESERTImage = "images/DESERT.PNG";
	public final String ROCKImage = "images/ROCK.PNG";
	public final String WHEATitemImage = "images/WHEATitem.PNG";
	public final String WOODImage = "images/WOOD.PNG";
	public final String BRICKImage = "images/BRICK.PNG";
	public final String WOOLImage = "images/WOOL.PNG";
	public final static String ROBBERImage = "images/ROBBER.PNG";
	public final static String SHIPImage = "images/SHIP.PNG";
	
	
	public void queueAddFonts(){
		
	}
	 
	public void queueAddParticleEffects(){
		
	}
	
	public void queueAddImages(){
		
		manager.load(OCEANImage, Texture.class);
		manager.load(PLAINSImage, Texture.class);
		manager.load(MOUNTAINImage, Texture.class);
		manager.load(MUDImage, Texture.class);
		manager.load(WHEATImage, Texture.class);
		manager.load(FORESTImage, Texture.class);
		manager.load(DESERTImage, Texture.class);
		
		manager.load(ROCKImage, Texture.class);
		manager.load(WOOLImage, Texture.class);
		manager.load(WHEATitemImage, Texture.class);
		manager.load(WOODImage, Texture.class);
		manager.load(BRICKImage, Texture.class);
		
		manager.load(ROBBERImage, Texture.class);
		manager.load(SHIPImage, Texture.class);
	}
	
	// a small set of images used by the loading screen
	public void queueAddLoadingImages(){
		
	}
	
	public void queueAddSkin(){
		SkinParameter params = new SkinParameter("skin/uiskin.atlas");
		manager.load(skin, Skin.class, params);
		
	}
	
	public void queueAddMusic(){
		manager.load(playingSong, Music.class);
	}
	
	public void queueAddSounds(){
		manager.load(boingSound, Sound.class);
		manager.load(pingSound, Sound.class);
	}
	
	
}
