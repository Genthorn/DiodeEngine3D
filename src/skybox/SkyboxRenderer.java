package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import entities.Camera;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.MasterRenderer;

public class SkyboxRenderer {
	private static final float SIZE = 500f;

	private static final float[] VERTICES = {

			-SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE,

			SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE,

			-SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE
	};

	private static String[] TEXTURE_FILES = { "right", "left", "top", "bottom", "back", "front" };

	private RawModel cube;
	private int texture;
	private SkyboxShader shader;

	public SkyboxRenderer(SkyboxShader shader, Loader loader) {
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		this.shader = shader;
	}

	public void render() {
		shader.start();
		shader.loadProjectionMatrix(Camera.projectionMatrix);
		shader.loadViewMatrix();
		shader.loadFogColour(MasterRenderer.SKY_RED, MasterRenderer.SKY_GREEN, MasterRenderer.SKY_BLUE);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

}