package net.edge.world.content.door;

import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.object.ObjectDirection;
import net.edge.world.node.object.ObjectNode;
import net.edge.world.node.object.ObjectType;
import net.edge.world.node.region.Region;

/**
 * Represents a single door.
 */
public class SimpleDoor extends ObjectNode {

	private boolean closed = false;

	private boolean appended = false;

	private final ObjectNode original;

	public SimpleDoor(ObjectNode object) {
		super(object.getId(), object.getPosition(), object.getDirection(), object.getObjectType());
		original = new ObjectNode(object.getId(), object.getPosition(), object.getDirection(), object.getObjectType());
		closed = getDefinition().hasAction("openShop");
	}

	public void append(Player player) {
		Region reg = World.getRegions().getRegion(getPosition());
		reg.getObject(getId(), getPosition()).ifPresent(o -> {
			reg.unregister(o);
			reg.addDeletedObj(o);
		});
		int xAdjustment = 0;
		int yAdjustment = 0;
		if(!appended) {
			if(getObjectType() == ObjectType.STRAIGHT_WALL) {
				if(closed) {
					if(original.getDirection() == ObjectDirection.WEST && getDirection() == ObjectDirection.WEST) {
						xAdjustment = -1;
						setDirection(ObjectDirection.NORTH);
					} else if(original.getDirection() == ObjectDirection.NORTH && getDirection() == ObjectDirection.NORTH) {
						yAdjustment = 1;
						setDirection(ObjectDirection.EAST);
					} else if(original.getDirection() == ObjectDirection.EAST && getDirection() == ObjectDirection.EAST) {
						xAdjustment = 1;
						setDirection(ObjectDirection.SOUTH);
					} else if(original.getDirection() == ObjectDirection.SOUTH && getDirection() == ObjectDirection.SOUTH) {
						yAdjustment = -1;
						setDirection(ObjectDirection.WEST);
					} else if(original.getDirection() != getDirection()) {
						setDirection(original.getDirection());
					}
				} else {
					if(original.getDirection() == ObjectDirection.WEST && getDirection() == ObjectDirection.WEST) {
						yAdjustment = 1;
						setDirection(ObjectDirection.SOUTH);
					} else if(original.getDirection() == ObjectDirection.NORTH && getDirection() == ObjectDirection.NORTH) {
						xAdjustment = 1;
						setDirection(ObjectDirection.WEST);
					} else if(original.getDirection() == ObjectDirection.EAST && getDirection() == ObjectDirection.EAST) {
						yAdjustment = -1;
						setDirection(ObjectDirection.NORTH);
					} else if(original.getDirection() == ObjectDirection.SOUTH && getDirection() == ObjectDirection.SOUTH) {
						xAdjustment = -1;
						setDirection(ObjectDirection.EAST);
					} else if(original.getDirection() != getDirection()) {
						setDirection(original.getDirection());
					}
				}
			} else if(getObjectType() == ObjectType.DIAGONAL_WALL) {
				if(closed) {
					if(original.getDirection() == ObjectDirection.WEST && getDirection() == ObjectDirection.WEST) {
						xAdjustment = 1;
						setDirection(ObjectDirection.SOUTH);
					} else if(original.getDirection() == ObjectDirection.NORTH && getDirection() == ObjectDirection.NORTH) {
						xAdjustment = 1;
						setDirection(ObjectDirection.EAST);
					} else if(original.getDirection() == ObjectDirection.EAST && getDirection() == ObjectDirection.EAST) {
						xAdjustment = -1;
						setDirection(ObjectDirection.NORTH);
					} else if(original.getDirection() == ObjectDirection.SOUTH && getDirection() == ObjectDirection.SOUTH) {
						xAdjustment = -1;
						setDirection(ObjectDirection.WEST);
					} else if(original.getDirection() != getDirection()) {
						setDirection(original.getDirection());
					}
				} else {
					if(original.getDirection() == ObjectDirection.WEST && getDirection() == ObjectDirection.WEST) {
						xAdjustment = 1;
						setDirection(ObjectDirection.SOUTH);
					} else if(original.getDirection() == ObjectDirection.NORTH && getDirection() == ObjectDirection.NORTH) {
						xAdjustment = 1;
						setDirection(ObjectDirection.WEST);
					} else if(original.getDirection() == ObjectDirection.EAST && getDirection() == ObjectDirection.EAST) {
						xAdjustment = -1;
						setDirection(ObjectDirection.NORTH);
					} else if(original.getDirection() == ObjectDirection.SOUTH && getDirection() == ObjectDirection.SOUTH) {
						xAdjustment = -1;
						setDirection(ObjectDirection.EAST);
					} else if(original.getDirection() != getDirection()) {
						setDirection(original.getDirection());
					}
				}
			}
			if(getPosition().same(original.getPosition())) {
				setPosition(getPosition().move(xAdjustment, yAdjustment));
			} else {
				setPosition(original.getPosition());
			}
		} else {
			setPosition(original.getPosition());
			setDirection(original.getDirection());
		}
		reg.removeDeletedObj(this);
		closed = !closed;
		appended = !appended;
		reg.register(this);
	}

}
