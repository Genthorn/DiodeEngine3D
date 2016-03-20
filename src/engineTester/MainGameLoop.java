package engineTester;

import OBJLoader.OBJLoader;
import engineTester.entities.Lamp;
import entities.*;
import models.TexturedModel;
import net.GameClient;
import net.packets.Packet00Login;
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
import terrains.World;
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

public class MainGameLoop {

	/*
		BUGS
			-Lamps cast shadows on themselves
			-Distant objects have full shadows on them
			-Normal mapped entities rendered half way into the ground, looks like it's their model
			 but the rock doesnt always stick to the ground

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
			-Collision Detection
			-Colour picking
			-Audio
			-Optimizations
			-Water edges improvements
			-Order ParticleLists
	*/

	public static void main(String[] args) {
		DisplayManager.createDisplay("Diode Engine");

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

		World world = new World();

		//SERVER CLIENT CREATION//
		//GameServer server = new GameServer();
		GameClient client = new GameClient(world, loader, "localhost");
		//server.start();
		//client.start();
		Packet00Login loginPacket = new Packet00Login("ted1");
		loginPacket.writeData(client);
		//////////////////////////

		//PLAYER STUFF//
		TexturedModel playerTex = new TexturedModel(loader.loadToVAO(OBJLoader.loadOBJ("person")), new ModelTexture(loader.loadTexture("playerTexture")));
		Player.LocalPlayer = new PlayerMP(playerTex, new Vector3f(0, 0, 0), 0, 0, 0, 0.4f, "ted", null, -1); 
		entities.add(Player.LocalPlayer);
		////////////////

		//CAMERA//
		Camera camera = new Camera();
		//////////

		//RENDERERS//
		//WATER RENDERER AND FBO TO BE ADDED TO MASTER RENDERER
		MasterRenderer renderer = new MasterRenderer(loader);
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterRenderer waterRenderer = new WaterRenderer(loader, fbos);
		/////////////
		
		ParticleMaster.init(loader);

		//TERRAIN STUFF//
		TerrainTexturePack texturePack = new TerrainTexturePack(loader, "snow", "snow", "snow", "mud");
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		terrains.add(new Terrain(0, -1, loader, texturePack, blendMap, "heightMap"));
		/////////////////

		//LAMP//
		Lamp lamp = new Lamp(loader, new Vector3f(Player.LocalPlayer.getPosition().x, terrains.get(0).getHeightOfTerrain(Player.LocalPlayer.getPosition().x, Player.LocalPlayer.getPosition().z), Player.LocalPlayer.getPosition().z), 1);
		//entities.add(lamp);
		lights.add(lamp.getLight());
		////////

		//SUN//
		Light sun = new Light(new Vector3f(20000,40000,20000), new Vector3f(0.7f, 0.7f, 0.7f));
		lights.add(sun);
		///////

		//WATER TILE//
		WaterTile water = new WaterTile(75,-75,0);
		waters.add(water);
		//////////////

		//PARTICLES//
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleStar"), 1);
		ParticleSystem system = new ParticleSystem(particleTexture, 30, 10, 1f, 7, 1f);
		system.randomizeRotation();
		system.setDirection(new Vector3f(10, 1, 0), 1.5f);
		system.setLifeError(0.1f);
		system.setSpeedError(0.4f);
		system.setScaleError(0.8f);
		/////////////

		//MOUSE PICKER//
		MousePicker picker = new MousePicker(terrains.get(0));
		////////////////

//		//NORMAL MAPPED BARREL//
//		TexturedModel barrelModel = new TexturedModel(NormalMappedOBJLoader.loadOBJ("barrel", loader), new ModelTexture(loader.loadTexture("barrel")));
//		barrelModel.getTexture().setShineDamper(10);
//		barrelModel.getTexture().setReflectivity(0.5f);
//		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
//		Entity barrel = new Entity(barrelModel, new Vector3f(1,1,1), 0, 0, 0, 1f);
//		normalMappedEntities.add(barrel);
//		////////////////////////
//
//		//NORMAL MAPPED ROCK//
//		TexturedModel boulderModel = new TexturedModel(NormalMappedOBJLoader.loadOBJ("boulder", loader),
//				new ModelTexture(loader.loadTexture("boulder")));
//		boulderModel.getTexture().setShineDamper(10);
//		boulderModel.getTexture().setReflectivity(0.5f);
//		boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
//		Entity boulder = new Entity(boulderModel, new Vector3f(1,1,1), 0, 0, 0, 1f);
//		normalMappedEntities.add(boulder);
//		////////////////////////
//
//		//NORMAL MAPPED CRATE//
		TexturedModel crateModel = new TexturedModel(NormalMappedOBJLoader.loadOBJ("crate", loader),
				new ModelTexture(loader.loadTexture("crate")));
//		crateModel.getTexture().setShineDamper(10);
//		crateModel.getTexture().setReflectivity(0.5f);
//		crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
//		Entity crate = new Entity(crateModel, new Vector3f(1,1,1), 0, 0, 0, 0.05f);
//		////////////////////////
//
//		//RANDOM GENERATE WORLD ENTITIES//
//		Random random = new Random();
		TexturedModel model = new TexturedModel(loader.loadToVAO(OBJLoader.loadOBJ("lamp")), new ModelTexture(loader.loadTexture("tree")));
//		for(int i = 0; i < 500; i++) {
//
//			float x = random.nextFloat() * 800 - 400;
//			float z = random.nextFloat() * -600;
//			float y = terrains.get(0).getHeightOfTerrain(x, z);
//
			entities.add(new Entity(model, new Vector3f(0,0,-100), 0, 0, 0, 1f));
//
//		}
//		//////////////////////////////////

		world.add(terrains, entities, normalMappedEntities, lights, waters);

		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			world.add(terrains, entities, normalMappedEntities, lights, waters);
			Player.LocalPlayer.move(terrains, entities);
			camera.update();
			
			lights.sort(new LightComparator(Player.LocalPlayer));
			picker.update();

			//if(picker.getCurrentTerrainPoint() != null) crate.setPosition(picker.getCurrentTerrainPoint());

			for(Entity e : normalMappedEntities) {
				if(e.getModel() == crateModel) System.out.println(e.getPosition().y);
			}

			ParticleMaster.update(camera);
			
			//WATER RENDERING SCENE STUFF//
//			fbos.bindReflectionFrameBuffer();
//			float distance = 2 * (camera.getPosition().y - water.getHeight());
//			camera.getPosition().y -= distance;
//			camera.invertPitch();
//			renderer.renderWorld(world, camera, new Vector4f(0, 1, 0, -water.getHeight()+1.2f));
//			camera.getPosition().y += distance;
//			camera.invertPitch();
//
//			fbos.bindRefractionFrameBuffer();
//			renderer.renderWorld(world, camera, new Vector4f(0, -1, 0, water.getHeight()));
//
			fbos.unbindCurrentFrameBuffer();
			////////////////////////////////
			
			//RENDER EVERYTHING ELSE//
			renderer.renderShadowMap(entities, sun);
			renderer.renderWorld(world, camera, new Vector4f(0, 1, 0, 10000000));
			//waterRenderer.render(waters, camera, sun);
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