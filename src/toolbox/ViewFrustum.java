package toolbox;

import entities.Camera;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ViewFrustum {

    private Camera camera;

    private static final float OFFSET = 10;
    private static final Vector4f UP = new Vector4f(0, 1, 0, 0);
    private static final Vector4f FORWARD = new Vector4f(0, 0, -1, 0);

    private Matrix4f viewMatrix;

    private float minX, maxX;
    private float minY, maxY;
    private float minZ, maxZ;

    private float farHeight, farWidth, nearHeight, nearWidth;

    public ViewFrustum(Camera camera) {
        this.camera = camera;
        viewMatrix = Maths.createViewMatrix(camera);
        calculateWidthsAndHeights();
    }

    public void update() {
        Matrix4f rotation = calculateCameraRotationMatrix();
        Vector3f forwardVector = new Vector3f(Matrix4f.transform(rotation, FORWARD, null));
        Vector3f upVector = new Vector3f(Matrix4f.transform(rotation, UP, null));

        Vector3f toFar = new Vector3f(forwardVector);
        toFar.scale(camera.FAR_PLANE);
        Vector3f toNear = new Vector3f(forwardVector);
        toNear.scale(camera.NEAR_PLANE);
        Vector3f centerNear = Vector3f.add(toNear, camera.getPosition(), null);
        Vector3f centerFar = Vector3f.add(toFar, camera.getPosition(), null);

        Vector4f[] points = calculateFrustumVertices(rotation, forwardVector, centerNear,
                centerFar);

        //Vector4f[] points = calculateFrustumVertices(rotation, forwardVector, toNear,
                //toFar);

        boolean first = true;
        for (Vector4f point : points) {
            if (first) {
                minX = point.x;
                maxX = point.x;
                minY = point.y;
                maxY = point.y;
                minZ = point.z;
                maxZ = point.z;
                first = false;
                continue;
            }
            if (point.x > maxX) {
                maxX = point.x;
            } else if (point.x < minX) {
                minX = point.x;
            }
            if (point.y > maxY) {
                maxY = point.y;
            } else if (point.y < minY) {
                minY = point.y;
            }
            if (point.z > maxZ) {
                maxZ = point.z;
            } else if (point.z < minZ) {
                minZ = point.z;
            }
        }
        maxZ -= OFFSET;

     }

    private Vector4f[] calculateFrustumVertices(Matrix4f rotation, Vector3f forwardVector,
                                                Vector3f centerNear, Vector3f centerFar) {
        Vector3f upVector = new Vector3f(Matrix4f.transform(rotation, UP, null));
        Vector3f rightVector = Vector3f.cross(forwardVector, upVector, null);
        Vector3f downVector = new Vector3f(-upVector.x, -upVector.y, -upVector.z);
        Vector3f leftVector = new Vector3f(-rightVector.x, -rightVector.y, -rightVector.z);
        Vector3f farTop = Vector3f.add(centerFar, new Vector3f(upVector.x * farHeight,
                upVector.y * farHeight, upVector.z * farHeight), null);
        Vector3f farBottom = Vector3f.add(centerFar, new Vector3f(downVector.x * farHeight,
                downVector.y * farHeight, downVector.z * farHeight), null);
        Vector3f nearTop = Vector3f.add(centerNear, new Vector3f(upVector.x * nearHeight,
                upVector.y * nearHeight, upVector.z * nearHeight), null);
        Vector3f nearBottom = Vector3f.add(centerNear, new Vector3f(downVector.x * nearHeight,
                downVector.y * nearHeight, downVector.z * nearHeight), null);

        Vector4f[] points = new Vector4f[8];
        points[0] = calculateFrustumCorner(farTop, rightVector, farWidth);
        points[1] = calculateFrustumCorner(farTop, leftVector, farWidth);
        points[2] = calculateFrustumCorner(farBottom, rightVector, farWidth);
        points[3] = calculateFrustumCorner(farBottom, leftVector, farWidth);
        points[4] = calculateFrustumCorner(nearTop, rightVector, nearWidth);
        points[5] = calculateFrustumCorner(nearTop, leftVector, nearWidth);
        points[6] = calculateFrustumCorner(nearBottom, rightVector, nearWidth);
        points[7] = calculateFrustumCorner(nearBottom, leftVector, nearWidth);

        return points;
    }

    private Vector4f calculateFrustumCorner(Vector3f startPoint, Vector3f direction, float width) {
        Vector3f point = Vector3f.add(startPoint, new Vector3f(direction.x * width, direction.y * width, direction.z * width), null);
        Vector4f point4f = new Vector4f(point.x, point.y, point.z, 1f);
        //Matrix4f.transform(viewMatrix, point4f, point4f);
        return point4f;
    }

    public Vector3f getCenter() {
        float x = (minX + maxX) / 2f;
        float y = (minY + maxY) / 2f;
        float z = (minZ + maxZ) / 2f;
        Vector4f cen = new Vector4f(x, y, z, 1);
        Matrix4f invertedLight = new Matrix4f();
        Matrix4f.invert(viewMatrix, invertedLight);
        return new Vector3f(Matrix4f.transform(invertedLight, cen, null));
    }

    private Matrix4f calculateCameraRotationMatrix() {
        Matrix4f rotation = new Matrix4f();
        rotation.rotate((float) Math.toRadians(-camera.getYaw()), new Vector3f(0, 1, 0));
        rotation.rotate((float) Math.toRadians(-camera.getPitch()), new Vector3f(1, 0, 0));
        return rotation;
    }

    private void calculateWidthsAndHeights() {
        farWidth = (float) (camera.FAR_PLANE * Math.tan(Math.toRadians(camera.FOV)));
        nearWidth = (float) (camera.NEAR_PLANE
                * Math.tan(Math.toRadians(camera.FOV)));
        farHeight = farWidth / getAspectRatio();
        nearHeight = nearWidth / getAspectRatio();
    }

    private float getAspectRatio() {
        return (float) Display.getWidth() / (float) Display.getHeight();
    }

    public float getDistance() {
        return camera.FAR_PLANE;
    }

    public float getWidth() {
        return maxX - minX;
    }

    public float getHeight() {
        return maxY - minY;
    }

    public float getLength() {
        return maxZ - minZ;
    }

    public Vector3f getMinPositions() {
        return new Vector3f(minX, minY, minZ);
    }

    public Vector3f getMaxPositions() {
        return new Vector3f(maxX, maxY, maxZ);
    }


}
