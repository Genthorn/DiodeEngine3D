package toolbox;

import OBJLoader.ModelData;
import org.lwjgl.util.vector.Vector3f;

public class AABB {

    private Vector3f position;
    private float width, height;

    public AABB(ModelData boundingBox, Vector3f position) {
        this.position = position;
        calculateAABB();
    }

    private void calculateAABB() {

    }

    public static boolean isColliding(AABB a, AABB b) {
        if(Math.abs(a.position.x - b.position.x) < a.width + b.width) {
            if(Math.abs(a.position.y - b.position.y) < a.height + b.height) {
                if(Math.abs(a.position.z - b.position.z) < a.height + b.height) {
                    return true;
                }
            }
        }

        return false;
    }

}
