package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

import toolbox.AABB;

public class Entity {
	protected TexturedModel model;
	protected Vector3f position = new Vector3f(0,0,0);
	protected float rotX, rotY, rotZ;
	protected float scale;
	
	private int textureIndex = 0;
	
	protected final float GRAVITY = -100;
	protected final float RUN_SPEED = 32;
	protected final float TURN_SPEED = 160;
	protected final float JUMP_POWER = 30;
	
	protected AABB collisionBox;
	
	public Entity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		
		this.collisionBox = new AABB(this.position, 
				new Vector3f(model.getRawModel().getSize().x, model.getRawModel().getSize().y, 
				model.getRawModel().getSize().z));
	}
	
	public Entity(TexturedModel model, int textureIndex, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		this.textureIndex = textureIndex;
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		
		this.collisionBox = new AABB(this.position, 
				new Vector3f(model.getRawModel().getSize().x, model.getRawModel().getSize().y, 
				model.getRawModel().getSize().z));
	}
	
	public float getTextureXOffset() {
		int column = textureIndex%model.getTexture().getNumberOfRows();
		return (float) column / (float) model.getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset() {
		int row = textureIndex/model.getTexture().getNumberOfRows();
		return (float) row / (float) model.getTexture().getNumberOfRows();
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	
	public TexturedModel getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
