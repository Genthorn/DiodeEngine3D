package models;

import OBJLoader.ModelData;

public class RawModel {
	private int vaoID;
	private int vertexCount;
	private ModelData modelData;
	
	public RawModel(int vaoID, int vertexCount, ModelData modelData) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.modelData = modelData;
	}

	public ModelData getModelData() {
		return modelData;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
}
