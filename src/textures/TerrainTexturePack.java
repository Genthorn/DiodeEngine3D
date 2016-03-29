package textures;

import renderEngine.Loader;

public class TerrainTexturePack {
	
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	
	public TerrainTexturePack(String backgroundTexture, String rTexture, String gTexture,
			String bTexture) {
		this.backgroundTexture = new TerrainTexture(Loader.loadTexture(backgroundTexture));
		this.rTexture = new TerrainTexture(Loader.loadTexture(rTexture));
		this.gTexture = new TerrainTexture(Loader.loadTexture(gTexture));
		this.bTexture = new TerrainTexture(Loader.loadTexture(bTexture));
	}

	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}

	public TerrainTexture getrTexture() {
		return rTexture;
	}

	public TerrainTexture getgTexture() {
		return gTexture;
	}

	public TerrainTexture getbTexture() {
		return bTexture;
	}
	
	
	
}
