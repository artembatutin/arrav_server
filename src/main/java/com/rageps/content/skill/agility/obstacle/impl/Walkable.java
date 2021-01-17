package com.rageps.content.skill.agility.obstacle.impl;

import com.rageps.content.skill.agility.obstacle.ObstacleActivity;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.task.Task;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Animation;

/**
 * The forced walkable obstacle action which will walk a player starting from the start position
 * to the destination with it's respective animation by setting them to the players animation indexes.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class Walkable extends ObstacleActivity {
	
	/**
	 * Constructs a new {@link Walkable} Obstacle Activity.
	 * @param start {@link #getStart()}.
	 * @param destination {@link #getDestination()}.
	 * @param animation {@link #getAnimation()}.
	 * @param requirement {@link #getRequirement()}.
	 * @param experience {@link #getExperience()}.
	 */
	public Walkable(Position start, Position destination, Animation animation, int requirement, double experience) {
		super(start, destination, animation, requirement, experience);
	}
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}
	
	@Override
	public void onSubmit(Player player) {
		int animation = getAnimation().getId();
		if(player.getPosition().same(getStart())) {
			player.setWalkIndex(animation);
			player.setRunIndex(animation);
			player.setStandIndex(animation);
			player.setTurn180Index(animation);
			player.setTurn90CWIndex(animation);
			player.setTurn90CCWIndex(animation);
			player.setTurnIndex(animation);
			player.updateAppearance();
			player.getMovementQueue().walk(getDestination());
		} else {
			LinkedTaskSequence sequence = new LinkedTaskSequence();
			sequence.connect(1, () -> player.move(getStart()));
			sequence.connect(2, () -> {
				player.setWalkIndex(animation);
				player.setRunIndex(animation);
				player.setStandIndex(animation);
				player.setTurn180Index(animation);
				player.setTurn90CWIndex(animation);
				player.setTurn90CCWIndex(animation);
				player.setTurnIndex(animation);
				player.updateAppearance();
				player.getMovementQueue().walk(getDestination());
			});
			sequence.start();
		}
		/*else if(travelBack() && player.getPosition().same(getDestination())) {
			player.getMovementQueue().walk(getStart());
		}*/
	}
	
	@Override
	public void execute(Player player, Task t) {

	}
	
	@Override
	public void onCancel(Player player) {
		int animation = -1;
		player.setWalkIndex(animation);
		player.setRunIndex(animation);
		player.setStandIndex(animation);
		player.setTurn180Index(animation);
		player.setTurn90CWIndex(animation);
		player.setTurn90CCWIndex(animation);
		player.setTurnIndex(animation);
		player.updateAppearance();
	}
}
