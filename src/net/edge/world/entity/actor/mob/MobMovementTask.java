package net.edge.world.entity.actor.mob;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Direction;
import net.edge.world.World;
import net.edge.world.entity.actor.move.path.Path;
import net.edge.world.entity.region.TraversalMap;
import net.edge.world.locale.Boundary;
import net.edge.world.locale.Position;

/**
 * An implementation of a {@link Task} that handles randomized {@link Mob} movements.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class MobMovementTask extends Task {
	
	/**
	 * The Queue of Mobs.
	 */
	private final ObjectList<Mob> mobs = new ObjectArrayList<>();
	
	/**
	 * Creates a new {@link MobMovementTask}.
	 */
	public MobMovementTask() {
		super(2, false);
	}
	
	@Override
	protected void execute() {
		int size = mobs.size();
		int count = RandomUtils.inclusive(size);
		for(int iterator = 0; iterator < count; iterator++) {
			Mob mob = mobs.get(RandomUtils.inclusive(size - 1));
			if(mob == null)
				break;
			if(!mob.active())
				break;
			MobMovementCoordinator move = mob.getMovementCoordinator();
			if(!move.isCoordinate() || mob.getCombat().isAttacking() || mob.getCombat().isUnderAttack()) {
				return;
			}
			if(mob.getMovementQueue().isMovementDone()) {
				if(!move.getBoundary().inside(mob.getPosition(), mob.size())) {
					Path pathHome = mob.getAStarPathFinder().find(mob.getOriginalPosition());
					if(pathHome != null && pathHome.isPossible())
						mob.getMovementQueue().walk(pathHome.getMoves());
				} else {
					Direction dir = Direction.random();
					int random_x = mob.toMob().getPosition().getX() + randomSteps(mob.size());
					int random_y = mob.toMob().getPosition().getY() + randomSteps(mob.size());
					Position generated_random_position = new Position(random_x, random_y);
					Boundary boundary = new Boundary(generated_random_position, mob.size());
					boolean traversable = TraversalMap.isTraversable(generated_random_position, boundary, dir, mob.size());
					if(traversable) {
						Path pathHome = World.getSimplePathFinder().find(mob, generated_random_position);
						if(pathHome.isPossible())
							mob.getMovementQueue().walk(pathHome.getMoves());
						else
							break;
					}
				}
			}
		}
	}
	
	private int randomSteps(int size) {
		return RandomUtils.inclusive(0, size);
	}
	
	ObjectList<Mob> getMobs() {
		return mobs;
	}
}
