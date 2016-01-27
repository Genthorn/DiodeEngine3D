package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;

public class MainGameLoop {
	
	// EPISODE 23

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();

		// *********TERRAIN TEXTURE STUFF***********

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("flowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		// *****************************************
		
		TexturedModel grass = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));
		TexturedModel fern = new TexturedModel(OBJLoader.loadOBJModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
		TexturedModel bobble = new TexturedModel(OBJLoader.loadOBJModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));
		TexturedModel playerTex = new TexturedModel(OBJLoader.loadOBJModel("person", loader), new ModelTexture(loader.loadTexture("playerTexture")));
		
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);
		
		Player player = new Player(playerTex, new Vector3f(0, 0, 0), 0, 0, 0, 1);


		Light light = new Light(new Vector3f(20000, 20000, 20000), new Vector3f(1, 1, 1));

		Terrain terrain = new Terrain(0, -1, loader, texturePack,blendMap, "heightMap");
		
		Camera camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer();

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			
			float x = random.nextFloat() * 400 - 200;
			float z = random.nextFloat() * - 400;
			float y = terrain.getHeightOfTerrain(x, z);
			
			if (i % 6 == 0) entities.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 1.8f));
			if (i % 8 == 0) entities.add(new Entity(flower, new Vector3f(x, y, z), 0, 0, 0, 2.3f));
			
			if (i % 3 == 0) entities.add(new Entity(fern, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
			if (i % 7 == 0) entities.add(new Entity(bobble, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));
			
			
		}
		
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move(terrain);
			
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			
			for (Entity entity: entities) {
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
