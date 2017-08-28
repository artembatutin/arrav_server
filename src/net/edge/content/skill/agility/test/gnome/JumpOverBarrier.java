package net.edge.content.skill.agility.test.gnome;

import net.edge.content.skill.agility.test.obstacle.Obstacle;
import net.edge.task.LinkedTaskSequence;
import net.edge.world.Animation;
import net.edge.world.entity.actor.move.ForcedMovement;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

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
