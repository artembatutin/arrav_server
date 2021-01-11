package com.rageps.world.entity.actor.mob;

import com.rageps.task.Task;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.World;
import com.rageps.world.entity.actor.move.path.Path;
import com.rageps.world.entity.region.TraversalMap;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Direction;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * An implementation of a {@link Task} that handles randomized {@link Mob} movements.
 * @author Artem Batutin
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
		if(mobs.isEmpty())
			return;
		int size = mobs.size();
		int count = RandomUtils.inclusive(size / 9);
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
					Path pathHome = World.getSmartPathfinder().find(mob.getPosition(), mob.getOriginalPosition(), mob.size());
					if(!pathHome.getMoves().isEmpty() && pathHome.isPossible())
						mob.getMovementQueue().walk(pathHome.getMoves());
				} else {
					Direction dir = Direction.random();
					Position movedPos = mob.getPosition().move(dir);
					boolean traversable = TraversalMap.isTraversable(mob.getPosition(), mob.getMovementCoordinator().getBoundary(), dir, mob.size());
					if(traversable) {
						Path pathHome = World.getSimplePathfinder().find(mob, movedPos);
						if(pathHome.isPossible())
							mob.getMovementQueue().walk(pathHome.getMoves());
						else
							break;
					}
				}
			}
		}
	}
	
	ObjectList<Mob> getMobs() {
		return mobs;
	}
}
