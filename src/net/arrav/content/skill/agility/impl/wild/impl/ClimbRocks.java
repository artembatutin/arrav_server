package net.arrav.content.skill.agility.impl.wild.impl;

import net.arrav.content.skill.agility.obstacle.ObstacleActivity;
import net.arrav.content.skill.agility.obstacle.ObstacleType;
import net.arrav.task.LinkedTaskSequence;
import net.arrav.world.Animation;
import net.arrav.world.Direction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

import java.util.stream.IntStream;

public final class ClimbRocks extends ObstacleActivity {

	public ClimbRocks(Player player) {
		super(new Position(player.getPosition().getX(), 3937), new Position(player.getPosition().getX(), 3933), ObstacleType.ROCKS.getAnimation(), 52, 0);
	}

	@Override
	public boolean canExecute(Player player) {
		if(!IntStream.rangeClosed(2993, 2995).anyMatch(i -> player.getPosition().getX() == i)) {
			player.message("You can't cross this obstacle from here!");
			return false;
		}
		return true;
	}

	@Override
	public void onSubmit(Player player) {
		LinkedTaskSequence seq = new LinkedTaskSequence();

		seq.connect(1, () -> player.faceDirection(Direction.SOUTH));

		seq.connect(1, () -> player.animation(new Animation(3378)));

		seq.connect(6, () -> {
			player.animation(null);
			player.move(new Position(player.getPosition().getX(), 3933));
		});
		seq.start();
	}
}
