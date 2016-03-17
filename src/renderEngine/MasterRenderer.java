package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TexturedModel;

import normalMapRenderer.NormalMappingRenderer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import shaders.StaticShader;
import shadows.ShadowMapMasterRenderer;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import terrains.TerrainShader;
import terrains.World;
import entities.Camera;
import entities.Entity;
import entities.Light;
import guis.GUIRenderer;
import guis.GUITexture;
import toolbox.ViewFrustum;

public class MasterRenderer {
	//SKYCOLOUR
	public static final float RED = 0.80392156862f;
	public static final float GREEN = 0.78823529411f;
	public static final float BLUE = 0.78823529411f;
	
	private Matrix4f projectionMatrix;

	public ViewFrustum viewFrustum;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer entityRenderer;
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;

	private NormalMappingRenderer normalMappingRenderer;
	
	private SkyboxRenderer skyboxRenderer;
	private ShadowMapMasterRenderer shadowMapRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<Entity>> normalmapEntities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private GUIRenderer guiRenderer;
	
	private Camera camera;
	
	public MasterRenderer(Loader loader, Camera camera) {
		enableCulling();
		createProjectionMatrix();
		this.camera = camera;
		entityRenderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		guiRenderer = new GUIRenderer(loader);
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);
		normalMappingRenderer = new NormalMappingRenderer(projectionMatrix);
		viewFrustum = new ViewFrustum(camera);
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK_RIGHT);
		GL11.glCullFace(GL11.GL_BACK_LEFT);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void renderWorld(World world, Camera camera, Vector4f clipPlane) {
		renderScene(world.getEntities(), world.getNormalMappedEntities(), world.getTerrains(), world.getLights(), camera, clipPlane);
	}
	
	public void renderScene(List<Entity> entities, List<Entity> normalMapEntities, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane) {
		for(Terrain terrain : terrains) {
			processTerrain(terrain);
		}

		int numberOfEntitiesRendered = 0;
		int numberOfNonNormalMapped = 0;
		int numberOfNormalMapped = 0;

		for(Entity entity : entities) {
			if(viewFrustum.isSphereInside(entity.getBoundingSphere())) {
			//if(viewFrustum.isPointInside(entity.getPosition())) {
				processEntity(entity);
				numberOfEntitiesRendered++;
				numberOfNonNormalMapped++;
			//}
			}
		}

		for(Entity entity : normalMapEntities) {
			if(viewFrustum.isSphereInside(entity.getBoundingSphere())) {
			//if(viewFrustum.isPointInside(entity.getPosition())) {
				processNormalMapEntity(entity);
				numberOfEntitiesRendered++;
				numberOfNormalMapped++;
			//}
			}
		}
		
		render(lights, camera, clipPlane);
		System.out.println("Number of rendered entities: " + numberOfEntitiesRendered +
				", Not Normal Mapped: " + numberOfNonNormalMapped
				+ ", Normal Mapped: " + numberOfNormalMapped);

	}
	
	public void render(List <Light> lights, Camera camera, Vector4f clipPlane) {
		viewFrustum.update();

		prepare();
		shader.start();
		shader.loadMapSize(shadowMapRenderer.getShadowMapSize());
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		entityRenderer.render(entities, shadowMapRenderer.getToShadowMapSpaceMatrix());
		shader.stop();
		normalMappingRenderer.render(normalmapEntities, clipPlane, lights, camera);
		terrainShader.start();
		terrainShader.loadMapSize(shadowMapRenderer.getShadowMapSize());
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix(), shadowMapRenderer.getShadowDistance());
		terrainShader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);

		terrains.clear();
		entities.clear();
		normalmapEntities.clear();
	}
	
	public void renderGUIList(List<GUITexture> guis) {
		guiRenderer.render(guis);
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void processTerrain(List<Terrain> terrain) {
		for (Terrain currentTerrain: terrain) {
			terrains.add(currentTerrain);
		}
		
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch != null) batch.add(entity);
		else { 
			List<Entity> newBatch = new ArrayList<Entity>(); 
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
		
	}

	public void processNormalMapEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = normalmapEntities.get(entityModel);
		if(batch != null) batch.add(entity);
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			normalmapEntities.put(entityModel, newBatch);
		}

	}
	
	private void createProjectionMatrix(){
    	projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(camera.FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = camera.FAR_PLANE - camera.NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((camera.FAR_PLANE + camera.NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * camera.NEAR_PLANE * camera.FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
    }
	
	public void renderShadowMap(List<Entity> entityList, Light sun) {
		for(Entity entity : entityList) {
			processEntity(entity);
		}
		shadowMapRenderer.render(entities, sun);
		entities.clear();
	}

	public int getShadowMapTexture() {
		return shadowMapRenderer.getShadowMap();
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
		shadowMapRenderer.cleanUp();
		guiRenderer.cleanUp();
		normalMappingRenderer.cleanUp();
	}
}
