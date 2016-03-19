package toolbox;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ViewFrustum {

	private static final Vector3f[] clipCube = { new Vector3f(-1, -1, -1), new Vector3f(1, -1, -1),
			new Vector3f(1, 1, -1), new Vector3f(-1, 1, -1), new Vector3f(-1, -1, 1),
			new Vector3f(1, -1, 1), new Vector3f(1, 1, 1), new Vector3f(-1, 1, 1) };

	private Plane[] planes = new Plane[6];

	public ViewFrustum() {
		for (int i = 0; i < 6; i++)
			planes[i] = new Plane();
	}

	public void update() {
		Vector3f[] points = calculateFrustumVertices();
		planes[0].set(points[1], points[0], points[2]);
		planes[1].set(points[4], points[5], points[7]);
		planes[2].set(points[0], points[4], points[3]);
		planes[3].set(points[5], points[1], points[6]);
		planes[4].set(points[2], points[3], points[6]);
		planes[5].set(points[4], points[0], points[1]);
	}

	private Vector3f[] calculateFrustumVertices() {
		Matrix4f inverseProjView = Matrix4f.mul(Camera.projectionMatrix, Camera.viewMatrix, null);
		inverseProjView.invert();
		Vector3f[] points = new Vector3f[8];
		Vector4f vertex = new Vector4f();
		vertex.w = 1;
		for (int i = 0; i < 8; i++) {
			vertex.x = clipCube[i].x;
			vertex.y = clipCube[i].y;
			vertex.z = clipCube[i].z;
			Matrix4f.transform(inverseProjView, vertex, vertex);
			vertex.x /= vertex.w;
			vertex.y /= vertex.w;
			vertex.z /= vertex.w;
			vertex.w /= vertex.w;
			points[i] = new Vector3f(vertex);
		}

		return points;
	}

	public boolean isSphereInside(Sphere sphere) {
		for (int i = 0; i < 6; i++) {
			float dot = (planes[i].getNormal().x * sphere.center.x + planes[i].getNormal().y *
					sphere.center.y + planes[i].getNormal().z * sphere.center.z);
			if (dot < (-sphere.radius - planes[i].getDistance())) {
				return false;
			}
		}

		return true;
	}

	public Plane[] getPlanes() {
		return planes;
	}
}
