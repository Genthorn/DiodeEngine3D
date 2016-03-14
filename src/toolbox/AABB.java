package toolbox;

import OBJLoader.ModelData;
import org.lwjgl.util.vector.Vector3f;

public class AABB {

    private Vector3f position;
    private ModelData modelData;
    private Vector3f size;

    public AABB(ModelData modelData, Vector3f position) {
        this.position = position;
        this.modelData = modelData;
        calculateAABB();
    }

    private void calculateAABB() {
        float size = modelData.getFurthestPoint();
        position = new Vector3f(position.x, position.y + size / 2, position.z);
        this.size = new Vector3f(size,size,size);
    }

    public static boolean isColliding(AABB a, AABB b) {
        if(Math.abs(a.position.x - b.position.x) < a.size.x + b.size.x) {
            if(Math.abs(a.position.y - b.position.y) < a.size.y + b.size.y) {
                if(Math.abs(a.position.z - b.position.z) < a.size.z + b.size.z) {
                    return true;
                }
            }
        }

        return false;
    }

}
