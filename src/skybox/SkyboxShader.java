package skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;

public class SkyboxShader extends ShaderProgram {
	private static final String VERTEX_FILE = "skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "skybox/skyboxFragmentShader.txt";
    
    private static final float ROTATE_SPEED = 1f;
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColour;
    
    private float rotation = 0;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(){
        Matrix4f matrix = Camera.viewMatrix;
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
        super.loadMatrix(location_viewMatrix, matrix);
    }
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColour = super.getUniformLocation("fogColour");
    }
    
    public void loadFogColour(float r, float g, float b) {
    	super.loadVector3f(location_fogColour, new Vector3f(r, g, b));
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
