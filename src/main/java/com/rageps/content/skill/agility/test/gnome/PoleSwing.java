package com.rageps.content.skill.agility.test.gnome;

import com.rageps.content.skill.agility.test.obstacle.Obstacle;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.move.ForcedMovement;
import com.rageps.world.entity.actor.move.ForcedMovementDirection;
import com.rageps.world.entity.actor.move.ForcedMovementManager;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 10-8-2017.
 */
public final class PoleSwing extends Obstacle {
	
	public PoleSwing(Position end) {
		super(new Position[]{new Position(2485, 3418, 3), new Position(2486, 3418, 3), new Position(2487, 3418, 3)}, end, 11784, 85, 25, 0);
	}
	
	@Override
	public void initialize(Player player) {
		final ForcedMovement[] movements = new ForcedMovement[]{new ForcedMovement(player), // walk
				new ForcedMovement(player), // jump
				new ForcedMovement(player), // swing
				new ForcedMovement(player), // swing new pole + end
		};
		
		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(1, () -> {
			movements[0].setFirst(player.getPosition());
			movements[0].setSecond(new Position(player.getPosition().getX(), 3421, 3));
			movements[0].setSecondSpeed(45);
			movements[0].setDirection(ForcedMovementDirection.NORTH);
			movements[0].setAnimation(animation);
			movements[0].setTimer(1);
			ForcedMovementManager.submit(player, movements[0]);
		});
		seq.connect(2, () -> {
			movements[1].setFirst(new Position(player.getPosition().getX(), 3421, 3));
			movements[1].setSecond(new Position(player.getPosition().getX(), 3425, 3));
			movements[1].setSecondSpeed(30);
			movements[1].setDirection(ForcedMovementDirection.NORTH);
			movements[1].setAnimation(11785);
			ForcedMovementManager.submit(player, movements[1]);
		});
		seq.connect(1, () -> player.animation(new Animation(11789)));
		seq.connect(3, () -> {
			movements[2].setFirst(new Position(player.getPosition().getX(), 3425, 3));
			movements[2].setSecond(new Position(player.getPosition().getX(), 3429, 3));
			movements[2].setSecondSpeed(50);
			movements[2].setDirection(ForcedMovementDirection.NORTH);
			movements[2].setAnimation(null);
			ForcedMovementManager.submit(player, movements[2]);
		});
		seq.connect(5, () -> {
			movements[3].setFirst(new Position(player.getPosition().getX(), 3429, 3));
			movements[3].setSecond(new Position(player.getPosition().getX(), 3432, 3));
			movements[3].setSecondSpeed(55);
			movements[3].setAnimation(null);
			movements[3].setDirection(ForcedMovementDirection.NORTH);
			ForcedMovementManager.submit(player, movements[3]);
		});
		seq.start();
	}
}
