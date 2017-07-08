package net.edge.world;

import net.edge.content.combat.CombatType;
import net.edge.locale.Position;
import net.edge.net.packet.out.SendProjectile;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.region.RegionManager;

/**
 * A container representing a graphic propelled through the air by some sort of
 * spell, weapon, or other miscellaneous force.
 * @author lare96 <http://github.com/lare96>
 */
public final class Projectile {
	
	/**
	 * Magic combat projectile delays.
	 */
	public static final int[] MAGIC_DELAYS = {
			2,
			2,
			3,
			3,
			3,
			4,
			4,
			4,
			5,
			5,
			15
	};
	
	/**
	 * Ranged combat projectile delays.
	 */
	public static final int[] RANGED_DELAYS = {
			2,
			2,
			2,
			3,
			3,
			3,
			3,
			3,
			3,
			4,
			4
	};
	
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
	 * The time to travel in ticks.
	 */
	private final int travelTime;
	
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
	 * @param type         the combat type of this projectile
	 */
	public Projectile(Position start, Position end, int lockon, int projectileId, int speed, int delay, int startHeight, int endHeight, int curve, int instance, CombatType type) {
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
		int distance = (int) start.getDistance(end);
		if(type != null && type.equals(CombatType.MAGIC)) {
			this.travelTime = MAGIC_DELAYS[distance > 10 ? 10 : distance];
		} else {
			this.travelTime = RANGED_DELAYS[distance > 10 ? 10 : distance];
		}
		//this.travelTime = (int) ((delay + speed + start.getDistance(end) * 5) * .02857);
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
	 * @param type         the combat type of this projectile
	 */
	public Projectile(EntityNode source, EntityNode victim, int projectileId, int speed, int delay, int startHeight, int endHeight, int curve, CombatType type) {
		this(source.getCenterPosition(), victim.getCenterPosition(), (victim.isPlayer() ? -victim.getSlot() - 1 : victim.getSlot() + 1), projectileId, speed, delay, startHeight, endHeight, curve, source.getInstance(), type);
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
		this(source.getCenterPosition(), victim.getCenterPosition(), (victim.isPlayer() ? -victim.getSlot() - 1 : victim.getSlot() + 1), projectileId, speed, delay, startHeight, endHeight, curve, source.getInstance(), null);
	}
	
	/**
	 * Sends a projectile for everyone in the world based on the values in this
	 * container.
	 */
	public Projectile sendProjectile() {
		int x = start.getX() & 63;
		int y = start.getY() & 63;
		int regionId = start.getRegion();
		RegionManager m = World.getRegions();
		m.getRegion(regionId).getPlayers().forEach(p -> p.out(new SendProjectile(start, offset, speed, projectileId, startHeight, endHeight, lockon, delay)));
		if(y > 48) {
			//top part of region.
			if(m.exists(regionId + 1))
				m.getRegion(regionId + 1).getPlayers().forEach(p -> p.out(new SendProjectile(start, offset, speed, projectileId, startHeight, endHeight, lockon, delay)));
			if(x > 48) {
				//top-right of region.
				if(m.exists(regionId + 256))
					m.getRegion(regionId + 256).getPlayers().forEach(p -> p.out(new SendProjectile(start, offset, speed, projectileId, startHeight, endHeight, lockon, delay)));
				if(m.exists(regionId + 257))
					m.getRegion(regionId + 257).getPlayers().forEach(p -> p.out(new SendProjectile(start, offset, speed, projectileId, startHeight, endHeight, lockon, delay)));
			} else if(x < 16) {
				//top-left of region.
				if(m.exists(regionId - 256))
					m.getRegion(regionId - 256).getPlayers().forEach(p -> p.out(new SendProjectile(start, offset, speed, projectileId, startHeight, endHeight, lockon, delay)));
				if(m.exists(regionId - 255))
					m.getRegion(regionId - 255).getPlayers().forEach(p -> p.out(new SendProjectile(start, offset, speed, projectileId, startHeight, endHeight, lockon, delay)));
			}
		} else if(y < 16) {
			//bottom part of region.
			if(m.exists(regionId - 1))
				m.getRegion(regionId - 1).getPlayers().forEach(p -> p.out(new SendProjectile(start, offset, speed, projectileId, startHeight, endHeight, lockon, delay)));
			if(x > 48) {
				//bottom-right of region.
				if(m.exists(regionId + 256))
					m.getRegion(regionId + 256).getPlayers().forEach(p -> p.out(new SendProjectile(start, offset, speed, projectileId, startHeight, endHeight, lockon, delay)));
				if(m.exists(regionId + 255))
					m.getRegion(regionId + 255).getPlayers().forEach(p -> p.out(new SendProjectile(start, offset, speed, projectileId, startHeight, endHeight, lockon, delay)));
			} else if(x < 16) {
				//bottom-left of region.
				if(m.exists(regionId - 256))
					m.getRegion(regionId - 256).getPlayers().forEach(p -> p.out(new SendProjectile(start, offset, speed, projectileId, startHeight, endHeight, lockon, delay)));
				if(m.exists(regionId - 257))
					m.getRegion(regionId - 257).getPlayers().forEach(p -> p.out(new SendProjectile(start, offset, speed, projectileId, startHeight, endHeight, lockon, delay)));
			}
		}
		return this;
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
	
	/**
	 * Gets the travel time of this projectile depending on it's configuration.
	 * @return the travel time in ticks.
	 */
	public int getTravelTime() {
		return travelTime;
	}
}
