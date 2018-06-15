package net.arrav.world.entity.region;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.world.entity.object.GameObject;
import net.arrav.world.entity.object.ObjectType;

import java.util.function.Consumer;

import static net.arrav.world.entity.object.ObjectGroup.INTERACTABLE_OBJECT;
import static net.arrav.world.entity.object.ObjectGroup.WALL;

/**
 * A class acting as a container for objects on a single tile.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class RegionTiledObjects {
	
	private final GameObject[] objects = new GameObject[23];
	
	public RegionTiledObjects() {
		reset();
	}
	
	public void add(GameObject o) {
		int type = o.getObjectType().getId();
		if(objects[type] != null)
			objects[type].remove();
		objects[type] = o;
	}
	
	public void remove(GameObject o) {
		int type = o.getObjectType().getId();
		if(objects[type] != null && objects[type].getId() == o.getId()) {
			objects[type] = null;
		}
	}
	
	public ObjectList<GameObject> getDynamic() {
		ObjectList<GameObject> list = new ObjectArrayList<>();
		for(GameObject o : objects) {
			if(o != null && o.isDynamic())
				list.add(o);
		}
		return list;
	}
	
	public ObjectList<GameObject> getStatic() {
		ObjectList<GameObject> list = new ObjectArrayList<>();
		for(GameObject o : objects) {
			if(o != null && !o.isDynamic())
				list.add(o);
		}
		return list;
	}
	
	public ObjectList<GameObject> getInteract() {
		ObjectList<GameObject> list = new ObjectArrayList<>();
		for(GameObject o : objects) {
			if(o != null && (o.getObjectType().getGroup() == INTERACTABLE_OBJECT || o.getObjectType().getGroup() == WALL))
				list.add(o);
		}
		return list;
	}
	
	public void dynamicAction(Consumer<GameObject> action) {
		for(GameObject o : objects) {
			if(o != null && o.isDynamic())
				action.accept(o);
		}
	}
	
	public void interactiveAction(int id, Consumer<GameObject> action) {
		for(GameObject o : objects) {
			if(o != null && (o.getId() == id && o.getObjectType().getGroup() == INTERACTABLE_OBJECT || o.getObjectType().getGroup() == WALL))
				action.accept(o);
		}
	}
	
	public ObjectList<GameObject> getAll() {
		ObjectList<GameObject> list = new ObjectArrayList<>();
		for(GameObject o : objects) {
			if(o != null)
				list.add(o);
		}
		return list;
	}
	
	public GameObject getId(int id) {
		for(GameObject o : objects) {
			if(o == null)
				continue;
			if(o.getId() == id)
				return o;
		}
		return null;
	}
	
	public GameObject getType(ObjectType type) {
		return objects[type.getId()];
	}
	
	public boolean hasInteractive() {
		for(GameObject o : objects) {
			if(o != null && (o.getObjectType().getGroup() == INTERACTABLE_OBJECT || o.getObjectType().getGroup() == WALL))
				return true;
		}
		return false;
	}
	
	public GameObject getType(int type) {
		return objects[type];
	}
	
	public void reset() {
		for(int i = 0; i < objects.length; i++)
			objects[i] = null;
	}
	
}
