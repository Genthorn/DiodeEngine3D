package particles;

import entities.Camera;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import entities.Entity;
import terrains.World;

public class Particle {
	
	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotatation;
	private float scale;
	
	private ParticleTexture texture;

	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend;

	private float distanceFromCamera = 0;
	private float elapsedTime = 0;

	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect,
			float lifeLength, float rotatation, float scale) {
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotatation = rotatation;
		this.scale = scale;
		this.texture = texture;
		
		ParticleMaster.addParticle(this);
	}
	
	protected boolean update(Camera camera) {
		velocity.y += World.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(change, position, position);
		distanceFromCamera = Vector3f.sub(Camera.position, position, null).lengthSquared();
		updateTextureCoordInfo();
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
	}

	private void updateTextureCoordInfo() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgress = lifeFactor * stageCount;
		int index1 = (int)Math.floor(atlasProgress);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgress % 1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	}

	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float) column / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}
	
	public ParticleTexture getTexture() {
		return texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotatation() {
		return rotatation;
	}

	public float getScale() {
		return scale;
	}

	public Vector2f getTexOffset1() {
		return texOffset1;
	}

	public Vector2f getTexOffset2() {
		return texOffset2;
	}

	public float getBlend() {
		return blend;
	}

	public float getDistanceFromCamera() {
		return distanceFromCamera;
	}
}
