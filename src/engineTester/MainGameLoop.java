package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.LightComparator;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterTile;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;

public class MainGameLoop {
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		List<Light> lights = new ArrayList<Light>();
		List<Entity> entities = new ArrayList<Entity>();
		List<WaterTile> waters = new ArrayList<WaterTile>();
		
		Loader loader = new Loader();
		
		TexturedModel playerTex = new TexturedModel(OBJLoader.loadOBJModel("person", loader), new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(playerTex, new Vector3f(153, 5, -274), 0, 100, 0, 0.4f);
		entities.add(player);
		Camera camera = new Camera(player);
		
		MasterRenderer renderer = new MasterRenderer(loader, camera);
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterRenderer waterRenderer = new WaterRenderer(loader, renderer.getProjectionMatrix(), fbos);
		
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		TerrainTexturePack texturePack = new TerrainTexturePack(loader, "grass", "mud", "flowers", "path");
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");
		terrains.add(terrain);
		
		Random random = new Random();
		TexturedModel model = new TexturedModel(OBJLoader.loadOBJModel("stall", loader), new ModelTexture(loader.loadTexture("stallTexture")));
		for(int i = 0; i < 400; i++) {
			
			float x = random.nextFloat() * 800 - 400;
			float z = random.nextFloat() * -600;
			float y = terrain.getHeightOfTerrain(x, z);
			
			entities.add(new Entity(model, new Vector3f(x,y,z), 0, random.nextFloat() * 360, 0, 1));
		}
		
		Light sun = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(0.7f, 0.7f, 0.7f));
		lights.add(sun);
		
		WaterTile water = new WaterTile(75,-75,0);
		waters.add(water);
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("snow"), 1);
		ParticleSystem system = new ParticleSystem(particleTexture, 700, 10, 0.3f, 7, 0.4f);
		system.randomizeRotation();
		system.setDirection(new Vector3f(10, 1, 0), 1.1f);
		system.setLifeError(0.1f);
		system.setSpeedError(0.4f);
		system.setScaleError(0.8f);
			
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			player.move(terrains, entities);
			camera.move();
			lights.sort(new LightComparator(player));
			
			//system.generateParticles(new Vector3f(player.getPosition().x, player.getPosition().y + 40, player.getPosition().z));
			
			ParticleMaster.update();
			
			//WATER RENDERING SCENE STUFF//
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight()+1.0f));
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			fbos.bindRefractionFrameBuffer();
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));
			
			fbos.unbindCurrentFrameBuffer();
			////////////////////////////////
			
			//RENDER EVERYTHING ELSE//
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, 1, 0, 10000000));
			waterRenderer.render(waters, camera, sun);
			renderer.renderShadowMap(entities, sun);
			//renderer.renderGUIList(guis);
			ParticleMaster.renderParticles(camera);
			//////////////////////////
			
			DisplayManager.updateDisplay();
		}
		
		System.out.println("Game closed : Cleaned up!");
		renderer.cleanUp();
		waterRenderer.cleanUp();
		loader.cleanUp();
		fbos.cleanUp();
		ParticleMaster.cleanUp();
		DisplayManager.closeDisplay();

	}

}
