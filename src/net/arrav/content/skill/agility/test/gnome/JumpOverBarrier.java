package net.arrav.content.skill.agility.test.gnome;

import net.arrav.content.skill.agility.test.obstacle.Obstacle;
import net.arrav.task.LinkedTaskSequence;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.move.ForcedMovement;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 10-8-2017.
 */
public final class JumpOverBarrier extends Obstacle {
	
	/**
	 * The place the player jumps to.
	 */
	private final Position prestop;
	
	public JumpOverBarrier(Position start) {
		super(new Position[]{start}, new Position(2485, 3436, 0), 2923, 85, 25, 3);
		this.prestop = new Position(2485, 3434, 3);
	}
	
	@Override
	public void initialize(Player player) {
		LinkedTaskSequence seq = new LinkedTaskSequence();
		
		seq.connect(1, () -> {
			ForcedMovement movement = new ForcedMovement(player);
			movement.setSecond(prestop);
			movement.setAnimation(animation);
			movement.submit();
		});
		
		seq.connect(3, () -> {
			player.move(end);
			player.setTeleportStage(10);
			player.task(2, p -> p.setTeleportStage(0));
			player.animation(new Animation(2924));
		});
		seq.start();
	}
	
}
