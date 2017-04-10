package net.edge.world.content.skill.agility.obstacle.impl;

import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.content.skill.agility.obstacle.ObstacleActivity;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.move.ForcedMovement;
import net.edge.world.model.node.entity.move.ForcedMovementManager;
import net.edge.world.model.node.entity.update.UpdateFlag;
import net.edge.task.Task;

/**
 * The forced steppable obstacle action which will walk a player starting from the start position
 * to the other steppable positions with it's respective animation by setting them to
 * the players animation indexes and finally to it's destination.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class Steppable extends ObstacleActivity {
	
	/**
	 * The steppables positions this player will walk to.
	 */
	private final Position[] steppables;
	
	/**
	 * The delay until this action is executed.
	 */
	private static final int DELAY = 4;
	
	/**
	 * The index of the current steppable this player should walk to.
	 */
	private int current;
	
	/**
	 * Constructs a new {@link Steppable} Obstacle Activity.
	 * @param start       {@link #getStart()}.
	 * @param steppables  {@link #steppables}.
	 * @param destination {@link #getDestination()}.
	 * @param animation   {@link #getAnimation()}.
	 * @param requirement {@link #getRequirement()}.
	 * @param experience  {@link #getExperience()}.
	 */
	public Steppable(Position start, Position[] steppables, Position destination, Animation animation, int requirement, double experience) {
		super(start, destination, animation, requirement, experience);
		this.steppables = steppables;
	}
	
	@Override
	public int getDelay() {
		return DELAY;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public boolean canExecute(Player player) {
		if(!player.getPosition().same(getStart())) {
			player.message("You can't cross this obstacle from this position.");
			return false;
		}
		return true;
	}
	
	private boolean hasPreviousSteppable() {
		return current > 0;
	}
	
	private Position previousSteppable() {
		if(current > steppables.length) {
			return steppables[current - 2];
		}
		return steppables[current - 1];
	}
	
	private Position nextSteppable() {
		if(current > steppables.length - 1) {
			return getDestination();
		}
		return steppables[current++];
	}
	
	private boolean hasNextSteppable() {
		return current < steppables.length + 1;
	}
	
	public void start(Player player) {
		ForcedMovement movement = new ForcedMovement(player);
		movement.setFirst(hasPreviousSteppable() ? previousSteppable() : getStart());
		movement.setSecond(nextSteppable());
		movement.setSecondSpeed(40);
		movement.setAnimation(getAnimation());
		ForcedMovementManager.submit(player, movement);
	}
	
	@Override
	public void execute(Player player, Task t) {
		start(player);
		if(!hasNextSteppable()) {
			t.cancel();
			return;
		}
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
		player.move(getDestination());
	}
	
	/**
	 * @return the steppables
	 */
	public Position[] getSteppables() {
		return steppables;
	}
	
}
