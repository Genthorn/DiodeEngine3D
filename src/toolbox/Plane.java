package toolbox;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Plane {

    private Vector3f normal;
    private float distance = 0;

    public Plane (Vector4f point1, Vector4f point2, Vector4f point3) {
        set(point1, point2, point3);
        normal = new Vector3f();
    }

    public void set (Vector4f point1, Vector4f point2, Vector4f point3) {
        normal = Maths.crossProduct(point1, point2, point3);
        distance = -Maths.dotProduct(new Vector3f(point1.x, point1.y, point1.z), normal);
    }

    public boolean testPoint (float x, float y, float z) {
        float dist = Maths.dotProduct(normal, new Vector3f(x, y, z)) + distance;

        if (dist == 0)
            return true;
        else if (dist < 0)
            return true;
        else
            return false;
    }

    public float getDistance() {
        return distance;
    }

    public Vector3f getNormal() {
        return normal;
    }
}
