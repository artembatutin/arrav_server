package net.arrav.content.object.door;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.arrav.action.impl.ObjectAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;
import net.arrav.world.object.GameObject;
import net.arrav.world.object.ObjectDefinition;

import static net.arrav.content.teleport.TeleportType.DOOR;

/**
 * Handles the interaction of the doors in the world.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class DoorHandler {
	
	/**
	 * The list of doors
	 */
	private static Object2ObjectArrayMap<Position, Door> doors = new Object2ObjectArrayMap<>();
	
	/**
	 * The door appender event.
	 */
	public static final ObjectAction APPENDER = new ObjectAction() {
		@Override
		public boolean click(Player player, GameObject object, int click) {
			ObjectDefinition def = object.getDefinition();
			if(def == null)
				return false;
			if(exception(player, object))
				return false;
			if(doors.containsKey(object.getGlobalPos())) {
				Door door = doors.get(object.getGlobalPos());
				doors.remove(door.getCurrentOne(), door);
				Position sec = door.getCurrentSecond();
				if(sec != null) {
					doors.remove(sec, door);
				}
				door.append(player);
				doors.put(door.getCurrentOne(), door);
				sec = door.getCurrentSecond();
				if(sec != null) {
					doors.put(sec, door);
				}
				
			} else {
				object.getRegion().ifPresent(r -> {
					Door door = new Door(object, r);
					door.append(player);
					doors.put(door.getCurrentOne(), door);
					Position sec = door.getCurrentSecond();
					if(sec != null) {
						doors.put(sec, door);
					}
				});
			}
			return true;
		}
	};
	
	/**
	 * All exceptions to door opening.
	 */
	private static boolean exception(Player player, GameObject object) {
		
		if(object.getId() == 34811 && object.getGlobalPos().same(new Position(3104, 3498))) {
			if(player.isIronMan() || player.getRights().isStaff()) {
				player.teleport(new Position(player.getPosition().getX() >= 3104 ? 3103 : 3104, 3498), DOOR);
				if(player.getPosition().getX() >= 3104)
					player.getLocalMobs().stream().filter(n -> n.getId() == 6184).findFirst().ifPresent(e -> e.forceChat("Welcome to the iron man house " + (player.isIronMaxed() ? "captain" : "soldier") + " " + player.getFormatUsername() + "."));
			} else
				player.getLocalMobs().stream().filter(n -> n.getId() == 6184).findFirst().ifPresent(e -> e.forceChat("Only iron man members can enter, sir."));
			return true;
		} else if(object.getId() == 34811 && object.getGlobalPos().same(new Position(3091, 3507, 1))) {
			if(player.getRights().isDonator() || player.getRights().isStaff()) {
				player.teleport(new Position(player.getPosition().getX() >= 3091 ? 3090 : 3091, 3507, 1), DOOR);
				if(player.getPosition().getX() < 3091)
					player.getLocalMobs().stream().filter(n -> n.getId() == 8328).findFirst().ifPresent(e -> e.forceChat("Welcome to the donator zone " + player.getFormatUsername() + "."));
			} else
				player.getLocalMobs().stream().filter(n -> n.getId() == 8328).findFirst().ifPresent(e -> e.forceChat("Only donators can enter, sir."));
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
