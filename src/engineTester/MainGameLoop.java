package engineTester;

import OBJLoader.OBJLoader;
import engineTester.entities.Lamp;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import normalMapOBJLoader.NormalMappedOBJLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.LightComparator;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

	/*
		BUGS
			-Lamps cast shadows on themselves
		FIXES
			BUG: Water flickers in distance
				FIX: FIND A WAY TO INCREASE AND DECREASE CLIPPING PLANE
					 VALUE FROM CAMERA ANGLE

			BUG: When player walks into it's shadow it renders on top of the player
				FIX: Moved shadows render order above rest of scene
	*/

	/*
		OPTIMIZATIONS
			-Only update lights that change
			-Change rendering for particles
			-Frustum Culling
	*/

	/*
		To-Do (In order of importance)
			-Narrow-Phase Collision Detection
			-Colour picking
			-Audio
			-Optimizations
			-Anti-Aliasing
			-Order ParticleLists
	*/
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();

		//LIST OF GAME OBJECTS//
		List<Terrain> terrains = new ArrayList<Terrain>();
		List<Light> lights = new ArrayList<Light>();
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMappedEntities = new ArrayList<Entity>();
		List<WaterTile> waters = new ArrayList<WaterTile>();
		////////////////////////

		//LOADER//
		Loader loader = new Loader();
		//////////

		//PLAYER STUFF//
		TexturedModel playerTex = new TexturedModel(loader.loadToVAO(OBJLoader.loadOBJ("person")), new ModelTexture(loader.loadTexture("playerTexture")));
		Player player = new Player(playerTex, new Vector3f(153, 5, -274), 0, 100, 0, 0.4f);
		entities.add(player);
		////////////////

		//CAMERA//
		Camera camera = new Camera(player);
		//////////

		//RENDERERS//
		//WATER RENDERER AND FBO TO BE ADDED TO MASTER RENDERER
		MasterRenderer renderer = new MasterRenderer(loader, camera);
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterRenderer waterRenderer = new WaterRenderer(loader, renderer.getProjectionMatrix(), fbos);
		/////////////
		
		ParticleMaster.init(loader, renderer.getProjectionMatrix());

		//TERRAIN STUFF//
		TerrainTexturePack texturePack = new TerrainTexturePack(loader, "snow", "snow", "snow", "mud");
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");
		terrains.add(terrain);
		/////////////////

		//LAMP//
		Lamp lamp = new Lamp(loader, new Vector3f(player.getPosition().x, terrain.getHeightOfTerrain(player.getPosition().x, player.getPosition().z), player.getPosition().z), 1);
		entities.add(lamp);
		lights.add(lamp.getLight());
		////////

		//RANDOM GENERATE WORLD ENTITIES//
		Random random = new Random();
		TexturedModel model = new TexturedModel(loader.loadToVAO(OBJLoader.loadOBJ("tree")), new ModelTexture(loader.loadTexture("tree")));
		for(int i = 0; i < 200; i++) {
			
			float x = random.nextFloat() * 800 - 400;
			float z = random.nextFloat() * -600;
			float y = terrain.getHeightOfTerrain(x, z);
			
			entities.add(new Entity(model, new Vector3f(x,y,z), 0, random.nextFloat() * 360, 0, 10));
		}
		//////////////////////////////////

		//SUN//
		Light sun = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(0.7f, 0.7f, 0.7f));
		lights.add(sun);
		///////

		//WATER TILE//
		WaterTile water = new WaterTile(75,-75,0);
		waters.add(water);
		//////////////

		//PARTICLES//
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas"), 4);
		ParticleSystem system = new ParticleSystem(particleTexture, 700, 10, 1f, 7, 0.4f);
		system.randomizeRotation();
		system.setDirection(new Vector3f(10, 1, 0), 1.5f);
		system.setLifeError(0.1f);
		system.setSpeedError(0.4f);
		system.setScaleError(0.8f);
		/////////////

		//MOUSE PICKER//
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		////////////////

		//NORMAL MAPPED BARREL//
		TexturedModel barrel = new TexturedModel(NormalMappedOBJLoader.loadOBJ("barrel", loader), new ModelTexture(loader.loadTexture("barrel")));
		barrel.getTexture().setShineDamper(10);
		barrel.getTexture().setReflectivity(0.5f);
		barrel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		normalMappedEntities.add(new Entity(barrel, new Vector3f(75,10,-75f), 0, 0, 0, 1f));
		////////////////////////

		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			player.move(terrains, entities);
			camera.move();
			lights.sort(new LightComparator(player));
			picker.update();

			//system.generateParticles(new Vector3f(player.getPosition().x, player.getPosition().y + 40, player.getPosition().z));

			ParticleMaster.update(camera);
			
			//WATER RENDERING SCENE STUFF//
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, normalMappedEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight()+1.2f));
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			fbos.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalMappedEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));
			
			fbos.unbindCurrentFrameBuffer();
			////////////////////////////////
			
			//RENDER EVERYTHING ELSE//
			renderer.renderShadowMap(entities, sun);
			renderer.renderScene(entities, normalMappedEntities, terrains, lights, camera, new Vector4f(0, 1, 0, 10000000));
			waterRenderer.render(waters, camera, sun);
			//renderer.renderGUIList(guis);
			ParticleMaster.renderParticles(camera);
			//////////////////////////
			
			DisplayManager.updateDisplay();
		}
		
		System.out.println("Game closed : Cleaned up!!");
		renderer.cleanUp();
		waterRenderer.cleanUp();
		loader.cleanUp();
		fbos.cleanUp();
		ParticleMaster.cleanUp();
		DisplayManager.closeDisplay();

	}

}