package engineTester;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {
	
	//EPISODE 12
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		RawModel model = OBJLoader.loadObjModel("stall", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
		texture.setShineDamper(10);
		texture.setReflectivity(0);
		TexturedModel texturedModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texturedModel, new Vector3f(0,0,-50),0,0,0,1);
		Camera camera = new Camera();
		Light light = new Light(new Vector3f(0,0,-20), new Vector3f(1,1,1));
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			
			entity.increaseRotation(0, 0.1f, 0);
			camera.move();
			
			renderer.processEntity(entity);
			
			renderer.render(light, camera);
			
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
