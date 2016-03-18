package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import toolbox.ViewFrustum;

public class Camera {
	
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.7f;
	public static final float FAR_PLANE = 900.0f;
	
	private float MIN_HEIGHT_ABOVE_TERRAIN = 2;

	private float distanceFromPlayer = 20;
	//private float angleAroundPlayer = 0;

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 0;
	private float yaw = 0;
	private float roll = 0;

	public Matrix4f rotation = new Matrix4f();
	public Matrix4f viewMatrix = new Matrix4f();
	
	private Player player;
	
	public ViewFrustum viewFrustum;
	
	private boolean hasRunOnce = false;
	private boolean onGround = false;
	
	public Camera(Player player) {
		this.player = player;
		viewFrustum = new ViewFrustum(this);
	}

	public void move() {
		calculateZoom();
		if(hasRunOnce) calculatePitch();
		calculateAngleAroundPlayer();
		
    	float theta = player.getRotY() + 180;
    	theta += yaw;
        rotation.setIdentity();
        rotation.rotate((float) Math.toRadians(theta), new Vector3f(0, 1, 0));
        rotation.rotate((float) Math.toRadians(-pitch), new Vector3f(1, 0, 0));

        Vector4f newPosition = new Vector4f(0, 0, distanceFromPlayer, 1);
        Matrix4f.transform(rotation, newPosition, newPosition);
        position = new Vector3f(newPosition);
        
        viewMatrix.setIdentity();
        viewMatrix.translate(position);
        Matrix4f.mul(viewMatrix, rotation, viewMatrix);
        viewMatrix.invert();
        
        viewFrustum.update();
		hasRunOnce = true;
	}
	
	private void preventCameraTerrainCollision() {
		float terrainHeightAtCurPos = player.getCurrentTerrain().getHeightOfTerrain(position.x, position.z);
		if(position.y < terrainHeightAtCurPos + MIN_HEIGHT_ABOVE_TERRAIN) {
			position.y = (terrainHeightAtCurPos + MIN_HEIGHT_ABOVE_TERRAIN);
			this.onGround = true;
		} else if (position.y > terrainHeightAtCurPos + MIN_HEIGHT_ABOVE_TERRAIN) {
			this.onGround = false;
		}
	}
	
	public void invertPitch() {
		this.pitch = -pitch;
	}
	
	public void setPosition(Vector3f position) {
		this.position.set(position);
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public void setRoll(float roll) {
		this.roll = roll;
	}

	public Vector3f getPosition() {
		return position;
	}

	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}

	private void calculatePitch() {
//		float terrainHeightAtCurPos = 
//				player.getCurrentTerrain().getHeightOfTerrain(position.x, position.z) 
//				+ MIN_HEIGHT_ABOVE_TERRAIN;
		
		if (Mouse.isButtonDown(0)) {
			float pitchChange = Mouse.getDY() * 0.28f;
			//if(!onGround)
				pitch -= pitchChange;
			//else pitch = terrainHeightAtCurPos;
		}
		
		if (Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.28f;
			//if(!onGround) 
				pitch -= pitchChange;
			//else pitch += terrainHeightAtCurPos;
		}
		
	}
	
	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.28f;
			yaw -= angleChange;
		}
		
		if (Mouse.isButtonDown(1)) {
			float angleChange = Mouse.getDX() * 0.28f;
			player.increaseRotation(0, -angleChange, 0);
			yaw = 0;
		}
	}
	
	public Player getPlayer() {
		return player;
	}

}