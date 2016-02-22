package models;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class RawModel {
	private int vaoID;
	private int vertexCount;
	private Vector3f size;
	private List<Vector3f> vertices = new ArrayList<Vector3f>(); 
	
	public RawModel(int vaoID, int vertexCount, Vector3f size) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.size = size;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public void setVertices(List<Vector3f> vertices) {
		this.vertices = vertices;
	}
	
	public List<Vector3f> getVertices() {
		return vertices;
	}
	
	public void setSize(Vector3f size) {
		this.size = size;
	}
	
	public Vector3f getSize() {
		return size;
	}
}
