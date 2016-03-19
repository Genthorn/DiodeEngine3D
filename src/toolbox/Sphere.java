package toolbox;

import org.lwjgl.util.vector.Vector3f;

public class Sphere
{
	public float radius;
	public Vector3f center;

	public Sphere(Vector3f center, float radius) {
		this.center = center;
		this.radius = radius;
	}
}
