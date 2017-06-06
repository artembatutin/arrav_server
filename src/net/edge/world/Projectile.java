package net.edge.world;

import net.edge.locale.Position;
import net.edge.world.node.entity.EntityNode;

/**
 * A container representing a graphic propelled through the air by some sort of
 * spell, weapon, or other miscellaneous force.
 * @author lare96 <http://github.com/lare96>
 */
public final class Projectile {
	
	/**
	 * The starting position of the projectile.
	 */
	private final Position start;
	
	/**
	 * The offset position of the projectile.
	 */
	private final Position offset;
	
	/**
	 * The speed of the projectile.
	 */
	private final int speed;
	
	/**
	 * The id of the projectile.
	 */
	private final int projectileId;
	
	/**
	 * The starting height of the projectile.
	 */
	private final int startHeight;
	
	/**
	 * The ending height of the projectile.
	 */
	private final int endHeight;
	
	/**
	 * The lock on value of the projectile.
	 */
	private final int lockon;
	
	/**
	 * The delay of the projectile.
	 */
	private final int delay;
	
	/**
	 * The curve angle of the projectile.
	 */
	private final int curve;
	
	/**
	 * The instance on which this projectile is being active.
	 */
	private final int instance;
	
	/**
	 * Creates a new {@link Projectile}.
	 * @param start        the starting position of the projectile.
	 * @param end          the ending position of the projectile.
	 * @param lockon       the lock on value of the projectile.
	 * @param projectileId the id of the projectile.
	 * @param speed        the speed of the projectile.
	 * @param delay        the delay of the projectile.
	 * @param startHeight  the starting height of the projectile.
	 * @param endHeight    the ending height of the projectile.
	 * @param curve        the curve angle of the projectile.
	 * @param instance     the world instance on which the projectile is active.
	 */
	public Projectile(Position start, Position end, int lockon, int projectileId, int speed, int delay, int startHeight, int endHeight, int curve, int instance) {
		this.start = start;
		this.offset = new Position((end.getX() - start.getX()), (end.getY() - start.getY()));
		this.lockon = lockon;
		this.projectileId = projectileId;
		this.delay = delay;
		this.speed = speed;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.curve = curve;
		this.instance = instance;
	}
	
	/**
	 * Creates a new {@link Projectile} based on the difference between the
	 * {@code source} and {@code victim}.
	 * @param source       the character that is firing this projectile.
	 * @param victim       the victim that this projectile is being fired at.
	 * @param projectileId the id of the projectile.
	 * @param speed        the speed of the projectile.
	 * @param delay        the delay of the projectile.
	 * @param startHeight  the starting height of the projectile.
	 * @param endHeight    the ending height of the projectile.
	 * @param curve        the curve angle of the projectile.
	 */
	public Projectile(EntityNode source, EntityNode victim, int projectileId, int speed, int delay, int startHeight, int endHeight, int curve) {
		this(source.getCenterPosition(), victim.getCenterPosition(), (victim.isPlayer() ? -victim.getSlot() - 1 : victim.getSlot() + 1), projectileId, speed, delay, startHeight, endHeight, curve, source.getInstance());
	}
	
	/**
	 * Sends a projectile for everyone in the world based on the values in this
	 * container.
	 */
	public void sendProjectile() {
		World.getRegions().getSurroundingRegions(start).forEach(r -> r.getPlayers().forEach((i, p) -> p.getMessages().sendProjectile(start, offset, speed, projectileId, startHeight, endHeight, lockon, delay)));
	}
	
	/**
	 * Gets the starting position of the projectile.
	 * @return the starting position of the projectile.
	 */
	public Position getStart() {
		return start;
	}
	
	/**
	 * Gets the offset position of the projectile.
	 * @return the offset position of the projectile.
	 */
	public Position getOffset() {
		return offset;
	}
	
	/**
	 * Gets the speed of the projectile.
	 * @return the speed of the projectile.
	 */
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * Gets the id of the projectile.
	 * @return the id of the projectile.
	 */
	public int getProjectileId() {
		return projectileId;
	}
	
	/**
	 * Gets the starting height of the projectile.
	 * @return the starting height of the projectile.
	 */
	public int getStartHeight() {
		return startHeight;
	}
	
	/**
	 * Gets the ending height of the projectile.
	 * @return the ending height of the projectile
	 */
	public int getEndHeight() {
		return endHeight;
	}
	
	/**
	 * Gets the lock on value of the projectile.
	 * @return the lock on value of the projectile.
	 */
	public int getLockon() {
		return lockon;
	}
	
	/**
	 * Gets the delay of the projectile.
	 * @return the delay of the projectile.
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * Gets the curve angle of the projectile.
	 * @return the curve angle of the projectile.
	 */
	public int getCurve() {
		return curve;
	}
	
	/**
	 * Gets the instance on which this projectile is being active.
	 * @return the instance of this projectile.
	 */
	public int getInstance() {
		return instance;
	}
}
