package net.arrav.content.object.door;

import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.object.DynamicObject;
import net.arrav.world.entity.object.GameObject;
import net.arrav.world.entity.object.ObjectDirection;
import net.arrav.world.entity.object.ObjectType;
import net.arrav.world.entity.region.Region;
import net.arrav.world.locale.Position;

import java.util.Optional;

import static net.arrav.world.entity.object.ObjectDirection.*;
import static net.arrav.world.entity.object.ObjectType.STRAIGHT_WALL;

/**
 * Represents a single door.
 */
public class Door {
	
	private boolean isAppend = false;
	
	private final DynamicObject appended;
	private final DynamicObject appendedSecond;
	
	private final GameObject original;
	private final GameObject originalSecond;
	
	public Door(GameObject object, Region reg) {
		boolean closed = object.getDefinition().hasAction("open");
		original = object.copy();
		
		Position pos = original.getPosition();
		//getting second door if found.
		if(original.getObjectType() == STRAIGHT_WALL) {
			if(original.getDirection() == WEST || original.getDirection() == EAST) {
				Optional<GameObject> northen = reg.getObject(STRAIGHT_WALL, pos.move(0, 1));
				Optional<GameObject> southen = reg.getObject(STRAIGHT_WALL, pos.move(0, -1));
				if(northen.isPresent() && northen.get().getDefinition() != null && northen.get().getDefinition().hasAction(closed ? "open" : "close")) {
					originalSecond = northen.get();
				} else if(southen.isPresent() && southen.get().getDefinition() != null && southen.get().getDefinition().hasAction(closed ? "open" : "close")) {
					originalSecond = southen.get();
				} else {
					originalSecond = null;
				}
			} else {
				Optional<GameObject> easten = reg.getObject(STRAIGHT_WALL, pos.move(1, 0));
				Optional<GameObject> westen = reg.getObject(STRAIGHT_WALL, pos.move(-1, 0));
				if(easten.isPresent() && easten.get().getDefinition() != null && easten.get().getDefinition().hasAction(closed ? "open" : "close")) {
					originalSecond = easten.get();
				} else if(westen.isPresent() && westen.get().getDefinition() != null && westen.get().getDefinition().hasAction(closed ? "open" : "close")) {
					originalSecond = westen.get();
				} else {
					originalSecond = null;
				}
			}
		} else {
			originalSecond = null;
		}
		
		//finding appended door
		int xAdjustment = 0;
		int yAdjustment = 0;
		ObjectDirection direction = original.getDirection();
		int xAdjustment2 = 0;
		int yAdjustment2 = 0;
		ObjectDirection direction2 = originalSecond != null ? originalSecond.getDirection() : null;
		GameObject first = null;
		GameObject second = null;
		
		if(original.getObjectType() == STRAIGHT_WALL) {
			if(originalSecond == null) {
				if(closed) {
					if(original.getDirection() == WEST) {
						xAdjustment = -1;
						direction = NORTH;
					} else if(original.getDirection() == NORTH) {
						yAdjustment = 1;
						direction = EAST;
					} else if(original.getDirection() == EAST) {
						xAdjustment = 1;
						direction = SOUTH;
					} else if(original.getDirection() == SOUTH) {
						yAdjustment = -1;
						direction = WEST;
					}
				} else {
					GameObject[] adjacants = {reg.getObject(STRAIGHT_WALL, pos.move(-1, 1)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(0, 1)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(1, 1)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(-1, 0)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(1, 0)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(-1, -1)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(0, -1)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(1, -1)).orElse(null)};
					for(GameObject ad : adjacants) {
						if(ad == null)
							continue;
						if(ad.isAdjacantDoor(original)) {
							direction = ad.getDirection();
							if(original.getDirection() == WEST || original.getDirection() == EAST)
								yAdjustment = ad.getY() - pos.getY();
							else
								xAdjustment = ad.getX() - pos.getX();
							break;
						}
					}
				}
			} else {
				Position pos2 = originalSecond.getPosition();
				if(closed)
					first = (original.getDirection() == WEST || original.getDirection() == EAST) ? pos.getY() < pos2.getY() ? original : originalSecond : pos.getX() < pos2.getX() ? original : originalSecond;
				else
					first = (original.getDirection() == WEST || original.getDirection() == EAST) ? pos.getX() < pos2.getX() ? original : originalSecond : pos.getY() < pos2.getY() ? original : originalSecond;
				second = original.getId() == first.getId() && original.getPosition().same(first.getPosition()) ? originalSecond : original;
				pos = first.getPosition();
				
				if(closed) {
					if(first.getDirection() == WEST || first.getDirection() == EAST) {
						xAdjustment = first.getDirection() == EAST ? 1 : -1;
						xAdjustment2 = xAdjustment;
						direction = SOUTH;
						direction2 = NORTH;
						
					} else if(first.getDirection() == NORTH || first.getDirection() == SOUTH) {
						yAdjustment = first.getDirection() == NORTH ? 1 : -1;
						yAdjustment2 = yAdjustment;
						direction = WEST;
						direction2 = EAST;
					}
				} else {
					GameObject[] adjacants = {reg.getObject(STRAIGHT_WALL, pos.move(-1, 1)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(0, 1)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(1, 1)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(-1, 0)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(1, 0)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(-1, -1)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(0, -1)).orElse(null), reg.getObject(STRAIGHT_WALL, pos.move(1, -1)).orElse(null)};
					for(GameObject ad : adjacants) {
						if(ad == null)
							continue;
						if(ad.isAdjacantDoor(first)) {
							direction = ad.getDirection();
							if(first.getDirection() == WEST || first.getDirection() == EAST) {
								yAdjustment = ad.getY() - pos.getY();
								yAdjustment2 = yAdjustment;
							} else {
								xAdjustment = ad.getX() - pos.getX();
								xAdjustment2 = xAdjustment;
							}
							direction2 = direction;
							break;
						}
					}
				}
			}
		} else if(original.getObjectType() == ObjectType.DIAGONAL_WALL) {
			if(closed) {
				if(original.getDirection() == WEST) {
					xAdjustment = 1;
					direction = SOUTH;
				} else if(original.getDirection() == NORTH) {
					xAdjustment = 1;
					direction = EAST;
				} else if(original.getDirection() == EAST) {
					xAdjustment = -1;
					direction = NORTH;
				} else if(original.getDirection() == SOUTH) {
					xAdjustment = -1;
					direction = WEST;
				} else if(original.getDirection() != original.getDirection()) {
					direction = original.getDirection();
				}
			} else {
				if(original.getDirection() == WEST) {
					xAdjustment = 1;
					direction = SOUTH;
				} else if(original.getDirection() == NORTH) {
					xAdjustment = 1;
					direction = WEST;
				} else if(original.getDirection() == EAST) {
					xAdjustment = -1;
					direction = NORTH;
				} else if(original.getDirection() == SOUTH) {
					xAdjustment = -1;
					direction = EAST;
				} else if(original.getDirection() != original.getDirection()) {
					direction = original.getDirection();
				}
			}
		}
		if(first != null) {
			appended = new DynamicObject(first.getId(), first.getPosition().move(xAdjustment, yAdjustment), direction, first.getObjectType(), false, 0, 0);
			appendedSecond = direction2 == null ? null : new DynamicObject(second.getId(), second.getPosition().move(xAdjustment2, yAdjustment2), direction2, second.getObjectType(), false, 0, 0);
		} else {
			appended = new DynamicObject(object.getId(), object.getPosition().move(xAdjustment, yAdjustment), direction, object.getObjectType(), false, 0, 0);
			appendedSecond = direction2 == null ? null : new DynamicObject(object.getId(), object.getPosition().move(xAdjustment2, yAdjustment2), direction2, object.getObjectType(), false, 0, 0);
		}
	}
	
	public void append(Player player) {
		if(isAppend) {
			appended.delete();
			appended.remove();
			if(appendedSecond != null) {
				appendedSecond.delete();
				appendedSecond.remove();
			}
			
			original.restore();
			original.publish();
			if(originalSecond != null) {
				originalSecond.restore();
				originalSecond.publish();
			}
			
		} else {
			original.delete();
			original.remove();
			if(originalSecond != null) {
				originalSecond.delete();
				originalSecond.remove();
			}
			
			appended.restore();
			appended.publish();
			if(appendedSecond != null) {
				appendedSecond.restore();
				appendedSecond.publish();
			}
		}
		
		isAppend = !isAppend;
	}
	
	public Position getCurrentOne() {
		return isAppend ? appended.getPosition() : original.getPosition();
	}
	
	public Position getCurrentSecond() {
		return isAppend ? appendedSecond == null ? null : appendedSecond.getPosition() : originalSecond == null ? null : originalSecond.getPosition();
	}
	
	public void setAppendId(int id) {
		appended.restore();
		appended.setId(id);
	}
	
	public void setAppendedSecondId(int id) {
		appendedSecond.restore();
		appendedSecond.setId(id);
	}
	
	public void setOriginalId(int id) {
		original.restore();
		original.setId(id);
	}
	
	public void setOriginalSecondId(int id) {
		originalSecond.restore();
		originalSecond.setId(id);
	}
	
	public void publish() {
		if(isAppend) {
			appended.publish();
			if(appendedSecond != null)
				appendedSecond.publish();
		} else {
			original.publish();
			if(originalSecond != null)
				originalSecond.publish();
		}
	}
	
	public boolean isAppend() {
		return isAppend;
	}
	
}
