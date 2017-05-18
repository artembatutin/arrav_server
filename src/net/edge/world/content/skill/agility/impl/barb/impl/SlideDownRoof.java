package net.edge.world.content.skill.agility.impl.barb.impl;

import net.edge.task.LinkedTaskSequence;
import net.edge.world.content.skill.agility.obstacle.ObstacleActivity;
import net.edge.world.content.skill.agility.obstacle.ObstacleType;
import net.edge.world.locale.Position;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.move.ForcedMovement;
import net.edge.world.node.entity.move.ForcedMovementManager;
import net.edge.world.node.entity.player.Player;

import java.util.stream.IntStream;

public final class SlideDownRoof extends ObstacleActivity {

	public SlideDownRoof(Player player) {
		super(new Position(2539, player.getPosition().getY(), 2), new Position(2543, player.getPosition().getY(), 0), ObstacleType.SLIDE_ROOF.getAnimation(), 90, 615);
	}

	@Override
	public boolean canExecute(Player player) {
		if(!IntStream.rangeClosed(3552, 3554).anyMatch(i -> player.getPosition().getY() == i)) {
			player.message("You can't cross this obstacle from here!");
			return false;
		}
		return true;
	}

	@Override
	public void onSubmit(Player player) {
		final ForcedMovement[] movements = new ForcedMovement[]{new ForcedMovement(player), // first slide
				new ForcedMovement(player), // second slide
				new ForcedMovement(player), // jump down
		};

		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(2, () -> {
			player.animation(new Animation(2588, 20));
			movements[0].setFirst(getStart());
			movements[0].setSecond(new Position(2540, player.getPosition().getY(), 1));
			movements[0].setSecondSpeed(60);
			movements[0].setTimer(0);
			movements[0].setAnimation(11792);
			ForcedMovementManager.submit(player, movements[0]);
		});
		seq.connect(2, () -> {
			movements[1].setFirst(new Position(2540, player.getPosition().getY(), 1));
			movements[1].setSecond(new Position(2542, player.getPosition().getY(), 1));
			movements[1].setSecondSpeed(20);
			movements[1].setAnimation(11790);
			movements[1].setTimer(2);
			ForcedMovementManager.submit(player, movements[1]);
		});
		seq.connect(2, () -> {
			movements[2].setFirst(new Position(2542, player.getPosition().getY(), 1));
			movements[2].setSecond(getDestination());
			movements[2].setSecondSpeed(20);
			movements[2].setAnimation(11791);
			movements[2].setTimer(0);
			ForcedMovementManager.submit(player, movements[2]);
		});
		seq.start();
	}

}
