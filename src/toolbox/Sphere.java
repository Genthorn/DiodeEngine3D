package toolbox;

import org.lwjgl.util.vector.Vector3f;

public class Sphere {
	private Vector3f center;
	private float radius;

	public Sphere(Vector3f center, float radius) {
		this.center = center;
		this.radius = radius;
	}

	public Vector3f getCenter() {
		return center;
	}

	public float getRadius() {
		return radius;
	}

	public void setCenter(Vector3f center) {
		this.center = center;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}
