package com.rageps.content.skill.agility.test;

import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.Skills;
import com.rageps.content.skill.action.SkillAction;
import com.rageps.content.skill.agility.test.obstacle.Obstacle;
import com.rageps.task.Task;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.object.GameObject;

import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-8-2017.
 */
public abstract class Agility extends SkillAction {
	
	/**
	 * Determines if the player was running.
	 */
	public final boolean running;
	
	/**
	 * The obstacle the player is crossing.
	 */
	protected final Obstacle crossing;
	
	/**
	 * The object that we're crossing.
	 */
	public final GameObject object;
	
	public final boolean travelback;
	
	/**
	 * Constructs a new {@link SkillAction}.
	 * @param player {@link #player}.
	 * @param object {@link #object}.
	 * @param crossing {@link #crossing}.
	 */
	public Agility(Player player, GameObject object, Obstacle crossing, boolean travelback) {
		super(player, Optional.empty());
		this.crossing = crossing;
		this.running = player.getMovementQueue().isRunning();
		this.object = object;
		this.travelback = travelback;
	}
	
	public Agility(Player player, GameObject object, Obstacle crossing) {
		this(player, object, crossing, false);
	}
	
	/**
	 * The delay intervals of this skill action in ticks.
	 * @return the delay intervals.
	 */
	@Override
	public final int delay() {
		return crossing.delay;
	}
	
	/**
	 * The priority determines if this skill can be overriden by another skill.
	 * @return <true> if the skill can be overriden by other skills, <false> otherwise.
	 */
	@Override
	public final boolean isPrioritized() {
		return true;
	}
	
	/**
	 * The skill that this skill action is for.
	 * @return the skill data.
	 */
	@Override
	public final SkillData skill() {
		return SkillData.AGILITY;
	}
	
	@Override
	public final boolean instant() {
		return crossing.instant;
	}
	
	@Override
	public final boolean canExecute() {
		return true;
	}
	
	@Override
	public final boolean init() {
		if(!getPlayer().getSkills()[Skills.AGILITY].reqLevel(crossing.requirement)) {
			getPlayer().message("You need an agility level of " + crossing.requirement + " to cross this obstacle.");
			return false;
		}
		return crossing.crossable(player);
	}
	
	@Override
	public final void onSubmit() {
		if(running) {
			player.getMovementQueue().setRunning(false);
		}
		
		crossing.initialize(player);
		
		player.getActivityManager().setAllExcept(ActivityManager.ActivityType.LOG_OUT);
	}
	
	/**
	 * The method executed when the delay has elapsed.
	 * @param t the task executing this skill action.
	 */
	@Override
	public final void execute(Task t) {
		if(!travelback && player.getPosition().same(crossing.end)) {
			t.cancel();
			return;
		} else if(travelback && player.getPosition().same(crossing.start[0])) {
			t.cancel();
			return;
		}
		
		crossing.execute(player, t);
	}
	
	@Override
	public void onStop() {
		if(getPlayer().getState() == EntityState.INACTIVE) {
			getPlayer().move(crossing.start[0]);
			return;
		}
		
		crossing.onStop(player);
		Skills.experience(getPlayer(), crossing.experience, skill().getId());
		
		if(running) {
			player.getMovementQueue().setRunning(true);
		}
		
		player.getActivityManager().enable();
	}
}
