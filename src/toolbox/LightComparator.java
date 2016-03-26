package toolbox;

import java.util.Comparator;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.Light;

public class LightComparator implements Comparator<Light> {

	private Entity entity;

	public LightComparator(Entity entity) {
		this.entity = entity;     
	}          
	
	@Override     
	public int compare(Light o1, Light o2) {
		if(o1.getAttenuation().equals(new Vector3f(1,0,0))) {
			return -1;         
		}         
		
		if(o2.getAttenuation().equals(new Vector3f(1,0,0))) {
			return 1;         
		}                  
		
		Vector3f delta1 = Vector3f.sub(o1.getPosition(), entity.getPosition(), null);
		Vector3f delta2 = Vector3f.sub(o2.getPosition(), entity.getPosition(), null);

		if(delta1.lengthSquared() < delta2.lengthSquared()) {
			return -1;         
		} else if(delta1.lengthSquared() > delta2.lengthSquared()) {
			return 1;         
		}   
		
		return 0;

		} 
	}
