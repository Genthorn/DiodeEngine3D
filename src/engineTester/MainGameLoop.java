package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.LightComparator;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import galileo.collada.ReadCollada;
import galileo.collada.nodes.RootColladaNode;

public class MainGameLoop {
	
	// EPISODE 26
	
	//TO-DO//
	/*
	
	Whole Engine
		-Architectured system
	
	Entities
		-Animations (All Entities)
		-Strafing (Player Only)
		-Don't let camera go past 180 degree yaw (Camera Only)
		
	Shadows
		-Make them look perdy
		-load shadowDistance in terrainVertexShader as uniform to be 
		 set as same as in shadowBox class
		-Shadows from all light sources
		
	Rendering
		-Anti-aliasing(MSAA)
		
	Optimizing
		-Only update lights when they change
		-Frustum Culling
	
	
	*/
	/////////

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
		
		RawModel model = OBJLoader.loadOBJModel("tree", loader);

		TexturedModel tree = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));

		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		fernTexture.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJLoader.loadOBJModel("fern", loader), fernTexture);

		TexturedModel bobble = new TexturedModel(OBJLoader.loadOBJModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));
		TexturedModel lamp = new TexturedModel(OBJLoader.loadOBJModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));
		lamp.getTexture().setUseFakeLighting(true);
		
		TexturedModel playerTex = new TexturedModel(OBJLoader.loadOBJModel("person", loader), new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(playerTex, new Vector3f(153, 5, -274), 0, 100, 0, 0.4f);
		
		Light sun = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(0.7f, 0.7f, 0.7f));
		List<Light> lights = new ArrayList<Light>();
		lights.add(sun);
		lights.add(new Light(new Vector3f(185,10,-293), new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(370,17,-300), new Vector3f(0,2,2), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(293,7,-305), new Vector3f(2,2,0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(250,17,-305), new Vector3f(2,2,0), new Vector3f(1, 0.01f, 0.002f)));
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(new Terrain(0, -1, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(0, -2, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(0, -3, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(0, -4, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(-1, -1, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(-1, -2, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(-1, -3, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(-1, -4, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(-2, -1, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(-2, -2, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(-2, -3, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(-2, -4, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(-3, -1, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(-3, -2, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(-3, -3, loader, texturePack, blendMap, "heightMap"));
		terrains.add(new Terrain(-3, -4, loader, texturePack, blendMap, "heightMap"));
		
		
		
		Camera camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer(loader, camera);

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			if (i % 7 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrains.get(0).getHeightOfTerrain(x, z);
				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
				
				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = terrains.get(0).getHeightOfTerrain(x, z);
				entities.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 1.8f));

				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = terrains.get(0).getHeightOfTerrain(x, z);
				entities.add(new Entity(flower, new Vector3f(x, y, z), 0, 0, 0, 2.3f));
			}

			if (i % 3 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrains.get(0).getHeightOfTerrain(x, z);
				entities.add(new Entity(bobble, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));

				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = terrains.get(0).getHeightOfTerrain(x, z);
				entities.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));
			}

		}
		
		entities.add(new Entity(lamp, new Vector3f(185,-4.7f,-293), 0,0,0,1));
		entities.add(new Entity(lamp, new Vector3f(370,4.2f,-300), 0,0,0,1));
		entities.add(new Entity(lamp, new Vector3f(293,-6.8f,-305), 0,0,0,1));
		
		entities.add(player);
		
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move(terrains); 
			
			renderer.renderShadowMap(entities, sun);
			
			renderer.processTerrain(terrains);
			
			for (Entity entity: entities) {
				renderer.processEntity(entity);
			}
			
			lights.sort(new LightComparator(player));
			
			renderer.render(lights, camera);
			DisplayManager.updateDisplay();
			
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) break; 
		}
		
		System.out.println("cleanedup");
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
