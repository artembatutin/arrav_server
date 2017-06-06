package net.edge.content.door;

import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectDefinition;
import net.edge.world.object.ObjectNode;

import java.util.HashMap;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.DOOR;

/**
 * Handles the interaction of the doors in the world.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class DoorHandler {

	/**
	 * The list of doors
	 */
	private static HashMap<Position, SimpleDoor> doors = new HashMap<>();
	
	/**
	 * The door appender event.
	 */
	public static final ObjectEvent APPENDER = new ObjectEvent() {
		@Override
		public boolean click(Player player, ObjectNode object, int click) {
			ObjectDefinition def = object.getDefinition();
			if(def == null)
				return false;
			if(exception(player, object))
				return false;
			if(doors.containsKey(object.getGlobalPos())) {
				SimpleDoor door = doors.get(object.getGlobalPos());
				doors.remove(door.getCurrent().getGlobalPos(), door);
				door.append(player);
				doors.put(door.getCurrent().getGlobalPos(), door);
			} else {
				SimpleDoor door = new SimpleDoor(object);
				door.append(player);
				doors.put(door.getCurrent().getGlobalPos(), door);
				
			}
			return true;
		}
	};
	
	/**
	 * All exceptions to door opening.
	 */
	private static boolean exception(Player player, ObjectNode object) {
		if(object.getId() == 34811 && object.getGlobalPos().same(new Position(3104, 3498))) {
			if(player.isNight()) {
				player.teleport(new Position(player.getPosition().getX() >= 3104 ? 3103 : 3104, 3498), DOOR);
				if(player.getPosition().getX() >= 3104)
					player.getLocalNpcs().stream().filter(n -> n.getId() == 6184).findFirst().ifPresent(e -> e.forceChat("Welcome to the watch " + (player.isNightMaxed() ? "captain" : "soldier") + " " + player.getFormatUsername() + "."));
			} else
				player.getLocalNpcs().stream().filter(n -> n.getId() == 6184).findFirst().ifPresent(e -> e.forceChat("Only night's watch members can enter, sir."));
			return true;
		}
		switch(object.getId()) {
			case 68429://armadyl gwd door
				return object.getGlobalPos().same(new Position(2839, 5296, 2)) || object.getGlobalPos().same(new Position(2839, 5295, 2));
			case 26425://bandos gwd door
				return object.getGlobalPos().same(new Position(2863, 5354, 2)) || object.getGlobalPos().same(new Position(2864, 5354, 2));
			case 26428://zamorak gwd door
				return object.getGlobalPos().same(new Position(2925, 5331, 2)) || object.getGlobalPos().same(new Position(2925, 5332, 2));
			case 68430://saradomin gwd door
				return object.getGlobalPos().same(new Position(2908, 5265, 0)) || object.getGlobalPos().same(new Position(2907, 5265, 0));
			case 24375:
				return object.getGlobalPos().same(new Position(3214, 3415)) || object.getGlobalPos().same(new Position(3217, 3419)) || object.getGlobalPos().same(new Position(3208, 3415));
			case 15644:
				return object.getGlobalPos().same(new Position(2855, 3545)) || object.getGlobalPos().same(new Position(2855, 3546)) || object.getGlobalPos().same(new Position(2846, 3541, 2)) || object.getGlobalPos().same(new Position(2847, 3541, 2));
			case 15641:
				return object.getGlobalPos().same(new Position(2854, 3545)) || object.getGlobalPos().same(new Position(2854, 3546)) || object.getGlobalPos().same(new Position(2846, 3540, 2)) || object.getGlobalPos().same(new Position(2847, 3540, 2));
			default:
				return false;
		}
	}

	public static boolean isDoor(ObjectDefinition def) {
		String name = def.getName().toLowerCase();
		return (name.contains("door") && !name.contains("trap") && !name.contains("way")) || name.contains("gate");
	}

}
