package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import toolbox.ViewFrustum;

public class Camera {

	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.7f;
	public static final float FAR_PLANE = 900.0f;

	public static Matrix4f rotationMatrix = new Matrix4f();
	public static Matrix4f viewMatrix = new Matrix4f();
	public static Matrix4f projectionMatrix = new Matrix4f();
	public static ViewFrustum viewFrustum = new ViewFrustum();
	public static float aspectRatio;

	private float distanceFromPlayer = 20;
	private float angleAroundPlayer = 0;
	private Player player;

	public static Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 0;
	private float yaw = 0;
	private boolean hasRunOnce = false;

	public Camera(Player player) {
		this.player = player;
		updateProjection();
	}

	public void update() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();

		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);

		float theta = player.getRotY();
		theta += angleAroundPlayer;
		rotationMatrix.setIdentity();
		rotationMatrix.rotate((float) Math.toRadians(theta), new Vector3f(0, 1, 0));
		rotationMatrix.rotate((float) Math.toRadians(-pitch), new Vector3f(1, 0, 0));

		Vector4f newPosition = new Vector4f(0, 0, distanceFromPlayer, 1);
		Matrix4f.transform(rotationMatrix, newPosition, newPosition);
		position = new Vector3f(newPosition);

		viewMatrix.setIdentity();
		viewMatrix.translate(new Vector3f(position.x, position.y, position.z));
		Matrix4f.mul(viewMatrix, rotationMatrix, viewMatrix);
		viewMatrix.invert();

		viewFrustum.update();
		this.yaw = 180 + (player.getRotY() + angleAroundPlayer);
		hasRunOnce = true;
	}

	private void calculateCameraPosition(float horizDistance, float verticDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance;
	}

	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}

	private void updateProjection() {
		aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float nearHeight = (float) (1.0f / Math.tan(Math.toRadians(Camera.FOV / 2.0f)));
		float frustum_length = NEAR_PLANE - FAR_PLANE;

		projectionMatrix.m00 = nearHeight / aspectRatio;
		projectionMatrix.m11 = nearHeight;
		projectionMatrix.m22 = ((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = ((2.0f * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}

	private void calculatePitch() {
		if(hasRunOnce) {
			if (Mouse.isButtonDown(0)) {
				float pitchChange = Mouse.getDY() * 0.28f;
				pitch -= pitchChange;
			}

			if (Mouse.isButtonDown(1)) {
				float pitchChange = Mouse.getDY() * 0.28f;
				pitch -= pitchChange;
			}
		}

	}

	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.28f;
			angleAroundPlayer -= angleChange;
		}

		if (Mouse.isButtonDown(1)) {
			float angleChange = Mouse.getDX() * 0.28f;
			player.increaseRotation(0, -angleChange, 0);
			angleAroundPlayer = 0;
		}
	}

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
}