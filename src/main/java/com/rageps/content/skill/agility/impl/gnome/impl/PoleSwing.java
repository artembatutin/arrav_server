package com.rageps.content.skill.agility.impl.gnome.impl;

import com.rageps.content.skill.agility.obstacle.ObstacleActivity;
import com.rageps.content.skill.agility.obstacle.ObstacleType;
import com.rageps.content.skill.agility.obstacle.impl.Movable;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.world.entity.actor.move.ForcedMovement;
import com.rageps.world.entity.actor.move.ForcedMovementDirection;
import com.rageps.world.entity.actor.move.ForcedMovementManager;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Animation;

import java.util.stream.IntStream;

/**
 * Holds functionality for the pole swing obstacle.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class PoleSwing extends ObstacleActivity {

	/**
	 * Constructs a new {@link Movable} Obstacle Activity.
	 * @param player the player who's attempting the pole swing..
	 */
	public PoleSwing(Player player) {
		super(new Position(player.getPosition().getX(), 3418, 3), new Position(player.getPosition().getX(), 3432, 3), ObstacleType.POLE_SWING.getAnimation(), 85, 25);
	}

	@Override
	public boolean canExecute(Player player) {
		if(!IntStream.rangeClosed(2485, 2487).anyMatch(i -> player.getPosition().getX() == i)) {
			player.message("You can't cross this obstacle from here!");
			return false;
		}
		return true;
	}

	@Override
	public void onSubmit(Player player) {
		final ForcedMovement[] movements = new ForcedMovement[]{new ForcedMovement(player), // walk
				new ForcedMovement(player), // jump
				new ForcedMovement(player), // swing
				new ForcedMovement(player), // swing new pole + end
		};

		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(1, () -> {
			movements[0].setFirst(getStart());
			movements[0].setSecond(new Position(player.getPosition().getX(), 3421, 3));
			movements[0].setSecondSpeed(45);
			movements[0].setDirection(ForcedMovementDirection.NORTH);
			movements[0].setAnimation(getAnimation());
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
