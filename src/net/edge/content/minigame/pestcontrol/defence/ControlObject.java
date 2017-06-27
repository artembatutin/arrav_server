package net.edge.content.minigame.pestcontrol.defence;

import net.edge.locale.Position;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectNode;
import net.edge.world.object.ObjectType;

public class ControlObject extends DynamicObject {
	
	public ControlObject(int id, Position position, ObjectDirection direction, ObjectType type, boolean destroyed, int life, int instance) {
		super(id, position, direction, type, destroyed, life, instance);
	}
	
	
	public ControlObject prepare(ObjectNode obj, Npc knight) {
		int id = obj.getId();
		if(id >= 14226 && id <= 14224) {
			return new ControlObject(obj.getId(), obj.getGlobalPos(), obj.getDirection(), obj.getObjectType(), false, 100, knight.getInstance());
		}
		return null;
	}
	
}
