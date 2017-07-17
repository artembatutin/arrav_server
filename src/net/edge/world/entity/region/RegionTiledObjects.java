package net.edge.world.entity.region;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.world.object.ObjectNode;
import net.edge.world.object.ObjectType;

import java.util.function.Consumer;

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
	
	public ObjectList<ObjectNode> getDynamic() {
		ObjectList<ObjectNode> list = new ObjectArrayList<>();
		for(ObjectNode o : objects) {
			if(o != null && o.isDynamic())
				list.add(o);
		}
		return list;
	}
	
	public ObjectList<ObjectNode> getStatic() {
		ObjectList<ObjectNode> list = new ObjectArrayList<>();
		for(ObjectNode o : objects) {
			if(o != null && !o.isDynamic())
				list.add(o);
		}
		return list;
	}
	
	public ObjectList<ObjectNode> getInteract() {
		ObjectList<ObjectNode> list = new ObjectArrayList<>();
		for(ObjectNode o : objects) {
			if(o != null && (o.getObjectType().getGroup() == INTERACTABLE_OBJECT || o.getObjectType().getGroup() == WALL))
				list.add(o);
		}
		return list;
	}
	
	public void dynamicAction(Consumer<ObjectNode> action) {
		for(ObjectNode o : objects) {
			if(o != null && o.isDynamic())
				action.accept(o);
		}
	}
	
	public void interactiveAction(int id, Consumer<ObjectNode> action) {
		for(ObjectNode o : objects) {
			if(o != null && (o.getId() == id && o.getObjectType().getGroup() == INTERACTABLE_OBJECT || o.getObjectType().getGroup() == WALL))
				action.accept(o);
		}
	}
	
	public ObjectList<ObjectNode> getAll() {
		ObjectList<ObjectNode> list = new ObjectArrayList<>();
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
	
	public ObjectNode getType(ObjectType type) {
		return objects[type.getId()];
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
