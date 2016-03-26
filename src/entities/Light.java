package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	private Vector3f position;
	private Vector3f colour;
	private Vector3f attenuation = new Vector3f(1,0,0);
	private boolean hasChanged = false;
	
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}

	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public Vector3f getAttenuation() {
		return attenuation;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChangedFalse() {
		hasChanged = false;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
		hasChanged = true;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
		hasChanged = true;
	}

}
