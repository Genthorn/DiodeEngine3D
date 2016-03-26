package particles;

public class ParticleTexture {
	private int textureID;
	private int numberOfRows;
	
	public ParticleTexture(int textureID, int numberOfRows) {
		this.textureID = textureID;
		this.numberOfRows = numberOfRows;
	}

	public ParticleTexture(int textureID) {
		this.textureID = textureID;
		this.numberOfRows = 1;
	}
	
	public int getTextureID() {
		return textureID;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

}
