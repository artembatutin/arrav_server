package net.edge.world.node.region;

import net.edge.util.rand.RandomUtils;
import net.edge.World;
import net.edge.locale.Boundary;
import net.edge.locale.Position;
import net.edge.world.Direction;
import net.edge.world.object.ObjectDefinition;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectNode;
import net.edge.world.object.ObjectType;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.edge.world.object.ObjectType.*;

/**
 * Contains traversal data for a set of regions.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class TraversalMap {
	
	/**
	 * Marks a {@link ObjectNode} with the specified attributes on the
	 * specified {@link Position} to the {@code TraversalMap}.
	 * @param object The game object.
	 * @param add    The condition if the object is added.
	 * @param list   the condition if the region object list will be affected.
	 */
	public void markObject(Region reg, ObjectNode object, boolean add, boolean list) {
		if(object.getId() > ObjectDefinition.DEFINITIONS.length) {
			return;
		}
		ObjectDefinition def = ObjectDefinition.DEFINITIONS[object.getId()];
		Position position = new Position(object.getX(), object.getY(), object.getZ());
		
		//Sets the sizes.
		final int sizeX;
		final int sizeY;
		if(object.getDirection() == ObjectDirection.NORTH || object.getDirection() == ObjectDirection.SOUTH) {
			sizeX = def.getSizeY();
			sizeY = def.getSizeX();
		} else {
			sizeX = def.getSizeX();
			sizeY = def.getSizeY();
		}
		
		if(def.isSolid()) {
			if(object.getObjectType() == GROUND_PROP) {
				if(def.hasActions() || def.isDecoration()) {
					if(def.hasActions()) {
						markOccupant(reg, position.getZ(), position.getX(), position.getY(), sizeX, sizeY, false, add);
					}
				}
			} else if(object.getObjectType() == GENERAL_PROP || object.getObjectType() == WALKABLE_PROP) {
				markOccupant(reg, position.getZ(), position.getX(), position.getY(), sizeX, sizeY, def.isWalkable(), add);
			} else if(object.getObjectType().getId() >= 12) {
				markOccupant(reg, position.getZ(), position.getX(), position.getY(), sizeX, sizeY, def.isWalkable(), add);
			} else if(object.getObjectType() == DIAGONAL_WALL) {
				markOccupant(reg, position.getZ(), position.getX(), position.getY(), sizeX, sizeY, def.isWalkable(), add);
			} else if(object.getObjectType().getId() >= 0 && object.getObjectType().getId() <= 3) {
				if(add)
					markWall(reg, object.getDirection(), position.getZ(), position.getX(), position.getY(), object.getObjectType(), def.isWalkable());
				else
					unmarkWall(reg, object.getDirection(), position.getZ(), position.getX(), position.getY(), object.getObjectType(), def.isWalkable());
			}
		}
		if(reg == null)
			reg = World.getRegions().getRegion(position);
		if(add && list) {
			reg.addObj(object);
		} else if(list) {
			reg.removeObj(object);
		}
		
	}
	
	/**
	 * Informs the region of an existing wall.
	 * @param orientation  The orientation of the wall.
	 * @param height       The walls height.
	 * @param x            The walls x coordinate.
	 * @param y            The walls y coordinate.
	 * @param type         The type of wall.
	 * @param impenetrable Whether or not this wall can be passed through.
	 */
	private void markWall(Region reg, ObjectDirection orientation, int height, int x, int y, ObjectType type, boolean impenetrable) {
		switch(type) {
			case STRAIGHT_WALL:
				if(orientation == ObjectDirection.WEST) {
					set(reg, height, x, y, TraversalConstants.WALL_WEST);
					set(reg, height, x - 1, y, TraversalConstants.WALL_EAST);
					if(impenetrable) {
						set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
						set(reg, height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
					}
				}
				if(orientation == ObjectDirection.NORTH) {
					set(reg, height, x, y, TraversalConstants.WALL_NORTH);
					set(reg, height, x, y + 1, TraversalConstants.WALL_SOUTH);
					if(impenetrable) {
						set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_NORTH);
						set(reg, height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
					}
				}
				if(orientation == ObjectDirection.EAST) {
					set(reg, height, x, y, TraversalConstants.WALL_EAST);
					set(reg, height, x + 1, y, TraversalConstants.WALL_WEST);
					if(impenetrable) {
						set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
						set(reg, height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
					}
				}
				if(orientation == ObjectDirection.SOUTH) {
					set(reg, height, x, y, TraversalConstants.WALL_SOUTH);
					set(reg, height, x, y - 1, TraversalConstants.WALL_NORTH);
					if(impenetrable) {
						set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
						set(reg, height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH);
					}
				}
				break;
			
			case ENTIRE_WALL:
				if(orientation == ObjectDirection.WEST) {
					set(reg, height, x, y, TraversalConstants.WALL_WEST | TraversalConstants.WALL_NORTH);
					set(reg, height, x - 1, y, TraversalConstants.WALL_EAST);
					set(reg, height, x, y + 1, TraversalConstants.WALL_SOUTH);
					if(impenetrable) {
						set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_WALL_NORTH);
						set(reg, height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
						set(reg, height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
					}
				}
				if(orientation == ObjectDirection.NORTH) {
					set(reg, height, x, y, TraversalConstants.WALL_EAST | TraversalConstants.WALL_NORTH);
					set(reg, height, x, y + 1, TraversalConstants.WALL_SOUTH);
					set(reg, height, x + 1, y, TraversalConstants.WALL_WEST);
					if(impenetrable) {
						set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_NORTH);
						set(reg, height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
						set(reg, height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
					}
				}
				if(orientation == ObjectDirection.EAST) {
					set(reg, height, x, y, TraversalConstants.WALL_EAST | TraversalConstants.WALL_SOUTH);
					set(reg, height, x + 1, y, TraversalConstants.WALL_WEST);
					set(reg, height, x, y - 1, TraversalConstants.WALL_NORTH);
					if(impenetrable) {
						set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_SOUTH);
						set(reg, height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
						set(reg, height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH);
					}
				}
				if(orientation == ObjectDirection.SOUTH) {
					set(reg, height, x, y, TraversalConstants.WALL_WEST | TraversalConstants.WALL_SOUTH);
					set(reg, height, x - 1, y, TraversalConstants.WALL_EAST);
					set(reg, height, x, y - 1, TraversalConstants.WALL_NORTH);
					if(impenetrable) {
						set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_WALL_SOUTH);
						set(reg, height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
						set(reg, height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH);
					}
				}
				break;
			
			case DIAGONAL_CORNER_WALL:
			case WALL_CORNER:
				if(orientation == ObjectDirection.WEST) {
					set(reg, height, x, y, TraversalConstants.WALL_NORTH_WEST);
					set(reg, height, x - 1, y + 1, TraversalConstants.WALL_SOUTH_EAST);
					if(impenetrable) {
						set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_NORTH_WEST);
						set(reg, height, x - 1, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH_EAST);
					}
				}
				if(orientation == ObjectDirection.NORTH) {
					set(reg, height, x, y, TraversalConstants.WALL_NORTH_EAST);
					set(reg, height, x + 1, y + 1, TraversalConstants.WALL_SOUTH_WEST);
					if(impenetrable) {
						set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_NORTH_EAST);
						set(reg, height, x + 1, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH_WEST);
					}
				}
				if(orientation == ObjectDirection.EAST) {
					set(reg, height, x, y, TraversalConstants.WALL_SOUTH_EAST);
					set(reg, height, x + 1, y - 1, TraversalConstants.WALL_NORTH_WEST);
					if(impenetrable) {
						set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_SOUTH_EAST);
						set(reg, height, x + 1, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH_WEST);
					}
				}
				if(orientation == ObjectDirection.SOUTH) {
					set(reg, height, x, y, TraversalConstants.WALL_SOUTH_WEST);
					set(reg, height, x - 1, y - 1, TraversalConstants.WALL_NORTH_EAST);
					if(impenetrable) {
						set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_SOUTH_WEST);
						set(reg, height, x - 1, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH_EAST);
					}
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * Informs the region of an existing wall being removed.
	 * @param orientation  The orientation of the wall.
	 * @param height       The walls height.
	 * @param x            The walls x coordinate.
	 * @param y            The walls y coordinate.
	 * @param type         The type of wall.
	 * @param impenetrable Whether or not this wall can be passed through.
	 */
	private void unmarkWall(Region reg, ObjectDirection orientation, int height, int x, int y, ObjectType type, boolean impenetrable) {
		switch(type) {
			case STRAIGHT_WALL:
				if(orientation == ObjectDirection.WEST) {
					unset(reg, height, x, y, TraversalConstants.WALL_WEST);
					unset(reg, height, x - 1, y, TraversalConstants.WALL_EAST);
					if(impenetrable) {
						unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
						unset(reg, height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
					}
				}
				if(orientation == ObjectDirection.NORTH) {
					unset(reg, height, x, y, TraversalConstants.WALL_NORTH);
					unset(reg, height, x, y + 1, TraversalConstants.WALL_SOUTH);
					if(impenetrable) {
						unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_NORTH);
						unset(reg, height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
					}
				}
				if(orientation == ObjectDirection.EAST) {
					unset(reg, height, x, y, TraversalConstants.WALL_EAST);
					unset(reg, height, x + 1, y, TraversalConstants.WALL_WEST);
					if(impenetrable) {
						unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
						unset(reg, height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
					}
				}
				if(orientation == ObjectDirection.SOUTH) {
					unset(reg, height, x, y, TraversalConstants.WALL_SOUTH);
					unset(reg, height, x, y - 1, TraversalConstants.WALL_NORTH);
					if(impenetrable) {
						unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
						unset(reg, height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH);
					}
				}
				break;
			
			case ENTIRE_WALL:
				if(orientation == ObjectDirection.WEST) {
					unset(reg, height, x, y, TraversalConstants.WALL_WEST | TraversalConstants.WALL_NORTH);
					unset(reg, height, x - 1, y, TraversalConstants.WALL_EAST);
					unset(reg, height, x, y + 1, TraversalConstants.WALL_SOUTH);
					if(impenetrable) {
						unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_WALL_NORTH);
						unset(reg, height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
						unset(reg, height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
					}
				}
				if(orientation == ObjectDirection.NORTH) {
					unset(reg, height, x, y, TraversalConstants.WALL_EAST | TraversalConstants.WALL_NORTH);
					unset(reg, height, x, y + 1, TraversalConstants.WALL_SOUTH);
					unset(reg, height, x + 1, y, TraversalConstants.WALL_WEST);
					if(impenetrable) {
						unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_NORTH);
						unset(reg, height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
						unset(reg, height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
					}
				}
				if(orientation == ObjectDirection.EAST) {
					unset(reg, height, x, y, TraversalConstants.WALL_EAST | TraversalConstants.WALL_SOUTH);
					unset(reg, height, x + 1, y, TraversalConstants.WALL_WEST);
					unset(reg, height, x, y - 1, TraversalConstants.WALL_NORTH);
					if(impenetrable) {
						unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_SOUTH);
						unset(reg, height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
						unset(reg, height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH);
					}
				}
				if(orientation == ObjectDirection.SOUTH) {
					unset(reg, height, x, y, TraversalConstants.WALL_EAST | TraversalConstants.WALL_SOUTH);
					unset(reg, height, x, y - 1, TraversalConstants.WALL_WEST);
					unset(reg, height, x - 1, y, TraversalConstants.WALL_NORTH);
					if(impenetrable) {
						unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_SOUTH);
						unset(reg, height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_WEST);
						unset(reg, height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_NORTH);
					}
				}
				break;
			
			case DIAGONAL_CORNER_WALL:
			case WALL_CORNER:
				if(orientation == ObjectDirection.WEST) {
					unset(reg, height, x, y, TraversalConstants.WALL_NORTH_WEST);
					unset(reg, height, x - 1, y + 1, TraversalConstants.WALL_SOUTH_EAST);
					if(impenetrable) {
						unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_NORTH_WEST);
						unset(reg, height, x - 1, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH_EAST);
					}
				}
				if(orientation == ObjectDirection.NORTH) {
					unset(reg, height, x, y, TraversalConstants.WALL_NORTH_EAST);
					unset(reg, height, x + 1, y + 1, TraversalConstants.WALL_SOUTH_WEST);
					if(impenetrable) {
						unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_NORTH_EAST);
						unset(reg, height, x + 1, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH_WEST);
					}
				}
				if(orientation == ObjectDirection.EAST) {
					unset(reg, height, x, y, TraversalConstants.WALL_SOUTH_EAST);
					unset(reg, height, x + 1, y - 1, TraversalConstants.WALL_NORTH_WEST);
					if(impenetrable) {
						unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_SOUTH_EAST);
						unset(reg, height, x + 1, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH_WEST);
					}
				}
				if(orientation == ObjectDirection.SOUTH) {
					unset(reg, height, x, y, TraversalConstants.WALL_SOUTH_WEST);
					unset(reg, height, x - 1, y - 1, TraversalConstants.WALL_NORTH_EAST);
					if(impenetrable) {
						unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_SOUTH_WEST);
						unset(reg, height, x - 1, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH_EAST);
					}
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * Marks the specified set of coordinates blocked, unable to be passed
	 * through.
	 * @param height     The height.
	 * @param x          The x coordinate.
	 * @param y          The y coordinate.
	 * @param block      The condition if the tile is blocked.
	 * @param projectile The condition if the tile is blocked for projectiles.
	 */
	public void mark(Region region, int height, int x, int y, boolean block, boolean projectile) {
		int localX = x & 0x3F;
		int localY = y & 0x3F;
		
		if(region == null)
			region = World.getRegions().getRegion(new Position(x, y));
		if(region == null)
			return;
		
		int modifiedHeight = height;
		if(region.getTile(1, localX, localY).isActive(TraversalConstants.BRIDGE)) {
			modifiedHeight = height - 1;
		}
		if(modifiedHeight < 0)
			modifiedHeight = 0;
		if(block) {
			region.getTile(modifiedHeight, x & 0x3F, y & 0x3F).set(TraversalConstants.BLOCKED);
			if(projectile)
				region.getTile(modifiedHeight, x & 0x3F, y & 0x3F).set(TraversalConstants.IMPENETRABLE_BLOCKED);
		} else {
			region.getTile(modifiedHeight, x & 0x3F, y & 0x3F).unset(TraversalConstants.BLOCKED);
			if(projectile)
				region.getTile(modifiedHeight, x & 0x3F, y & 0x3F).unset(TraversalConstants.IMPENETRABLE_BLOCKED);
		}
	}
	
	/**
	 * Marks the specified coordinates occupied by some object.
	 * @param height       The height.
	 * @param x            The x coordinate.
	 * @param y            The y coordinate.
	 * @param sizeX        The width of the occupation.
	 * @param sizeY        The length of the occupation.
	 * @param impenetrable Whether or not this occupation can be passed through.
	 * @param add          Flag if the occupant is added or removed.
	 */
	private void markOccupant(Region region, int height, int x, int y, int sizeX, int sizeY, boolean impenetrable, boolean add) {
		int flag = TraversalConstants.BLOCKED;
		if(impenetrable) {
			flag += TraversalConstants.IMPENETRABLE_BLOCKED;
		}
		for(int xPos = x; xPos < x + sizeX; xPos++) {
			for(int yPos = y; yPos < y + sizeY; yPos++) {
				if(add)
					set(region, height, xPos, yPos, flag);
				else
					unset(region, height, xPos, yPos, flag);
			}
		}
	}
	
	/**
	 * Marks the specified coordinates a bridge.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 */
	public void markBridge(Region region, int height, int x, int y) {
		set(region, height, x, y, TraversalConstants.BRIDGE);
	}
	
	/**
	 * Tests if the specified position can be traversed north.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param size   The size of the entity attempting to traverse north.
	 * @return <code>true</code> if it is possible to traverse north otherwise
	 * <code>false</code>
	 */
	private boolean isTraversableNorth(int height, int x, int y, int size) {
		for(int offsetX = 0; offsetX < size; offsetX++) {
			for(int offsetY = 0; offsetY < size; offsetY++) {
				if(!isTraversableNorth(height, x + offsetX, y + offsetY)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Tests if the specified position can be traversed north.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @return <code>true</code> if it is possible to traverse north otherwise
	 * <code>false</code>.
	 */
	private boolean isTraversableNorth(int height, int x, int y) {
		return isTraversableNorth(height, x, y, false);
	}
	
	/**
	 * Tests if the specified position can be traversed north.
	 * @param height       The height.
	 * @param x            The x coordinate.
	 * @param y            The y coordinate.
	 * @param impenetrable Whether or not this occupation can be traversed.
	 * @return <code>true</code> if it is possible to traverse north otherwise
	 * <code>false</code>.
	 */
	private boolean isTraversableNorth(int height, int x, int y, boolean impenetrable) {
		if(impenetrable) {
			return isInactive(height, x, y + 1, TraversalConstants.IMPENETRABLE_BLOCKED | TraversalConstants.IMPENETRABLE_WALL_SOUTH);
		}
		return isInactive(height, x, y + 1, TraversalConstants.WALL_SOUTH | TraversalConstants.BLOCKED);
	}
	
	/**
	 * Tests if the specified position can be traversed south.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param size   The size of the entity attempting to traverse south.
	 * @return <code>true</code> if it is possible to traverse south otherwise
	 * <code>false</code>
	 */
	private boolean isTraversableSouth(int height, int x, int y, int size) {
		for(int offsetX = 0; offsetX < size; offsetX++) {
			for(int offsetY = 0; offsetY < size; offsetY++) {
				if(!isTraversableSouth(height, x + offsetX, y + offsetY)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Tests if the specified position can be traversed south.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @return <code>true</code> if it is possible to traverse south otherwise
	 * <code>false</code>.
	 */
	private boolean isTraversableSouth(int height, int x, int y) {
		return isTraversableSouth(height, x, y, false);
	}
	
	/**
	 * Tests if the specified position can be traversed south.
	 * @param height       The height.
	 * @param x            The x coordinate.
	 * @param y            The y coordinate.
	 * @param impenetrable Whether or not this occupation can be traversed.
	 * @return <code>true</code> if it is possible to traverse south otherwise
	 * <code>false</code>.
	 */
	private boolean isTraversableSouth(int height, int x, int y, boolean impenetrable) {
		if(impenetrable) {
			return isInactive(height, x, y - 1, TraversalConstants.IMPENETRABLE_BLOCKED | TraversalConstants.IMPENETRABLE_WALL_NORTH);
		}
		return isInactive(height, x, y - 1, TraversalConstants.WALL_NORTH | TraversalConstants.BLOCKED);
	}
	
	/**
	 * Tests if the specified position can be traversed east.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param size   The size of the entity attempting to traverse east.
	 * @return <code>true</code> if it is possible to traverse east otherwise
	 * <code>false</code>
	 */
	private boolean isTraversableEast(int height, int x, int y, int size) {
		for(int offsetX = 0; offsetX < size; offsetX++) {
			for(int offsetY = 0; offsetY < size; offsetY++) {
				if(!isTraversableEast(height, x + offsetX, y + offsetY)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Tests if the specified position can be traversed east.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @return <code>true</code> if it is possible to traverse east otherwise
	 * <code>false</code>.
	 */
	private boolean isTraversableEast(int height, int x, int y) {
		return isTraversableEast(height, x, y, false);
	}
	
	/**
	 * Tests if the specified position can be traversed east.
	 * @param height       The height.
	 * @param x            The x coordinate.
	 * @param y            The y coordinate.
	 * @param impenetrable Whether or not this occupation can be traversed.
	 * @return <code>true</code> if it is possible to traverse east otherwise
	 * <code>false</code>.
	 */
	private boolean isTraversableEast(int height, int x, int y, boolean impenetrable) {
		if(impenetrable) {
			return isInactive(height, x + 1, y, TraversalConstants.IMPENETRABLE_BLOCKED | TraversalConstants.IMPENETRABLE_WALL_WEST);
		}
		return isInactive(height, x + 1, y, TraversalConstants.WALL_WEST | TraversalConstants.BLOCKED);
	}
	
	/**
	 * Tests if the specified position can be traversed west.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param size   The size of the entity attempting to traverse west.
	 * @return <code>true</code> if it is possible to traverse west otherwise
	 * <code>false</code>
	 */
	private boolean isTraversableWest(int height, int x, int y, int size) {
		for(int offsetX = 0; offsetX < size; offsetX++) {
			for(int offsetY = 0; offsetY < size; offsetY++) {
				if(!isTraversableWest(height, x + offsetX, y + offsetY)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Tests if the specified position can be traversed west.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @return <code>true</code> if it is possible to traverse west otherwise
	 * <code>false</code>.
	 */
	private boolean isTraversableWest(int height, int x, int y) {
		return isTraversableWest(height, x, y, false);
	}
	
	/**
	 * Tests if the specified position can be traversed west.
	 * @param height       The height.
	 * @param x            The x coordinate.
	 * @param y            The y coordinate.
	 * @param impenetrable Whether or not this occupation can be traversed.
	 * @return <code>true</code> if it is possible to traverse west otherwise
	 * <code>false</code>.
	 */
	private boolean isTraversableWest(int height, int x, int y, boolean impenetrable) {
		if(impenetrable) {
			return isInactive(height, x - 1, y, TraversalConstants.IMPENETRABLE_BLOCKED | TraversalConstants.IMPENETRABLE_WALL_EAST);
		}
		return isInactive(height, x - 1, y, TraversalConstants.WALL_EAST | TraversalConstants.BLOCKED);
	}
	
	/**
	 * Tests if the specified position can be traversed north east.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param size   The size of the entity attempting to traverse north east.
	 * @return <code>true</code> if it is possible to traverse north east
	 * otherwise <code>false</code>
	 */
	private boolean isTraversableNorthEast(int height, int x, int y, int size) {
		for(int offsetX = 0; offsetX < size; offsetX++) {
			for(int offsetY = 0; offsetY < size; offsetY++) {
				if(!isTraversableNorthEast(height, x + offsetX, y + offsetY)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Tests if the specified position can be traversed north east.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @return <code>true</code> if it is possible to traverse north east
	 * otherwise <code>false</code>.
	 */
	private boolean isTraversableNorthEast(int height, int x, int y) {
		return isTraversableNorthEast(height, x, y, false);
	}
	
	/**
	 * Tests if the specified position can be traversed north east.
	 * @param height       The height.
	 * @param x            The x coordinate.
	 * @param y            The y coordinate.
	 * @param impenetrable Whether or not this occupation can be traversed.
	 * @return <code>true</code> if it is possible to traverse north east
	 * otherwise <code>false</code>.
	 */
	private boolean isTraversableNorthEast(int height, int x, int y, boolean impenetrable) {
		if(impenetrable) {
			return isInactive(height, x + 1, y + 1, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_WALL_SOUTH | TraversalConstants.IMPENETRABLE_WALL_SOUTH_WEST) && isInactive(height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_BLOCKED) && isInactive(height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH | TraversalConstants.IMPENETRABLE_BLOCKED);
		}
		return isInactive(height, x + 1, y + 1, TraversalConstants.WALL_WEST | TraversalConstants.WALL_SOUTH | TraversalConstants.WALL_SOUTH_WEST | TraversalConstants.BLOCKED) && isInactive(height, x + 1, y, TraversalConstants.WALL_WEST | TraversalConstants.BLOCKED) && isInactive(height, x, y + 1, TraversalConstants.WALL_SOUTH | TraversalConstants.BLOCKED);
	}
	
	/**
	 * Tests if the specified position can be traversed north west.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param size   The size of the entity attempting to traverse north west.
	 * @return <code>true</code> if it is possible to traverse north west
	 * otherwise <code>false</code>
	 */
	private boolean isTraversableNorthWest(int height, int x, int y, int size) {
		for(int offsetX = 0; offsetX < size; offsetX++) {
			for(int offsetY = 0; offsetY < size; offsetY++) {
				if(!isTraversableNorthWest(height, x + offsetX, y + offsetY)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Tests if the specified position can be traversed north west.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @return <code>true</code> if it is possible to traverse north west
	 * otherwise <code>false</code>.
	 */
	private boolean isTraversableNorthWest(int height, int x, int y) {
		return isTraversableNorthWest(height, x, y, false);
	}
	
	/**
	 * Tests if the specified position can be traversed north west.
	 * @param height       The height.
	 * @param x            The x coordinate.
	 * @param y            The y coordinate.
	 * @param impenetrable Whether or not this occupation can be traversed.
	 * @return <code>true</code> if it is possible to traverse north west
	 * otherwise <code>false</code>.
	 */
	private boolean isTraversableNorthWest(int height, int x, int y, boolean impenetrable) {
		if(impenetrable) {
			return isInactive(height, x - 1, y + 1, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_SOUTH | TraversalConstants.IMPENETRABLE_WALL_SOUTH_EAST) && isInactive(height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_BLOCKED) && isInactive(height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH | TraversalConstants.IMPENETRABLE_BLOCKED);
		}
		return isInactive(height, x - 1, y + 1, TraversalConstants.WALL_EAST | TraversalConstants.WALL_SOUTH | TraversalConstants.WALL_SOUTH_EAST | TraversalConstants.BLOCKED) && isInactive(height, x - 1, y, TraversalConstants.WALL_EAST | TraversalConstants.BLOCKED) && isInactive(height, x, y + 1, TraversalConstants.WALL_SOUTH | TraversalConstants.BLOCKED);
	}
	
	/**
	 * Tests if the specified position can be traversed south east.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param size   The size of the entity attempting to traverse south east.
	 * @return <code>true</code> if it is possible to traverse south east
	 * otherwise <code>false</code>
	 */
	private boolean isTraversableSouthEast(int height, int x, int y, int size) {
		for(int offsetX = 0; offsetX < size; offsetX++) {
			for(int offsetY = 0; offsetY < size; offsetY++) {
				if(!isTraversableSouthEast(height, x + offsetX, y + offsetY)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Tests if the specified position can be traversed south east.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @return <code>true</code> if it is possible to traverse south east
	 * otherwise <code>false</code>.
	 */
	private boolean isTraversableSouthEast(int height, int x, int y) {
		return isTraversableSouthEast(height, x, y, false);
	}
	
	/**
	 * Tests if the specified position can be traversed south east.
	 * @param height       The height.
	 * @param x            The x coordinate.
	 * @param y            The y coordinate.
	 * @param impenetrable Whether or not this occupation can be traversed.
	 * @return <code>true</code> if it is possible to traverse south east
	 * otherwise <code>false</code>.
	 */
	private boolean isTraversableSouthEast(int height, int x, int y, boolean impenetrable) {
		if(impenetrable) {
			return isInactive(height, x + 1, y - 1, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_WALL_NORTH | TraversalConstants.IMPENETRABLE_WALL_NORTH_WEST) && isInactive(height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_BLOCKED) && isInactive(height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH | TraversalConstants.IMPENETRABLE_BLOCKED);
		}
		return isInactive(height, x + 1, y - 1, TraversalConstants.WALL_WEST | TraversalConstants.WALL_NORTH | TraversalConstants.WALL_NORTH_WEST | TraversalConstants.BLOCKED) && isInactive(height, x + 1, y, TraversalConstants.WALL_WEST | TraversalConstants.BLOCKED) && isInactive(height, x, y - 1, TraversalConstants.WALL_NORTH | TraversalConstants.BLOCKED);
	}
	
	/**
	 * Tests if the specified position can be traversed south west.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param size   The size of the entity attempting to traverse south west.
	 * @return <code>true</code> if it is possible to traverse south west
	 * otherwise <code>false</code>
	 */
	private boolean isTraversableSouthWest(int height, int x, int y, int size) {
		for(int offsetX = 0; offsetX < size; offsetX++) {
			for(int offsetY = 0; offsetY < size; offsetY++) {
				if(!isTraversableSouthWest(height, x + offsetX, y + offsetY)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Tests if the specified position can be traversed south west.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @return <code>true</code> if it is possible to traverse south west
	 * otherwise <code>false</code>.
	 */
	private boolean isTraversableSouthWest(int height, int x, int y) {
		return isTraversableSouthWest(height, x, y, false);
	}
	
	/**
	 * Tests if the specified position can be traversed south west.
	 * @param height       The height.
	 * @param x            The x coordinate.
	 * @param y            The y coordinate.
	 * @param impenetrable Whether or not this occupation can be traversed.
	 * @return <code>true</code> if it is possible to traverse south west
	 * otherwise <code>false</code>.
	 */
	private boolean isTraversableSouthWest(int height, int x, int y, boolean impenetrable) {
		if(impenetrable) {
			return isInactive(height, x - 1, y - 1, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_NORTH | TraversalConstants.IMPENETRABLE_WALL_NORTH_EAST) && isInactive(height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_BLOCKED) && isInactive(height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH | TraversalConstants.IMPENETRABLE_BLOCKED);
		}
		return isInactive(height, x - 1, y - 1, TraversalConstants.WALL_EAST | TraversalConstants.WALL_NORTH | TraversalConstants.WALL_NORTH_EAST | TraversalConstants.BLOCKED) && isInactive(height, x - 1, y, TraversalConstants.WALL_EAST | TraversalConstants.BLOCKED) && isInactive(height, x, y - 1, TraversalConstants.WALL_NORTH | TraversalConstants.BLOCKED);
	}
	
	/**
	 * Sets a flag on the specified position.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param flag   The flag to put on this tile.
	 */
	public void set(Region region, int height, int x, int y, int flag) {
		if(region == null)
			region = World.getRegions().getRegion(new Position(x, y));
		if(region == null)
			return;
		region.getTile(height, x & 0x3F, y & 0x3F).set(flag);
	}
	
	/**
	 * Checks whether or not the specified flag is not active on the specified
	 * position.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param flag   The flag to check.
	 * @return <code>true</code> if the specified flag is not active on the
	 * specified position, otherwise <code>false</code>.
	 */
	private boolean isInactive(int height, int x, int y, int flag) {
		int localX = x & 0x3F;
		int localY = y & 0x3F;
		
		Region region = World.getRegions().getRegion(new Position(x, y));
		if(region == null) {
			return false;
		}
		
		RegionTile tile = region.getTile(height, localX, localY);
		if(tile == null) {
			return false;
		}
		
		int modifiedHeight = height;
		if(tile.isActive(TraversalConstants.BRIDGE)) {
			modifiedHeight = height + 1;
		}
		
		tile = region.getTile(modifiedHeight, localX, localY);
		return tile != null && tile.isInactive(flag);
		
	}
	
	/**
	 * Unsets the specified flag from the specified position.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param flag   The flag to unset from the specified position.
	 */
	private void unset(Region region, int height, int x, int y, int flag) {
		if(region == null)
			region = World.getRegions().getRegion(new Position(x, y));
		if(region == null)
			return;
		region.getTile(height, x & 0x3F, y & 0x3F).unset(flag);
	}
	
	/**
	 * Tests whether or not a specified position is traversable in the specified
	 * direction.
	 * @param from      The position.
	 * @param direction The direction to traverse.
	 * @param size      The size of the entity attempting to traverse.
	 * @return <code>true</code> if the direction is traversable otherwise
	 * <code>false</code>.
	 */
	public boolean isTraversable(Position from, Direction direction, int size) {
		return isTraversable(from, null, direction, size);
	}
	
	/**
	 * Tests whether or not a specified position is traversable in the specified
	 * direction.
	 * @param from      The position.
	 * @param boundary  The boundary of this check.
	 * @param direction The direction to traverse.
	 * @param size      The size of the entity attempting to traverse.
	 * @return <code>true</code> if the direction is traversable otherwise
	 * <code>false</code>.
	 */
	public boolean isTraversable(Position from, Boundary boundary, Direction direction, int size) {
		switch(direction) {
			case NORTH:
				return (boundary == null || from.getY() + 1 <= boundary.getEndY()) && isTraversableNorth(from.getZ(), from.getX(), from.getY(), size);
			case SOUTH:
				return (boundary == null || from.getY() - 1 >= boundary.getStartY()) && isTraversableSouth(from.getZ(), from.getX(), from.getY(), size);
			case EAST:
				return (boundary == null || from.getX() + 1 <= boundary.getEndX()) && isTraversableEast(from.getZ(), from.getX(), from.getY(), size);
			case WEST:
				return (boundary == null || from.getX() - 1 >= boundary.getStartX()) && isTraversableWest(from.getZ(), from.getX(), from.getY(), size);
			case NORTH_EAST:
				return (boundary == null || from.getY() + 1 <= boundary.getEndY() && from.getX() + 1 <= boundary.getEndX()) && isTraversableNorthEast(from.getZ(), from.getX(), from.getY(), size);
			case NORTH_WEST:
				return (boundary == null || from.getY() + 1 <= boundary.getEndY() && from.getX() - 1 >= boundary.getStartX()) && isTraversableNorthWest(from.getZ(), from.getX(), from.getY(), size);
			case SOUTH_EAST:
				return (boundary == null || from.getY() - 1 >= boundary.getStartY() && from.getX() + 1 <= boundary.getEndX()) && isTraversableSouthEast(from.getZ(), from.getX(), from.getY(), size);
			case SOUTH_WEST:
				return (boundary == null || from.getY() - 1 >= boundary.getStartY() && from.getX() - 1 >= boundary.getStartX()) && isTraversableSouthWest(from.getZ(), from.getX(), from.getY(), size);
			case NONE:
				return true;
			default:
				throw new IllegalArgumentException("direction: " + direction + " is not valid");
		}
	}
	
	/**
	 * Tests whether or not a specified position is traversable in the specified
	 * direction.
	 * @param from         The position.
	 * @param direction    The direction to traverse.
	 * @param impenetrable The condition if impenetrability must be checked.
	 * @return <code>true</code> if the direction is traversable otherwise
	 * <code>false</code>.
	 */
	public boolean isTraversable(Position from, Direction direction, boolean impenetrable) {
		switch(direction) {
			case NORTH:
				return isTraversableNorth(from.getZ(), from.getX(), from.getY(), impenetrable);
			case SOUTH:
				return isTraversableSouth(from.getZ(), from.getX(), from.getY(), impenetrable);
			case EAST:
				return isTraversableEast(from.getZ(), from.getX(), from.getY(), impenetrable);
			case WEST:
				return isTraversableWest(from.getZ(), from.getX(), from.getY(), impenetrable);
			case NORTH_EAST:
				return isTraversableNorthEast(from.getZ(), from.getX(), from.getY(), impenetrable);
			case NORTH_WEST:
				return isTraversableNorthWest(from.getZ(), from.getX(), from.getY(), impenetrable);
			case SOUTH_EAST:
				return isTraversableSouthEast(from.getZ(), from.getX(), from.getY(), impenetrable);
			case SOUTH_WEST:
				return isTraversableSouthWest(from.getZ(), from.getX(), from.getY(), impenetrable);
			case NONE:
				return true;
			default:
				throw new IllegalArgumentException("direction: " + direction + " is not valid");
		}
	}
	
	/**
	 * Returns a {@link List} of positions that are traversable from the
	 * specified position.
	 * @param from The position moving from.
	 * @param size The size of the mob attempting to traverse.
	 * @return A {@link List} of positions.
	 */
	public List<Position> getNearbyTraversableTiles(Position from, int size) {
		List<Position> positions = new LinkedList<>();
		if(isTraversableNorth(from.getZ(), from.getX(), from.getY(), size))
			positions.add(new Position(from.getX(), from.getY() + 1, from.getZ()));
		if(isTraversableSouth(from.getZ(), from.getX(), from.getY(), size))
			positions.add(new Position(from.getX(), from.getY() - 1, from.getZ()));
		if(isTraversableEast(from.getZ(), from.getX(), from.getY(), size))
			positions.add(new Position(from.getX() + 1, from.getY(), from.getZ()));
		if(isTraversableWest(from.getZ(), from.getX(), from.getY(), size))
			positions.add(new Position(from.getX() - 1, from.getY(), from.getZ()));
		if(isTraversableNorthEast(from.getZ(), from.getX(), from.getY(), size))
			positions.add(new Position(from.getX() + 1, from.getY() + 1, from.getZ()));
		if(isTraversableNorthWest(from.getZ(), from.getX(), from.getY(), size))
			positions.add(new Position(from.getX() - 1, from.getY() + 1, from.getZ()));
		if(isTraversableSouthEast(from.getZ(), from.getX(), from.getY(), size))
			positions.add(new Position(from.getX() + 1, from.getY() - 1, from.getZ()));
		if(isTraversableSouthWest(from.getZ(), from.getX(), from.getY(), size))
			positions.add(new Position(from.getX() - 1, from.getY() - 1, from.getZ()));
		return positions;
	}
	
	/**
	 * Returns a {@link Optional} {@link Position} of a random traversable tile.
	 * @param from       The position moving from.
	 * @param size       The size of the mob attempting to traverse.
	 * @param exceptions The exceptions of traversable positions.
	 * @return A random traversable position.
	 */
	public Optional<Position> getRandomTraversableTile(Position from, int size, Position... exceptions) {
		List<Position> pos = getNearbyTraversableTiles(from, size).stream().filter(p -> {
			for(Position e : exceptions) {
				if(p.equals(e))
					return false;
			}
			return true;
		}).collect(Collectors.toList());
		if(pos.isEmpty())
			return Optional.empty();
		return Optional.of(RandomUtils.random(pos));
	}
	
	/**
	 * Returns a {@link List} of positions that are traversable from the
	 * specified position depending on a direction.
	 * Used for NPC movements as they are based on a straight line.
	 * @param from The position.
	 * @param size The size of the mob attempting to traverse.
	 * @return A {@link List} of positions.
	 */
	public List<Position> getNonDiagonalNearbyTraversableTiles(Position from, int size) {
		List<Position> positions = new LinkedList<>();
		if(isTraversableNorth(from.getZ(), from.getX(), from.getY(), size))
			positions.add(new Position(from.getX(), from.getY() + 1, from.getZ()));
		if(isTraversableSouth(from.getZ(), from.getX(), from.getY(), size))
			positions.add(new Position(from.getX(), from.getY() - 1, from.getZ()));
		if(isTraversableEast(from.getZ(), from.getX(), from.getY(), size))
			positions.add(new Position(from.getX() + 1, from.getY(), from.getZ()));
		if(isTraversableWest(from.getZ(), from.getX(), from.getY(), size))
			positions.add(new Position(from.getX() - 1, from.getY(), from.getZ()));
		return positions;
	}
	
	/**
	 * Returns a {@link List} of position that are settable from the
	 * specified position depending on the leader's and follower's entity sizes.
	 * @param from         the position.
	 * @param leaderSize   the leader's entity size.
	 * @param followerSize the follower's entity size.
	 * @return A {@link List} of positions.
	 */
	public List<Position> getSurroundedTraversableTiles(Position from, int leaderSize, int followerSize) {
		List<Position> positions = new LinkedList<>();
		//north
		for(int x = from.getX() - 1; x < from.getX() + leaderSize; x++) {
			Direction d = Direction.fromDeltas(Position.delta(new Position(x, from.getY() + (leaderSize), from.getZ()), from));
			Direction d2 = Direction.fromDeltas(Position.delta(from, new Position(x, from.getY() + (leaderSize), from.getZ())));
			if(isTraversable(new Position(x, from.getY() + (leaderSize), from.getZ()), d, followerSize) && isTraversable(from, d2, followerSize)) {
				positions.add(new Position(x, from.getY() + (leaderSize), from.getZ()));
			}
		}
		//south
		for(int x = from.getX() - 1; x < from.getX() + leaderSize; x++) {
			Direction d = Direction.fromDeltas(Position.delta(new Position(x, from.getY() - ((followerSize - 1) + 1), from.getZ()), from));
			Direction d2 = Direction.fromDeltas(Position.delta(from, new Position(x, from.getY() - ((followerSize - 1) + 1), from.getZ())));
			if(isTraversable(new Position(x, from.getY() - ((followerSize - 1) + 1), from.getZ()), d, followerSize) && isTraversable(from, d2, followerSize)) {
				positions.add(new Position(x, from.getY() - ((followerSize - 1) + 1), from.getZ()));
			}
		}
		//west
		for(int y = from.getY() - 1; y < from.getY() + leaderSize; y++) {
			Direction d = Direction.fromDeltas(Position.delta(new Position(from.getX() - ((followerSize - 1) + 1), y, from.getZ()), from));
			Direction d2 = Direction.fromDeltas(Position.delta(from, new Position(from.getX() - ((followerSize - 1) + 1), y, from.getZ())));
			if(isTraversable(new Position(from.getX() - ((followerSize - 1) + 1), y, from.getZ()), d, followerSize) && isTraversable(from, d2, followerSize)) {
				positions.add(new Position(from.getX() - ((followerSize - 1) + 1), y, from.getZ()));
			}
		}
		//east
		for(int y = from.getY() - 1; y < from.getY() + leaderSize; y++) {
			Direction d = Direction.fromDeltas(Position.delta(new Position(from.getX() + (leaderSize - 1) + 1, y, from.getZ()), from));
			Direction d2 = Direction.fromDeltas(Position.delta(from, new Position(from.getX() + (leaderSize - 1) + 1, y, from.getZ())));
			if(isTraversable(new Position(from.getX() + (leaderSize - 1) + 1, y, from.getZ()), d, followerSize) && isTraversable(from, d2, followerSize)) {
				positions.add(new Position(from.getX() + (leaderSize - 1) + 1, y, from.getZ()));
			}
		}
		return positions;
	}
	
}