package toolbox;

import org.lwjgl.util.vector.Vector3f;

public class Plane
{
	public Vector3f Normal = new Vector3f();
	public float D = 0;

	public Plane()
	{
	}
	public Plane(Vector3f normal, float d)
	{
		Normal = normal;
		D = d;
	}

	public void Set(Vector3f point1, Vector3f point2, Vector3f point3)
	{
		Vector3f dir1 = new Vector3f();
		Vector3f dir2 = new Vector3f();
		Vector3f.sub(point1, point2, dir1);
		Vector3f.sub(point2, point3, dir2);
		Vector3f.cross(dir1, dir2, Normal);
		Normal.normalise();
		D = -Vector3f.dot(point1, Normal);
	}
}
