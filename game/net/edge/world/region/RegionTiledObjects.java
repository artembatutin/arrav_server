package net.edge.world.region;

import net.edge.world.object.ObjectNode;

import java.util.ArrayList;
import java.util.List;

import static net.edge.world.object.ObjectGroup.INTERACTABLE_OBJECT;
import static net.edge.world.object.ObjectGroup.WALL;

/**
 * A class acting as a container for objects on a single tile.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class RegionTiledObjects {
	
	private final ObjectNode[] objects = new ObjectNode[23];
	
	public RegionTiledObjects() {
		reset();
	}
	
	public void add(ObjectNode o) {
		int type = o.getObjectType().getId();
		if(objects[type] != null)
			objects[type].remove();
		objects[type] = o;
	}
	
	public void remove(ObjectNode o) {
		int type = o.getObjectType().getId();
		if(objects[type] != null && objects[type].getId() == o.getId()) {
			objects[type] = null;
		}
	}
	
	public List<ObjectNode> getDynamic() {
		List<ObjectNode> list = new ArrayList<>();
		for(ObjectNode o : objects) {
			if(o != null && o.isDynamic())
				list.add(o);
		}
		return list;
	}
	
	public List<ObjectNode> getStatic() {
		List<ObjectNode> list = new ArrayList<>();
		for(ObjectNode o : objects) {
			if(o != null && !o.isDynamic())
				list.add(o);
		}
		return list;
	}
	
	public List<ObjectNode> getInteract() {
		List<ObjectNode> list = new ArrayList<>();
		for(ObjectNode o : objects) {
			if(o != null && (o.getObjectType().getGroup() == INTERACTABLE_OBJECT || o.getObjectType().getGroup() == WALL))
				list.add(o);
		}
		return list;
	}
	
	public List<ObjectNode> getAll() {
		List<ObjectNode> list = new ArrayList<>();
		for(ObjectNode o : objects) {
			if(o != null)
				list.add(o);
		}
		return list;
	}
	
	public ObjectNode getId(int id) {
		for(ObjectNode o : objects) {
			if(o == null)
				continue;
			if(o.getId() == id)
				return o;
		}
		return null;
	}
	
	public boolean hasInteractive() {
		for(ObjectNode o : objects) {
			if(o != null && (o.getObjectType().getGroup() == INTERACTABLE_OBJECT || o.getObjectType().getGroup() == WALL))
				return true;
		}
		return false;
	}
	
	public ObjectNode getType(int type) {
		return objects[type];
	}
	
	public void reset() {
		for(int i = 0; i < objects.length; i++)
			objects[i] = null;
	}
	
}
