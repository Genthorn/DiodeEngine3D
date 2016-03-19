package toolbox;

import org.lwjgl.util.vector.Vector3f;

public class Plane {
	private Vector3f normal = new Vector3f();
	private float d = 0;

	public Plane(Vector3f normal, float d) {
		this.normal = normal;
		this.d = d;
	}

	public Plane() {}

	public void set(Vector3f point1, Vector3f point2, Vector3f point3) {
		Vector3f dir1 = new Vector3f();
		Vector3f dir2 = new Vector3f();
		Vector3f.sub(point1, point2, dir1);
		Vector3f.sub(point2, point3, dir2);
		Vector3f.cross(dir1, dir2, normal);
		normal.normalise();
		d = -Vector3f.dot(point1, normal);
	}

	public float getDistance() {
		return d;
	}

	public Vector3f getNormal() {
		return normal;
	}
}
