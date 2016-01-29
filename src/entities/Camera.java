package entities;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import terrains.Terrain;

public class Camera {
	
	private float MIN_HEIGHT_ABOVE_TERRAIN = 2;

	private float distanceFromPlayer = 20;
	private float angleAroundPlayer = 0;

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 20;
	private float yaw = 0;
	private float roll;

	private Player player;

	public Camera(Player player) {
		this.player = player;
	}

	public void move() {
		//preventCameraTerrainCollision();
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		
	}
	
	private void preventCameraTerrainCollision() {
		float terrainHeightAtCurPos = player.getCurrentTerrain().getHeightOfTerrain(position.x, position.z);
		if(position.y < terrainHeightAtCurPos +MIN_HEIGHT_ABOVE_TERRAIN) {
			position.y = (terrainHeightAtCurPos + MIN_HEIGHT_ABOVE_TERRAIN);
			//this.onGround = true;          
		} else if (position.y > terrainHeightAtCurPos + MIN_HEIGHT_ABOVE_TERRAIN) {
			//this.onGround = false;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
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

	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}

	private void calculatePitch() {
		if (Mouse.isButtonDown(0)) {
			float pitchChange = Mouse.getDY() * 0.28f;
			pitch -= pitchChange;
		}
		
		if (Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.28f;
			pitch -= pitchChange;
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
	
	public Player getPlayer() {
		return player;
	}

}