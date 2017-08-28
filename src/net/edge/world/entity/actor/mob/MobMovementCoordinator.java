package net.edge.world.entity.actor.mob;

import net.edge.world.Direction;
import net.edge.world.locale.Boundary;

/**
 * The movement coordinator that makes all {@link Mob}s pseudo-randomly move
 * within a radius of their original positions.
 * @author lare96 <http://github.com/lare96>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MobMovementCoordinator {
	
	/**
	 * The NPC that this coordinator is dedicated to.
	 */
	private final Mob mob;
	
	/**
	 * The facing direction of the mob.
	 */
	private Direction facingDirection = Direction.NONE;
	
	/**
	 * Determines if the NPC is flagged to walk randomly.
	 */
	private boolean coordinate;
	
	/**
	 * The boundary of this NPC to walk on.
	 */
	private Boundary boundary;
	
	/**
	 * Creates a new {@link MobMovementCoordinator}.
	 * @param mob the NPC that this coordinator is dedicated to.
	 */
	MobMovementCoordinator(Mob mob) {
		this.mob = mob;
	}
	
	/**
	 * Gets the facing direction of the NPC. By default {@link Direction#NONE} if
	 * the NPC is not standing.
	 * @return the NPC's facing direction.
	 */
	public Direction getFacing() {
		return facingDirection;
	}
	
	/**
	 * Sets the value for {@link MobMovementCoordinator#facingDirection}.
	 * @param facingDirection the new value to set.
	 */
	public void setFacingDirection(Direction facingDirection) {
		this.facingDirection = facingDirection;
	}
	
	/**
	 * Determines if the NPC is flagged to walk randomly.
	 * @return {@code true} if the NPC will walk randomly, {@code false}
	 * otherwise.
	 */
	public boolean isCoordinate() {
		return coordinate;
	}
	
	/**
	 * Sets the value for {@link MobMovementCoordinator#boundary}.
	 * @param boundary the new value to set.
	 */
	public void setBoundary(Boundary boundary) {
		if(this.boundary != null || boundary == null)
			return;
		this.boundary = boundary;
	}
	
	/**
	 * Sets the boundary depending on a given value radius.
	 * @param radius the radius value.
	 */
	public void setRadius(int radius) {
		setBoundary(new Boundary(mob.getPosition().move(-radius, -radius), radius * 2));
	}
	
	/**
	 * Gets the boundary of this NPC walking.
	 * @return this mob's boundary.
	 */
	public Boundary getBoundary() {
		return boundary;
	}
	
	/**
	 * Sets the value for {@link MobMovementCoordinator#coordinate}.
	 * @param coordinate the new value to set.
	 */
	public void setCoordinate(boolean coordinate) {
		this.coordinate = coordinate;
	}
	
}