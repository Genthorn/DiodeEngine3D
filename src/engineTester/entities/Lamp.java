package engineTester.entities;

import entities.Entity;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import toolbox.AABB;
import entities.Light;

public class Lamp extends Entity {

    private Light light;

    public Lamp(Loader loader, Vector3f position, float scale) {
        super(new TexturedModel(OBJLoader.loadOBJModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp"))), position, 1, 1, 1, scale);
        this.model.getTexture().setUseFakeLighting(true);

        light = new Light(new Vector3f(this.position.x, this.position.y + 12, this.position.z),
                new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f));

        this.collisionBox = new AABB(this.position,
                new Vector3f(model.getRawModel().getSize().x, model.getRawModel().getSize().y,
                        model.getRawModel().getSize().z));


    }

    public Light getLight() {
        return light;
    }
}
