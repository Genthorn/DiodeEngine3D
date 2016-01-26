package engineTester;

import models.ModelData;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJFileLoader;
import terrains.Terrain;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MainGameLoop {
	
	//EPISODE 15
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		ModelData model_data = OBJFileLoader.loadOBJ("fern");
		RawModel model = loader.loadToVAO(model_data.getVertices(), model_data.getTextureCoords(),
				model_data.getNormals(), model_data.getIndices());
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("fern"));
		texture.setShineDamper(10);
		texture.setReflectivity(0);
		TexturedModel texturedModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texturedModel, new Vector3f(0,0,-25),0,0,0,1);
		
		texture.setHasTransparency(true);
		texture.setUseFakeLighting(true);
		
		Terrain terrain = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass")));
		Camera camera = new Camera();
		Light light = new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1));
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			
			entity.increaseRotation(0, 0.1f, 0);
			camera.move();
			
			renderer.processEntity(entity);
			renderer.processTerrain(terrain);
			
			renderer.render(light, camera);
			
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
