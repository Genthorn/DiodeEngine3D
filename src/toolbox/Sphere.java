package toolbox;

import org.lwjgl.util.vector.Vector3f;

public class Sphere {
    private float radius;
    private Vector3f center;

    public Sphere(Vector3f center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public Vector3f getCenter() {
        return center;
    }

}
