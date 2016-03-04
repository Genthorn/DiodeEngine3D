package engineTester.entities;

import entities.Entity;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import entities.Light;

public class Lamp extends Entity {

    private Light light;

    public Lamp(Loader loader, Vector3f position, float scale) {
        super(new TexturedModel(OBJLoader.loadOBJModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp"))), position, 1, 1, 1, scale);
        this.model.getTexture().setUseFakeLighting(true);
        this.model.getTexture().setHasTransparency(true);

        light = new Light(new Vector3f(this.position.x, this.position.y + 12, this.position.z),
                new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f));

    }

    public Light getLight() {
        return light;
    }
}
