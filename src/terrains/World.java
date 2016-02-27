package terrains;

import java.util.ArrayList;
import java.util.List;

import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import water.WaterTile;
import entities.Entity;
import entities.Light;

public class World {
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<Light> lights = new ArrayList<Light>();
	private List<Entity> entities = new ArrayList<Entity>();
	private List<WaterTile> waters = new ArrayList<WaterTile>();
	
	private String worldFilePath = "";
	
	private Loader loader;
	
	TerrainTexturePack texturePack;
	TerrainTexture blendMap;
	
	public World(Loader loader) {
		texturePack = new TerrainTexturePack(loader, "grass", "mud", "flowers", "path");
		blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		terrains.add(new Terrain(0, -1, loader, texturePack, blendMap, "heightMap"));
	}
	
	public World(String worldFile) {
		this.worldFilePath = worldFile;
		loadFileWorld();
	}
	
	private void loadFileWorld() {
		//Parser
	}
	
	public List<Entity> getEntities() {
		return entities;
	}

	public List<Light> getLights() {
		return lights;
	}

	public List<Terrain> getTerrains() {
		return terrains;
	}

}
