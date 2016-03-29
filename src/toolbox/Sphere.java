package toolbox;

import org.lwjgl.util.vector.Vector3f;

public class Sphere {
	private Vector3f center;
	private float radius;

	public Sphere(Vector3f center, float radius) {
		this.center = center;
		this.radius = radius;
	}

	public boolean isCollidingWithSphere(Sphere sphere) {
		float diffX = (sphere.getCenter().x - center.x);
		float diffY = (sphere.getCenter().y - center.y);
		float diffZ = (sphere.getCenter().z - center.z);

		float diffXSquared = (float) Math.pow(diffX, 2);
		float diffYSquared = (float) Math.pow(diffY, 2);
		float diffZSquared = (float) Math.pow(diffZ, 2);

		float radiusSums = (sphere.radius + radius);
		float radiusSumsSquared = (float)Math.pow(radiusSums, 2);

		if (diffXSquared + diffYSquared + diffZSquared > radiusSumsSquared){
			return false;
		}

		return true;
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
