package net.edge.world.content.skill.agility.obstacle.impl;

import net.edge.task.LinkedTaskSequence;
import net.edge.task.Task;
import net.edge.world.content.skill.agility.obstacle.ObstacleActivity;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.update.UpdateFlag;

/**
 * The forced walkable obstacle action which will walk a player starting from the start position
 * to the destination with it's respective animation by setting them to the players animation indexes.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class Walkable extends ObstacleActivity {
	
	/**
	 * Constructs a new {@link Walkable} Obstacle Activity.
	 * @param start       {@link #getStart()}.
	 * @param destination {@link #getDestination()}.
	 * @param animation   {@link #getAnimation()}.
	 * @param requirement {@link #getRequirement()}.
	 * @param experience  {@link #getExperience()}.
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
			player.message("same");
			player.setWalkIndex(animation);
			player.setRunIndex(animation);
			player.setStandIndex(animation);
			player.setTurn180Index(animation);
			player.setTurn90CWIndex(animation);
			player.setTurn90CCWIndex(animation);
			player.setTurnIndex(animation);
			player.getFlags().flag(UpdateFlag.APPEARANCE);
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
				player.getFlags().flag(UpdateFlag.APPEARANCE);
				player.getMovementQueue().walk(getDestination());
				player.message("walking");
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
		player.getFlags().flag(UpdateFlag.APPEARANCE);
	}
}
