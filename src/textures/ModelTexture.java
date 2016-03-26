package textures;

public class ModelTexture {
	private int textureID;
	private int normalMap;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	private int numberOfRows = 1;

	public ModelTexture(int id) {
		this.textureID = id;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public int getNormalMap() {
		return normalMap;
	}

	public int getTextureID() {
		return textureID;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public void setTextureID(int textureID) {
		this.textureID = textureID;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
	}
}
