package toolbox;

import org.lwjgl.util.vector.Vector3f;

public class AABB {
	protected Vector3f pos, size;

	public AABB(Vector3f pos, Vector3f size) {
		this.pos = pos;
		this.size = size;
	}

	public static boolean collides(AABB a, AABB b) {
		/*if (Math.abs(a.pos.x - b.pos.x) <= a.size.x + b.size.x) {
			if (Math.abs(a.pos.y - b.pos.y) <= a.size.y + b.size.y) {
				System.out.println(a.size.z + b.size.z);
				if (Math.abs(a.pos.z - b.pos.z) <= a.size.z + b.size.z) {
					return true;
				}
			}
		}*/
		
		return (Math.abs(a.pos.x - b.pos.x) < (a.size.x + b.size.x)) &&
			   (Math.abs(a.pos.z - b.pos.z) < (a.size.z + b.size.z)) &&
			   (Math.abs(a.pos.y - b.pos.y) < (a.size.y + b.size.y));
			

		//return false;
	}
	
	public static boolean inside(AABB a, Vector3f b) {
		if (Math.abs(a.pos.x - b.x) < a.size.x) {
			if (Math.abs(a.pos.y - b.y) < a.size.y) {
				if (Math.abs(a.pos.z - b.z) < a.size.z) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Vector3f getPos() {
		return pos;
	}
	
	public Vector3f getSize() {
		return size;
	}
}
