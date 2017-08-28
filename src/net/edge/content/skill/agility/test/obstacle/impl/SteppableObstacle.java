package net.edge.content.skill.agility.test.obstacle.impl;

import net.edge.content.skill.agility.test.obstacle.Obstacle;
import net.edge.task.Task;
import net.edge.world.entity.actor.move.ForcedMovement;
import net.edge.world.entity.actor.move.ForcedMovementManager;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 12-8-2017.
 */
public final class SteppableObstacle extends Obstacle {

	public final Position[] steppables;

	public static final int DELAY = 4;

	public int current;

	public SteppableObstacle(Position start, Position[] steppables, Position end, int requirement, double experience) {
		super(start, end, 769, requirement, experience, DELAY, true);
		this.steppables = steppables;
	}

	@Override
	public boolean findProperPosition(Player player) {
		return true;
	}

	private boolean hasPreviousSteppable() {
		return current > 0;
	}

	private Position previousSteppable() {
		if(current > steppables.length) {
			return steppables[current - 2];
		}
		return steppables[current - 1];
	}

	private Position nextSteppable() {
		if(current > steppables.length - 1) {
			return end;
		}
		return steppables[current++];
	}

	private boolean hasNextSteppable() {
		return current < steppables.length + 1;
	}

	public void start(Player player) {
		ForcedMovement movement = new ForcedMovement(player);
		movement.setFirst(hasPreviousSteppable() ? previousSteppable() : start[0]);
		movement.setSecond(nextSteppable());
		movement.setSecondSpeed(40);
		movement.setAnimation(animation);
		ForcedMovementManager.submit(player, movement);
	}

	@Override
	public void execute(Player player, Task t) {
		start(player);

		if(!hasNextSteppable()) {
			t.cancel();
		}
	}

}
