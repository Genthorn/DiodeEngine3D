package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Player;
import toolbox.ViewFrustum;

public class Camera {

    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.7f;
    public static final float FAR_PLANE = 900.0f;

    public static Matrix4f rotationMatrix = new Matrix4f();
    public static Matrix4f viewMatrix = new Matrix4f();
    public static Matrix4f projectionMatrix = new Matrix4f();
    public static ViewFrustum viewFrustum = new ViewFrustum();
    public static float aspectRatio;

    private float distanceFromPlayer = 20;

    public static Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 20;
    private float yaw = 0;
    private boolean hasRunOnce = false;

    public Camera() {
        updateProjection();
    }

    public void update() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();

        float theta = Player.LocalPlayer.getRotY() + 180;
        theta += yaw;
        rotationMatrix.setIdentity();
        rotationMatrix.rotate((float) Math.toRadians(theta), new Vector3f(0, 1, 0));
        rotationMatrix.rotate((float) Math.toRadians(-pitch), new Vector3f(1, 0, 0));

        Vector4f newPosition = new Vector4f(0, 0, distanceFromPlayer, 1);
        Matrix4f.transform(rotationMatrix, newPosition, newPosition);
        position = new Vector3f(newPosition);
        Vector3f.add(position, Player.LocalPlayer.position, position);

        viewMatrix.setIdentity();
        viewMatrix.translate(position);
        Matrix4f.mul(viewMatrix, rotationMatrix, viewMatrix);
        viewMatrix.invert();

        viewFrustum.update();
        hasRunOnce = true;
    }

    private void updateProjection() {
        aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float nearHeight = 1.0f / (float) Math.tan(Math.toRadians(Camera.FOV / 2.0f));
        float frustum_length = NEAR_PLANE - FAR_PLANE;

        projectionMatrix.m00 = nearHeight / aspectRatio;
        projectionMatrix.m11 = nearHeight;
        projectionMatrix.m22 = (FAR_PLANE + NEAR_PLANE) / frustum_length;
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = (2.0f * NEAR_PLANE * FAR_PLANE) / frustum_length;
        projectionMatrix.m33 = 0;
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
    }

    private void calculatePitch() {
        if (hasRunOnce) {
            if (Mouse.isButtonDown(0)) {
                float pitchChange = Mouse.getDY() * 0.28f;
                pitch -= pitchChange;
            }

            if (Mouse.isButtonDown(1)) {
                float pitchChange = Mouse.getDY() * 0.28f;
                pitch -= pitchChange;
            }
        }

    }

    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.28f;
            yaw -= angleChange;
        }

        if (Mouse.isButtonDown(1)) {
            float angleChange = Mouse.getDX() * 0.28f;
            Player.LocalPlayer.increaseRotation(0, -angleChange, 0);
            yaw = 0;
        }
    }

    public void invertPitch() {
        this.pitch = -pitch;
    }

    public void setPosition(Vector3f newPosition) {
        position.set(newPosition);
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}