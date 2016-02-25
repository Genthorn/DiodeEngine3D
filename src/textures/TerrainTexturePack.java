package textures;

import renderEngine.Loader;

public class TerrainTexturePack {
	
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	
	public TerrainTexturePack(Loader loader, String backgroundTexture, String rTexture, String gTexture, 
			String bTexture) {
		this.backgroundTexture = new TerrainTexture(loader.loadTexture(backgroundTexture));
		this.rTexture = new TerrainTexture(loader.loadTexture(rTexture));
		this.gTexture = new TerrainTexture(loader.loadTexture(gTexture));
		this.bTexture = new TerrainTexture(loader.loadTexture(bTexture));
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
