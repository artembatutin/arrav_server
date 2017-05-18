package net.edge.world.content.door;

import net.edge.world.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.object.ObjectDefinition;
import net.edge.world.node.object.ObjectNode;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Handles the interaction of the doors in the world.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class DoorHandler {

	/**
	 * The list of doors
	 */
	private static HashMap<Position, SimpleDoor> doors = new HashMap<>();

	private static boolean exception(Player player, ObjectNode object) {
		switch(object.getId()) {
			case 68429://armadyl gwd door
				return object.getPosition().same(new Position(2839, 5296, 2)) || object.getPosition().same(new Position(2839, 5295, 2));
			case 26425://bandos gwd door
				return object.getPosition().same(new Position(2863, 5354, 2)) || object.getPosition().same(new Position(2864, 5354, 2));
			case 26428://zamorak gwd door
				return object.getPosition().same(new Position(2925, 5331, 2)) || object.getPosition().same(new Position(2925, 5332, 2));
			case 68430://saradomin gwd door
				return object.getPosition().same(new Position(2908, 5265, 0)) || object.getPosition().same(new Position(2907, 5265, 0));
			case 24375:
				return object.getPosition().same(new Position(3214, 3415)) || object.getPosition().same(new Position(3217, 3419)) || object.getPosition().same(new Position(3208, 3415));
			case 15644:
				return object.getPosition().same(new Position(2855, 3545)) || object.getPosition().same(new Position(2855, 3546)) || object.getPosition().same(new Position(2846, 3541, 2)) || object.getPosition().same(new Position(2847, 3541, 2));
			case 15641:
				return object.getPosition().same(new Position(2854, 3545)) || object.getPosition().same(new Position(2854, 3546)) || object.getPosition().same(new Position(2846, 3540, 2)) || object.getPosition().same(new Position(2847, 3540, 2));
			default:
				return false;
		}
	}

	public static boolean interact(Player player, ObjectNode object) {
		ObjectDefinition def = object.getDefinition();
		if(def == null)
			return false;
		if(isDoor(def)) {
			if(exception(player, object)) {
				if(Arrays.stream(new int[]{68429, 26425, 26428, 68430}).noneMatch(t -> t == object.getId())) {
					player.message("This door seems stuck...");
				}
				return false;
			}
			if(doors.containsKey(object.getPosition())) {
				SimpleDoor door = doors.get(object.getPosition());
				doors.remove(door.getPosition(), door);
				door.append(player);
				doors.put(door.getPosition(), door);
			} else {
				SimpleDoor door = new SimpleDoor(object);
				door.append(player);
				doors.put(door.getPosition(), door);

			}
		}
		return false;
	}

	private static boolean isDoor(ObjectDefinition def) {
		String name = def.getName().toLowerCase();
		return (name.contains("door") && !name.contains("trap") && !name.contains("way")) || name.contains("gate");
	}

}
