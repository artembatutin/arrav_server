package net.edge.world.node.entity.move.path;

import net.edge.locale.Position;
import net.edge.world.node.region.TraversalMap;

/**
 * Represents a {@code PathFinder} which is meant to be used to check projectiles passage in a straight line.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class SimplePathChecker extends PathFinder {
	@Override
	public Path find(Position start, Position end, int size) {
		return new Path(null);//Empty path.
	}
	
	/**
	 * Determines if the path can be crossed by projectile from {@link Position start} to {@link Position} end.
	 * @param start The start Position.
	 * @param end   The end Position.
	 * @return {@code true} if the projectile can pass, {@code false} otherwise.
	 */
	public boolean checkProjectile(Position start, Position end) {
		return check(start, end, 1, true);
	}
	
	/**
	 * Determines if the path can be crossed from {@link Position start} to {@link Position} end.
	 * @param start The start Position.
	 * @param end   The end Position.
	 * @param size  The size of the entity.
	 * @return {@code true} if the projectile can pass, {@code false} otherwise.
	 */
	public boolean checkLine(Position start, Position end, int size) {
		return check(start, end, size, false);
	}
	
	/**
	 * Determines if the projectile can reach it's destination.
	 * @param start      The projectile's starting Position.
	 * @param end        The projectile's ending Position.
	 * @param projectile The condition if the check is meant for projectiles.
	 * @return {@code true} if the projectile can reach it's destination, {@code false} otherwise.
	 */
	public boolean check(Position start, Position end, int size, boolean projectile) {
		int deltaX = end.getX() - start.getX();
		int deltaY = end.getY() - start.getY();
		double error = 0;
		final double deltaError = Math.abs((deltaY) / (deltaX == 0 ? ((double) deltaY) : ((double) deltaX)));
		int x = start.getX();
		int y = start.getY();
		int z = start.getZ();
		int pX = x;
		int pY = y;
		boolean incrX = start.getX() < end.getX();
		boolean incrY = start.getY() < end.getY();
		while(true) {
			if(x != end.getX()) {
				x += (incrX ? 1 : -1);
			}
			if(y != end.getY()) {
				error += deltaError;
				
				if(error >= 0.5) {
					y += (incrY ? 1 : -1);
					error -= 1;
				}
			}
			if(projectile ? !projectileCheck(new Position(x, y, z), new Position(pX, pY, z)) : !traversable(new Position(x, y, z), new Position(pX, pY, z), size)) {
				return false;
			}
			if(incrX && incrY && x >= end.getX() && y >= end.getY()) {
				break;
			} else if(!incrX && !incrY && x <= end.getX() && y <= end.getY()) {
				break;
			} else if(!incrX && incrY && x <= end.getX() && y >= end.getY()) {
				break;
			} else if(incrX && !incrY && x >= end.getX() && y <= end.getY()) {
				break;
			}
			pX = x;
			pY = y;
		}
		return true;
	}
	
}