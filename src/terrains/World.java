package terrains;

import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import entities.Light;

public class World {
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Light> lights = new ArrayList<Light>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private String worldFilePath = "";
	
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
