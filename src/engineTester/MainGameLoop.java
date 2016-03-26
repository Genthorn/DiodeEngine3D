package engineTester;

import OBJLoader.OBJLoader;
import entities.*;
import guis.GUITexture;
import models.TexturedModel;
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
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterTile;

import java.util.ArrayList;
import java.util.List;

public class MainGameLoop {

	/*
	    TO BE FIXED
	        -Water rendering to FBO makes camera rotate around point off
	         in the distance
	        -Particles
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

        World world = new World();

        //PLAYER STUFF//
        TexturedModel playerTex = new TexturedModel(loader.loadToVAO(OBJLoader.loadOBJ("person")), new ModelTexture(loader.loadTexture("playerTexture")));
        Player.LocalPlayer = new PlayerMP(playerTex, new Vector3f(86.26708f, -4.398244f, -109.75052f), 0, 0, 0, 0.4f, "ted", null, -1);
        entities.add(Player.LocalPlayer);
        ////////////////

        //CAMERA//
        Camera camera = new Camera();
        //////////

        //RENDERERS//
        MasterRenderer renderer = new MasterRenderer(loader);
        /////////////

        //WATER//
        WaterTile waterTile = new WaterTile(75,-75,0);
        waters.add(waterTile);
        WaterFrameBuffers fbos = new WaterFrameBuffers();
        WaterRenderer waterRenderer = new WaterRenderer(loader, fbos);
        /////////

        //TERRAIN STUFF//
        TerrainTexturePack texturePack = new TerrainTexturePack(loader, "snow", "snow", "snow", "mud");
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        terrains.add(new Terrain(0, -1, loader, texturePack, blendMap, "heightMap"));
        /////////////////

        //SUN//
        Light sun = new Light(new Vector3f(20000, 40000, 20000), new Vector3f(0.7f, 0.7f, 0.7f));
        lights.add(sun);
        ///////

        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            world.add(terrains, entities, normalMappedEntities, lights, waters);
            Player.LocalPlayer.move(terrains, entities);
            camera.update();

            //RENDER EVERYTHING ELSE//
            renderer.renderShadowMap(entities, sun);
            renderer.renderWorld(world, new Vector4f(0, 1, 0, 10000000));
            waterRenderer.render(waters, camera, sun);
            renderer.renderGUIList(guis);
            //////////////////////////

            DisplayManager.updateDisplay();
        }

        System.out.println("Game closed : Cleaned up!!");
        renderer.cleanUp();
        waterRenderer.cleanUp();
        loader.cleanUp();
        fbos.cleanUp();
        DisplayManager.closeDisplay();

    }

}