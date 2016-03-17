package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import org.lwjgl.util.vector.Vector4f;

public class Maths {
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0,1), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	public static Vector3f crossProduct(Vector4f point1, Vector4f point2, Vector4f point3) {
		Vector3f normal = new Vector3f();

		normal.x = point1.x;
		normal.y = point1.y;
		normal.z = point1.z;

		normal.x = point1.x - point2.x;
		normal.y = point1.y - point2.y;
		normal.z = point1.z - point2.z;

		Vector3f toCrossProduct = new Vector3f();
		toCrossProduct.x = point2.x-point3.x;
		toCrossProduct.y = point2.y-point3.y;
		toCrossProduct.z = point2.z-point3.z;

		normal.x = normal.y * toCrossProduct.z - normal.z * toCrossProduct.y;
		normal.y = normal.z * toCrossProduct.x - normal.x * toCrossProduct.z;
		normal.z = normal.x * toCrossProduct.y - normal.y * toCrossProduct.x;

		normal.normalise(normal);

		return toCrossProduct;
	}

	public static void calculatePlaneNormals(Vector4f point0, Vector4f point1, Vector4f point2) {
		Vector3f v0 = new Vector3f();
		Vector3f v1 = new Vector3f();

		v0.x = point0.x;
		v0.y = point0.y;
		v0.z = point0.z;
		v0.x = point1.x - v0.x;
		v0.x = point1.y - v0.y;
		v0.x = point1.z - v0.z;

		v1.x = point1.x;
		v1.y = point1.y;
		v1.z = point1.z;
		v1.x = point1.x - v1.x;
		v1.x = point1.y - v1.y;
		v1.x = point1.z - v1.z;


	}

	public static float dotProduct(Vector3f vector1, Vector3f vector2) {
		return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
	}
}
