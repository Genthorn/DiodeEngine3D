package toolbox;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ViewFrustum
{
	private static final Vector3f[] clipCube = { new Vector3f(-1, -1, -1), new Vector3f(1, -1, -1), new Vector3f(1, 1, -1), new Vector3f(-1, 1, -1), new Vector3f(-1, -1, 1), new Vector3f(1, -1, 1),
			new Vector3f(1, 1, 1), new Vector3f(-1, 1, 1) };
	// private static final Vector4f UP = new Vector4f(0, 1, 0, 0);
	// private static final Vector4f FORWARD = new Vector4f(0, 0, -1, 0);
	private Plane[] planes = new Plane[6];

	public ViewFrustum()
	{
		for (int i = 0; i < 6; i++)
			planes[i] = new Plane();
	}

	public void update()
	{
		Vector3f[] points = calculateFrustumVertices();
		planes[0].Set(points[1], points[0], points[2]);
		planes[1].Set(points[4], points[5], points[7]);
		planes[2].Set(points[0], points[4], points[3]);
		planes[3].Set(points[5], points[1], points[6]);
		planes[4].Set(points[2], points[3], points[6]);
		planes[5].Set(points[4], points[0], points[1]);

		// planes[0].Set(points[4], points[5], points[7]); // front plane
		// planes[1].Set(points[0], points[4], points[6]); // right plane
		// planes[2].Set(points[1], points[0], points[2]); // back plane
		// planes[3].Set(points[5], points[1], points[3]); // left plane
		// planes[4].Set(points[0], points[1], points[5]); // top plane
		// planes[5].Set(points[6], points[7], points[3]); // bottom plane
	}
	private Vector3f[] calculateFrustumVertices()
	{
		Matrix4f inverseProjView = Matrix4f.mul(Camera.projectionMatrix, Camera.viewMatrix, null);
		inverseProjView.invert();
		Vector3f[] points = new Vector3f[8];
		Vector4f vertex = new Vector4f();
		vertex.w = 1;
		for (int i = 0; i < 8; i++)
		{
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
	// private Vector3f calculateFrustumCorner(Vector3f startPoint, Vector3f direction, float width)
	// {
	// return Vector3f.add(startPoint, new Vector3f(direction.x * width, direction.y * width, direction.z * width), null);
	// }
	public boolean Contains(Sphere sphere)
	{
		for (int i = 0; i < 6; i++)
		{
			float dot = (planes[i].Normal.x * sphere.center.x + planes[i].Normal.y * sphere.center.y + planes[i].Normal.z * sphere.center.z);
			if (dot < (-sphere.radius - planes[i].D))
			// distance = (planes[i].Normal.x * sphere.center.x + planes[i].Normal.y * sphere.center.y + planes[i].Normal.z * sphere.center.z);
			// distance -= sphere.radius;
			// distance -= planes[i].D;
			// if (distance < 0)
			{
				return false;
			}
		}
		System.out.println(" ");
		return true;
	}
	// public boolean isPointInside(Vector3f pos) {
	// return isPointInside(pos.x, pos.y, pos.z);
	// }
	//
	// public boolean isPointInside(float x, float y, float z) {
	// for (int i = 0; i < 6; i++) {
	// boolean result = planes[i].testPoint(x, y, z);
	// if (result == false) return false;
	// }
	//
	// return true;
	// }
}
