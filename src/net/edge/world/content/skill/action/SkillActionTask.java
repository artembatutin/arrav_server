package net.edge.world.content.skill.action;

import net.edge.task.Task;
import net.edge.world.model.node.NodeState;
import net.edge.world.model.node.entity.player.Player;

import java.util.Optional;

public final class SkillActionTask extends Task {
	
	/**
	 * The player this task is running for.
	 */
	private final Player player;
	
	/**
	 * The skill action we're currently handling.
	 */
	private final SkillAction action;
	
	/**
	 * The flag which determines if this action was executed.
	 */
	private boolean executed = true;
	
	/**
	 * The counter that determines how many ticks have passed.
	 */
	private int counter;
	
	/**
	 * The counter that determines how many ticks have passed for the next animation.
	 */
	private int animationCounter;
	
	/**
	 * Constructs a new {@link SkillActionTask}.
	 * @param action {@link #action}.
	 */
	public SkillActionTask(SkillAction action) {
		super(1, false);
		this.action = action;
		this.player = action.getPlayer();
	}
	
	@Override
	public void onSubmit() {
		if(!action.init()) {
			executed = false;
			this.cancel();
			return;
		}
		if(!action.canRun(this)) {
			executed = false;
			this.cancel();
			return;
		}
		if(action.instant()) {
			if(!player.getSkillActionTask().isPresent()) {
				this.cancel();
				return;
			}
			if(!action.canExecute()) {
				this.cancel();
				return;
			}
			action.execute(this);
			action.position.ifPresent(player::facePosition);
			action.animation().ifPresent(player::animation);
			counter = 0;
			animationCounter = 0;
		}
		player.getMovementQueue().reset();
		action.startAnimation().ifPresent(player::animation);
		action.onSubmit();
	}
	
	@Override
	public void execute() {
		if(player.getState() != NodeState.ACTIVE) {
			this.cancel();
			return;
		}
		if(!player.getSkillActionTask().isPresent()) {
			this.cancel();
			return;
		}
		
		if(player.getSkillActionTask().isPresent()) {
			action.onSequence(this);
		}
		
		action.animationDelay().ifPresent(delay -> {
			if(animationCounter++ >= delay) {
				action.animation().ifPresent(player::animation);
				animationCounter = 0;
			}
		});
		
		if(++counter >= action.delay()) {
			if(!player.getSkillActionTask().isPresent()) {
				this.cancel();
				return;
			}
			
			if(!action.canRun(this)) {
				this.cancel();
				return;
			}
			
			if(!action.canExecute()) {
				this.cancel();
				return;
			}
			action.execute(this);
			action.position.ifPresent(player::facePosition);
			if(!action.animationDelay().isPresent()) {
				action.animation().ifPresent(player::animation);
			}
			counter = 0;
		}
	}
	
	@Override
	public void onCancel() {
		if(executed) {
			action.onStop();
		}
		player.setSkillAction(Optional.empty());
	}
	
	public final SkillAction getAction() {
		return action;
	}
}
