package net.edge.content.skill.action;

import net.edge.task.Task;
import net.edge.content.skill.SkillData;
import net.edge.world.locale.Position;
import net.edge.world.Animation;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * The skill action which can be used for skills.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @author Graham Edgecombe
 */
public abstract class SkillAction {
	
	/**
	 * The player this skill action is for.
	 */
	protected final Player player;
	
	/**
	 * The position this player should face.
	 */
	protected final Optional<Position> position;
	
	/**
	 * Constructs a new {@link SkillAction}.
	 * @param player   {@link #player}.
	 * @param position {@link #position}.
	 */
	public SkillAction(Player player, Optional<Position> position) {
		this.player = player;
		this.position = position;
	}
	
	/**
	 * Starts this skill action by submitting a new skill action task.
	 */
	public final void start() {
		if(player.getSkillActionTask().isPresent() && player.getSkillActionTask().get().getAction().isPrioritized() && !this.isPrioritized()) {
			getPlayer().message("You currently cannot do this.");
			return;
		}
		
		/** This will cancel previous skill, if you're mining and you try to fletch,
		 * mining will get stopped, and fletching will start.*/
		if(player.getSkillActionTask().isPresent() && !player.getSkillActionTask().get().getAction().skill().equals(this.skill())) {
			stop();
		}
		
		SkillActionTask task = new SkillActionTask(this);
		getPlayer().setSkillAction(task);
		World.get().submit(task);
	}
	
	/**
	 * Stops this skill action effectively.
	 */
	public final void stop() {
		SkillActionTask task = player.getSkillActionTask().get();
		
		task.setRunning(false);
		task.getAction().onStop();
		task.getAction().getPlayer().setSkillAction(Optional.empty());
	}
	
	/**
	 * Determines if this skill action can be ran.
	 * @param t the task to determine this for.
	 * @return {@code true} if it can, {@code false} otherwise.
	 */
	public boolean canRun(Task t) {
		return true;
	}
	
	/**
	 * The delay inbetween playing animations.
	 * @return the numerical value which determines the time to play the animation again.
	 */
	public OptionalInt animationDelay() {
		return OptionalInt.empty();
	}
	
	/**
	 * The delay intervals of this skill action in ticks.
	 * @return the delay intervals.
	 */
	public abstract int delay();
	
	/**
	 * Determines if this skill action should be executed instantly rather than
	 * after the delay.
	 * @return <true> if this skill action should be instant, <false> otherwise.
	 */
	public abstract boolean instant();
	
	/**
	 * Initializes this skill action and performs any pre-checks, <b>this method is only executed
	 * one<b>.
	 * @return <true> if the skill action can proceed, <false> otherwise.
	 */
	public abstract boolean init();
	
	/**
	 * Determines if this skill can be executed, <b>this method is executed
	 * every tick</b>
	 * @return <true> if this skill can be executed, <false> otherwise.
	 */
	public abstract boolean canExecute();
	
	/**
	 * The method executed when the delay has elapsed.
	 * @param t the task executing this skill action.
	 */
	public abstract void execute(Task t);
	
	/**
	 * The experience given from this skill action.
	 * @return the experience given.
	 */
	public abstract double experience();
	
	/**
	 * The skill that this skill action is for.
	 * @return the skill data.
	 */
	public abstract SkillData skill();
	
	/**
	 * The activity types which will disable this skill action.
	 * @return {@link Optional#empty} if non of the activities will disable this skill action,
	 * {@link Optional#of} an array of {@link ActivityManager.ActivityType}s which will disable this skill action.
	 */
	public Optional<ActivityManager.ActivityType[]> onDisable() {
		return Optional.empty();
	}
	
	/**
	 * The method executed when this skill action is submitted.
	 */
	public void onSubmit() {
		
	}
	
	/**
	 * The method executed each periodical sequence of this skill action.
	 */
	public void onSequence(Task t) {
		
	}
	
	/**
	 * The method executed when this skill action is stopped.
	 */
	public void onStop() {
		
	}
	
	/**
	 * The animation played periodically during this skill action.
	 * @return the animation played.
	 */
	public Optional<Animation> animation() {
		return Optional.empty();
	}
	
	/**
	 * The animation played instantly during this skill action.
	 * @return the animation played.
	 */
	public Optional<Animation> startAnimation() {
		return Optional.empty();
	}
	
	/**
	 * The priority determines if this skill can be overriden by another skill.
	 * @return <true> if the skill can be overriden by other skills, <false> otherwise.
	 */
	public abstract boolean isPrioritized();
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
}
