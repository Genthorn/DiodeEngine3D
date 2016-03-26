package terrains;

import java.util.ArrayList;
import java.util.List;

import water.WaterTile;
import entities.Entity;
import entities.Light;

public class World {
	public static final float GRAVITY = -100;

	private List<Terrain> m_terrains = new ArrayList<Terrain>();
	private List<Light> m_lights = new ArrayList<Light>();
	private static List<Entity> m_entities = new ArrayList<Entity>();
	private List<Entity> m_normalMappedEntities = new ArrayList<Entity>();
	private List<WaterTile> m_waters = new ArrayList<WaterTile>();

	private String worldFilePath = "";

	public World() {
	}

	public World(String worldFile) {
		worldFilePath = worldFile;
		loadWorldFile();
	}

	private void loadWorldFile() {
		//TO BE LOADED
	}

	public void addEntity(Entity entity)
	{
		m_entities.add(entity);
	}

	public void addNormalMappedEntity(Entity entity)
	{
		m_normalMappedEntities.add(entity);
	}

	public void addLight(Light light)
	{
		m_lights.add(light);
	}

	public void addWater(WaterTile tile)
	{
		m_waters.add(tile);
	}

	public void add(List<Terrain> terrains, List<Entity> entities, List<Entity> normalMappedEntities, List<Light> lights, List<WaterTile> waters) {
		m_entities = entities;
		m_normalMappedEntities = normalMappedEntities;
		m_lights = lights;
		m_waters = waters;
		m_terrains = terrains;
	}
	public static List<Entity> getEntities()
	{
		return m_entities;
	}
	public List<Entity> getNormalMappedEntities()
	{
		return m_normalMappedEntities;
	}
	public List<Light> getLights()
	{
		return m_lights;
	}
	public List<Terrain> getTerrains()
	{
		return m_terrains;
	}
	public List<WaterTile> getWaters()
	{
		return m_waters;
	}
}
