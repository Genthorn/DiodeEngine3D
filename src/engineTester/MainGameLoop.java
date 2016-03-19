package engineTester;

import OBJLoader.OBJLoader;
import engineTester.entities.Lamp;
import entities.*;
import guis.GUITexture;
import models.TexturedModel;
import net.GameClient;
import net.GameServer;
import net.packets.Packet00Login;
import normalMapOBJLoader.NormalMappedOBJLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
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
import java.util.Random;

public class MainGameLoop {

	/*
		ISSUES TO BE RESOLVED
		-------------------------
		 	-Can't seem to add player position to camera position
		 	-Particles don't scale down
		 	-Go through all class and make sure to set
		 	 visibility as needed for all vars
		 	-When rendering everything to fbo, camera rotates
		 	-Water renderer and Water FBO to be moved to MasterRenderer
			-GUI coordinates messed up
			-Projection matrix in camera: m22 and m32 are not negative?

	 */

	public static void main(String[] args) {
		DisplayManager.createDisplay("Diode Engine");

		//LIST OF GAME OBJECTS//
		List<GUITexture> guis = new ArrayList<GUITexture>();
		List<Terrain> terrains = new ArrayList<Terrain>();
		List<Light> lights = new ArrayList<Light>();
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMappedEntities = new ArrayList<Entity>();
		List<WaterTile> waters = new ArrayList<WaterTile>();
		////////////////////////

		//LOADER//
		Loader loader = new Loader();
		//////////

		//guis.add(new GUITexture(loader.loadTexture("texture"), new Vector2f(0, 0), new Vector2f(0.1f, 0.1f)));

		World world = new World();

		//SERVER CLIENT CREATION//
		GameServer server = new GameServer();
		GameClient client = new GameClient(world, loader, "localhost");
		//server.start();
		//client.start();
		Packet00Login loginPacket = new Packet00Login("ted1");
		//loginPacket.writeData(client);
		//////////////////////////

		//PLAYER STUFF//
		TexturedModel playerTex = new TexturedModel(loader.loadToVAO(OBJLoader.loadOBJ("person")), new ModelTexture(loader.loadTexture("playerTexture")));
		PlayerMP player = new PlayerMP(playerTex, new Vector3f(15.282322f, 0.15686035f, -15.762215f), 0, 0, 0, 0.4f, "ted", null, -1);
		entities.add(player);
		////////////////

		//CAMERA//
		Camera camera = new Camera(player);
		//////////

		//RENDERERS//
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
		Lamp lamp = new Lamp(loader, new Vector3f(player.getPosition().x, terrains.get(0).getHeightOfTerrain(player.getPosition().x, player.getPosition().z), player.getPosition().z), 1);
		//entities.add(lamp);
		//lights.add(lamp.getLight());
		////////

		//SUN//
		Light sun = new Light(new Vector3f(20000,40000,20000), new Vector3f(0.7f, 0.7f, 0.7f));
		lights.add(sun);
		///////

		//WATER TILE//
		WaterTile water = new WaterTile(75,-75,0);
		//waters.add(water);
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

		//NORMAL MAPPED BARREL//
		TexturedModel barrelModel = new TexturedModel(NormalMappedOBJLoader.loadOBJ("barrel", loader),
      		new ModelTexture(loader.loadTexture("barrel")));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		Entity barrel = new Entity(barrelModel, new Vector3f(1,1,1), 0, 0, 0, 1f);
		//normalMappedEntities.add(barrel);
		////////////////////////

		//RANDOM GENERATE WORLD ENTITIES//
		Random random = new Random();
		TexturedModel model = new TexturedModel(loader.loadToVAO(OBJLoader.loadOBJ("tree")), new ModelTexture(loader.loadTexture("tree")));
		for(int i = 0; i < 500; i++) {

			float x = random.nextFloat() * 800 - 400;
			float z = random.nextFloat() * -600;
			float y = terrains.get(0).getHeightOfTerrain(x, z);

			entities.add(new Entity(model, new Vector3f(x,y,z), 0, 0, 0, 1f));

		}
		//////////////////////////////////

		world.add(terrains, entities, normalMappedEntities, lights, waters);

		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			world.add(terrains, entities, normalMappedEntities, lights, waters);
			player.move(terrains, entities);
			camera.update();
			lights.sort(new LightComparator(player));
			picker.update();

			ParticleMaster.update(camera);
			system.generateParticles(new Vector3f(10, 0, 10));

			
			//WATER RENDERING SCENE STUFF//
//			fbos.bindReflectionFrameBuffer();
//			float distance = 2 * (Camera.position.y - water.getHeight());
//			Camera.position.y -= distance;
//			camera.invertPitch();
//			renderer.renderWorld(world, camera, new Vector4f(0, 1, 0, -water.getHeight()+1.2f));
//			Camera.position.y += distance;
//			camera.invertPitch();
//
//			fbos.bindRefractionFrameBuffer();
//			renderer.renderWorld(world, camera, new Vector4f(0, -1, 0, water.getHeight()));
//
//			fbos.unbindCurrentFrameBuffer();
			//////////////////////////////
			
			//RENDER EVERYTHING ELSE//
			renderer.renderShadowMap(entities, sun);
			renderer.renderWorld(world, camera, new Vector4f(0, 1, 0, 10000000));
			waterRenderer.render(waters, camera, sun);
			renderer.renderGUIList(guis);
			ParticleMaster.renderParticles(camera);
			//////////////////////////

			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		waterRenderer.cleanUp();
		loader.cleanUp();
		fbos.cleanUp();
		ParticleMaster.cleanUp();
		DisplayManager.closeDisplay();
		System.out.println("Game closed : Cleaned up!!");

	}

}