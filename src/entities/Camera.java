package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import toolbox.ViewFrustum;

public class Camera
{

	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.7f;
	public static final float FAR_PLANE = 900.0f;

	public static Matrix4f rotationMatrix = new Matrix4f();
	public static Matrix4f viewMatrix = new Matrix4f();
	public static Matrix4f projectionMatrix = new Matrix4f();
	public static ViewFrustum viewFrustum = new ViewFrustum();
	public static float aspectRatio;

	// private float MIN_HEIGHT_ABOVE_TERRAIN = 2;
	private float distanceFromPlayer = 20;
	private Player m_player;

	public static Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 0;
	private float yaw = 0;
	private boolean hasRunOnce = false;

	public Camera(Player player)
	{
		m_player = player;
		updateProjection();
	}

	private void updateProjection()
	{
		aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float nearHeight = 1.0f / (float) Math.tan(Math.toRadians(Camera.FOV / 2.0f));
		float frustum_length = NEAR_PLANE - FAR_PLANE;

		projectionMatrix.m00 = nearHeight / aspectRatio;
		projectionMatrix.m11 = nearHeight;
		projectionMatrix.m22 = (FAR_PLANE + NEAR_PLANE) / frustum_length;
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = (2.0f * NEAR_PLANE * FAR_PLANE) / frustum_length;
		projectionMatrix.m33 = 0;
	}
	public void update()
	{
		calculateZoom();
		if (hasRunOnce)
			calculatePitch();
		calculateAngleAroundPlayer();

		float theta = m_player.getRotY();
		theta += yaw;
		rotationMatrix.setIdentity();
		rotationMatrix.rotate((float) Math.toRadians(theta), new Vector3f(0, 1, 0));
		rotationMatrix.rotate((float) Math.toRadians(-pitch), new Vector3f(1, 0, 0));

		Vector4f newPosition = new Vector4f(0, 0, distanceFromPlayer, 1);
		Matrix4f.transform(rotationMatrix, newPosition, newPosition);
		position = new Vector3f(newPosition);

		viewMatrix.setIdentity();
		viewMatrix.translate(position);
		Matrix4f.mul(viewMatrix, rotationMatrix, viewMatrix);
		viewMatrix.invert();

		viewFrustum.update();
		hasRunOnce = true;
	}

	// private void preventCameraTerrainCollision()
	// {
	// float terrainHeightAtCurPos =
	// player.getCurrentTerrain().getHeightOfTerrain(position.x, position.z);
	// if (position.y < terrainHeightAtCurPos + MIN_HEIGHT_ABOVE_TERRAIN)
	// {
	// position.y = (terrainHeightAtCurPos + MIN_HEIGHT_ABOVE_TERRAIN);
	// }
	// else if (position.y > terrainHeightAtCurPos + MIN_HEIGHT_ABOVE_TERRAIN)
	// {
	// }
	// }
	public void invertPitch()
	{
		this.pitch = -pitch;
	}
	public void setPosition(Vector3f newPosition)
	{
		position.set(newPosition);
	}
	public void setPitch(float pitch)
	{
		this.pitch = pitch;
	}
	public void setYaw(float yaw)
	{
		this.yaw = yaw;
	}
	public void setRoll(float roll)
	{
	}
	private void calculateZoom()
	{
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	private void calculatePitch()
	{
		// float terrainHeightAtCurPos =
		// player.getCurrentTerrain().getHeightOfTerrain(position.x, position.z)
		// + MIN_HEIGHT_ABOVE_TERRAIN;

		if (Mouse.isButtonDown(0))
		{
			float pitchChange = Mouse.getDY() * 0.28f;
			// if(!onGround)
			pitch -= pitchChange;
			// else pitch = terrainHeightAtCurPos;
		}

		if (Mouse.isButtonDown(1))
		{
			float pitchChange = Mouse.getDY() * 0.28f;
			// if(!onGround)
			pitch -= pitchChange;
			// else pitch += terrainHeightAtCurPos;
		}

	}
	private void calculateAngleAroundPlayer()
	{
		if (Mouse.isButtonDown(0))
		{
			float angleChange = Mouse.getDX() * 0.28f;
			yaw -= angleChange;
		}

		if (Mouse.isButtonDown(1))
		{
			float angleChange = Mouse.getDX() * 0.28f;
			m_player.increaseRotation(0, -angleChange, 0);
			yaw = 0;
		}
	}
}