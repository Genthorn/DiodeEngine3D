package toolbox;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Plane {

    private Vector3f normal;
    //private float distance = 0;
    private Vector4f[] points;

    public Plane (Vector4f point1, Vector4f point2, Vector4f point3) {
        normal = new Vector3f();
        set(point1, point2, point3);
    }

    public void set (Vector4f point1, Vector4f point2, Vector4f point3) {
        points = new Vector4f[3];
        points[0] = point1;
        points[1] = point2;
        points[2] = point3;
    	normal = Maths.calculateNormal(point1, point2, point3);
        //distance = -Maths.dotProduct(new Vector3f(point1.x, point1.y, point1.z), normal);
    }

//    public boolean testPoint (float x, float y, float z) {
//        float dist = Maths.dotProduct(normal, new Vector3f(x, y, z)) + distance;
//
//        if (dist == 0)
//            return false;
//        else if (dist < 0)
//            return false;
//        else
//            return true;
//    }

    public Vector4f[] getPoints() {
        return points;
    }

//    public float getDistance() {
//        return distance;
//    }

    public Vector3f getNormal() {
        return normal;
    }
}
