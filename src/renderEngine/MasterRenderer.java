package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Player;
import guis.GUIShader;
import models.TexturedModel;

import normalMapRenderer.NormalMappingRenderer;

import normalMapRenderer.NormalMappingShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector4f;

import shaders.StaticShader;
import shadows.ShadowMapMasterRenderer;
import shadows.ShadowShader;
import skybox.SkyboxRenderer;
import skybox.SkyboxShader;
import terrains.Terrain;
import terrains.TerrainShader;
import terrains.World;
import entities.Camera;
import entities.Entity;
import entities.Light;
import guis.GUIRenderer;
import guis.GUITexture;
import toolbox.LightComparator;

public class MasterRenderer {
    // SKYCOLOUR
    public static final float SKY_RED = 0.80392156862f;
    public static final float SKY_GREEN = 0.78823529411f;
    public static final float SKY_BLUE = 0.78823529411f;

    private StaticShader shader = new StaticShader();
    private EntityRenderer entityRenderer;

    private TerrainShader terrainShader = new TerrainShader();
    private TerrainRenderer terrainRenderer;

    private NormalMappingShader normalMappingShader = new NormalMappingShader();
    private NormalMappingRenderer normalMappingRenderer;

    private SkyboxShader skyboxShader = new SkyboxShader();
    private SkyboxRenderer skyboxRenderer;

    private ShadowShader shadowShader = new ShadowShader();
    private ShadowMapMasterRenderer shadowMapRenderer;

    private GUIShader guiShader = new GUIShader();
    private GUIRenderer guiRenderer;

    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
    private Map<TexturedModel, List<Entity>> normalmapEntities = new HashMap<TexturedModel, List<Entity>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();

    public MasterRenderer(Loader loader) {
        enableCulling();
        entityRenderer = new EntityRenderer(shader);
        terrainRenderer = new TerrainRenderer(terrainShader);
        skyboxRenderer = new SkyboxRenderer(skyboxShader, loader);
        guiRenderer = new GUIRenderer(guiShader, loader);
        shadowMapRenderer = new ShadowMapMasterRenderer(shadowShader);
        normalMappingRenderer = new NormalMappingRenderer(normalMappingShader);
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

    public void renderWorld(World world, Vector4f clipPlane) {
        renderScene(World.getEntities(), world.getNormalMappedEntities(), world.getTerrains(), world.getLights(),
                clipPlane);
    }

    public void renderScene(List<Entity> entities, List<Entity> normalMapEntities, List<Terrain> terrains,
                            List<Light> lights, Vector4f clipPlane) {

        for (Terrain terrain : terrains) {
            processTerrain(terrain);
        }

        for (Entity entity : entities) {
            if (Camera.viewFrustum.isSphereInside(entity.getBoundingSphere())) {
                processEntity(entity);
            }
        }

        for (Entity entity : normalMapEntities) {
            if (Camera.viewFrustum.isSphereInside(entity.getBoundingSphere())) {
                processNormalMapEntity(entity);
            }
        }

        render(lights, clipPlane);
    }

    public void render(List<Light> lights, Vector4f clipPlane) {
        lights.sort(new LightComparator(Player.LocalPlayer));

        prepare();
        shader.start();
        shader.loadMapSize(shadowMapRenderer.getShadowMapSize());
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColour(SKY_RED, SKY_GREEN, SKY_BLUE);
        shader.loadLights(lights);
        shader.loadViewMatrix();
        entityRenderer.render(entities, shadowMapRenderer.getToShadowMapSpaceMatrix());
        shader.stop();
        normalMappingRenderer.render(normalmapEntities, clipPlane, lights);
        terrainShader.start();
        terrainShader.loadMapSize(shadowMapRenderer.getShadowMapSize());
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColour(SKY_RED, SKY_GREEN, SKY_BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix();
        terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix(),
                shadowMapRenderer.getShadowDistance());
        terrainShader.stop();
        skyboxRenderer.render();

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
        GL11.glClearColor(SKY_RED, SKY_GREEN, SKY_BLUE, 1);
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processTerrain(List<Terrain> terrain) {
        for (Terrain currentTerrain : terrain) {
            terrains.add(currentTerrain);
        }

    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null)
            batch.add(entity);
        else {
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }

    }

    public void processNormalMapEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = normalmapEntities.get(entityModel);
        if (batch != null)
            batch.add(entity);
        else {
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            normalmapEntities.put(entityModel, newBatch);
        }

    }

    public void renderShadowMap(List<Entity> entityList, Light sun) {
        for (Entity entity : entityList) {
            if (Camera.viewFrustum.isSphereInside(entity.getBoundingSphere())) processEntity(entity);
        }
        shadowMapRenderer.render(entities, sun);
        entities.clear();
    }

    public int getShadowMapTexture() {
        return shadowMapRenderer.getShadowMap();
    }

    public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
        guiShader.cleanUp();
        normalMappingShader.cleanUp();
        shadowShader.cleanUp();
        shadowMapRenderer.cleanUp();
    }
}
