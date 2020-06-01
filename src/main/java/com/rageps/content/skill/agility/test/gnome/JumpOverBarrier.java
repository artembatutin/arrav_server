package com.rageps.content.skill.agility.test.gnome;

import com.rageps.content.skill.agility.test.obstacle.Obstacle;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.world.Animation;
import com.rageps.world.entity.actor.move.ForcedMovement;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;

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
