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
	private static List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> normalMappedEntities = new ArrayList<Entity>();
	private List<WaterTile> waters = new ArrayList<WaterTile>();
	
	private String worldFilePath = "";
	
	public World() {
	}
	
	public World(String worldFile) {
		this.worldFilePath = worldFile;
		loadFileWorld();
	}
	
	private void loadFileWorld() {

	}

	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

	public void addNormalMappedEntity(Entity entity) {
		this.normalMappedEntities.add(entity);
	}

	public void addLight(Light light) {
		this.lights.add(light);
	}

	public void addWater(WaterTile tile) {
		this.waters.add(tile);
	}

	public void add(List<Terrain> terrains, List<Entity> entities, List<Entity> normalMappedEntities, List<Light> lights, List<WaterTile> waters) {
		this.entities = entities;
		this.normalMappedEntities = normalMappedEntities;
		this.lights = lights;
		this.waters = waters;
		this.terrains = terrains;
	}
	
	public static List<Entity> getEntities() { return entities; }

	public List<Entity> getNormalMappedEntities() { return normalMappedEntities; }

	public List<Light> getLights() {
		return lights;
	}

	public List<Terrain> getTerrains() {
		return terrains;
	}

	public List<WaterTile> getWaters() { return waters; }


}
