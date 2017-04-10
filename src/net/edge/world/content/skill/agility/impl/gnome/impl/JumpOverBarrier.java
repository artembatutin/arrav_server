package net.edge.world.content.skill.agility.impl.gnome.impl;

import net.edge.task.LinkedTaskSequence;
import net.edge.world.content.skill.agility.obstacle.ObstacleType;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.content.skill.agility.obstacle.ObstacleActivity;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.move.ForcedMovement;

/**
 * Holds functionality for the jump over barrier obstacle.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class JumpOverBarrier extends ObstacleActivity {

	/**
	 * The place the player jumps to.
	 */
	private final Position prestop;

	/**
	 * Constructs a new {@link JumpOverBarrier}.
	 */
	public JumpOverBarrier() {
		super(new Position(2485, 3433, 3), new Position(2485, 3436, 0), ObstacleType.JUMP_OVER_BARRIER.getAnimation(), 85, 25);
		this.prestop = new Position(2485, 3434, 3);
	}

	@Override
	public void onSubmit(Player player) {
		LinkedTaskSequence seq = new LinkedTaskSequence();

		seq.connect(1, () -> {
			ForcedMovement movement = new ForcedMovement(player);
			movement.setSecond(prestop);
			movement.setAnimation(getAnimation());
			movement.submit();
		});

		seq.connect(4, () -> {
			player.move(getDestination());
		});
		seq.start();
	}

	@Override
	public void onCancel(Player player) {
		player.animation(new Animation(2924));
	}

}
