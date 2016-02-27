package particles;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import entities.Entity;

public class Particle {
	
	//TO ANIMATE PARTICLES GO TO EPISODE 35 AND GO TO 11:29
	
	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotatation;
	private float scale;
	
	private ParticleTexture texture;
	
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
	
	protected boolean update() {
		velocity.y += Entity.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(change, position, position);
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
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
	
	
}
