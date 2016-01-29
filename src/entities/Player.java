package entities;

import java.util.List;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import terrains.Terrain;

public class Player extends Entity {

	private static final float RUN_SPEED = 32;
	private static final float TURN_SPEED = 160;
	private static final float JUMP_POWER = 30;

	private float currentForwardSpeed = 0;
	private float currentStrafeSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;

	private static final float TERRAIN_HEIGHT = 0;

	private boolean isInAir = false;
	
	private Terrain curTerrain = null;

	public Player(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move(List<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			if (terrain.getX() <= getPosition().x) {
				if (terrain.getX() + Terrain.getSIZE() > getPosition().x) {
					if (terrain.getZ() <= getPosition().z) {
						if (terrain.getZ() + Terrain.getSIZE() > getPosition().z) {
							checkInputs();
							super.increaseRotation(0, currentTurnSpeed
									* DisplayManager.getFrameTimeSeconds(), 0);
							float distance = currentForwardSpeed
									* DisplayManager.getFrameTimeSeconds();
							float dx = 0;
							float dz = 0;
							dx += (float) (distance * Math.sin(Math
									.toRadians(super.getRotY())));
							dz += (float) (distance * Math.cos(Math
									.toRadians(super.getRotY())));

							// dx += (float)
							// (distance*Math.cos(Math.toRadians(super.getRotY()+90)));
							// dz += (float)
							// (-distance*Math.sin(Math.toRadians(super.getRotY()+90)));

							super.increasePosition(dx, 0, dz);

							upwardsSpeed += super.GRAVITY
									* DisplayManager.getFrameTimeSeconds();
							super.increasePosition(0, upwardsSpeed
									* DisplayManager.getFrameTimeSeconds(), 0);
							float terrainHeight = terrain.getHeightOfTerrain(
									super.getPosition().x,
									super.getPosition().z);
							if (super.getPosition().y < terrainHeight) {
								upwardsSpeed = 0;
								isInAir = false;
								super.getPosition().y = terrainHeight;
							}
							
							curTerrain = terrain;

						}
					}
				}
			}
		}
	}

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentForwardSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentForwardSpeed = -RUN_SPEED;
		} else {
			this.currentForwardSpeed = 0;
		}

		// STRAFING CODE HERE
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if (!Mouse.isButtonDown(1))
				this.currentTurnSpeed = -TURN_SPEED;
			if (Mouse.isButtonDown(1))
				this.currentStrafeSpeed = -RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if (!Mouse.isButtonDown(1))
				this.currentTurnSpeed = TURN_SPEED;
			if (Mouse.isButtonDown(1))
				this.currentStrafeSpeed = RUN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
			this.currentStrafeSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			jump();
	}

	public boolean isMoving() {
		if (currentForwardSpeed != 0)
			return true;
		return false;
	}
	
	public Terrain getCurrentTerrain() {
		return curTerrain;
	}

}
