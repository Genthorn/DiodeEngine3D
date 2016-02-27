package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class Mobs extends Entity {

	public Mobs(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
}
