package net.edge.world.content.skill.agility;

import net.edge.task.Task;
import net.edge.world.content.skill.SkillData;
import net.edge.world.content.skill.Skills;
import net.edge.world.content.skill.action.SkillAction;
import net.edge.world.content.skill.agility.obstacle.ObstacleAction;
import net.edge.world.content.skill.agility.obstacle.ObstacleActivity;
import net.edge.world.locale.Position;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.move.ForcedMovement;
import net.edge.world.node.entity.move.ForcedMovementManager;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;
import net.edge.world.node.object.ObjectNode;

import java.util.Optional;

/**
 * The skill action which holds basic functionality for Agility Courses.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class AgilityCourse extends SkillAction {
	
	/**
	 * The current activity we're executing.
	 */
	private ObstacleActivity current;
	
	/**
	 * The type of course this agility represents.
	 */
	private final AgilityCourseType type;
	
	/**
	 * The object this player is interacting with.
	 */
	private final ObjectNode object;
	
	/**
	 * Indicates if the player was running before he attempted to cross the obstacle.
	 */
	private final boolean running;
	
	/**
	 * Constructs a new {@link AgilityCourse}.
	 * @param player the player we're handling functionality for.
	 * @param object the object being interacted with.
	 * @param type   the type of this agility course.
	 */
	public AgilityCourse(Player player, ObjectNode object, AgilityCourseType type) {
		super(player, Optional.empty());
		
		this.type = type;
		this.running = player.getMovementQueue().isRunning();
		this.object = object;
	}
	
	/**
	 * The respective obstacle policy for this agility course.
	 */
	public abstract ObstacleAction obstacleAction();
	
	/**
	 * The message sent when the player attempts to cross the obstacle.
	 * @return the message.
	 */
	public abstract String message();
	
	/**
	 * The message sent when the player successfully crossed the obstacle.
	 * @return the message or {@link Optional#empty} otherwise.
	 */
	public abstract Optional<String> crossedMessage();

	public void onSuccess() {
		
	}
	
	@Override
	public final boolean instant() {
		return current.instant();
	}
	
	@Override
	public final boolean canExecute() {
		//Method shouldn't be used since this checks each cycle, and with agility you only check on initialisation.
		return true;
	}
	
	@Override
	public boolean init() {
		this.current = obstacleAction().activity(getPlayer());
		if(!getPlayer().getSkills()[Skills.AGILITY].reqLevel(current.getRequirement())) {
			getPlayer().message("You need an agility level of " + obstacleAction().activity(getPlayer()).getRequirement() + " to cross this obstacle.");
			return false;
		}
		return current.init(getPlayer());
	}
	
	@Override
	public int delay() {
		return current.getDelay();
	}
	
	@Override
	public void onSubmit() {
		current.onSubmit(getPlayer());
		if(message() != null) {
			getPlayer().message(message());
		}
		if(running) {
			player.getMovementQueue().setRunning(false);
		}
		player.getActivityManager().setAllExcept(ActivityManager.ActivityType.LOG_OUT);
	}
	
	@Override
	public void execute(Task t) {
		if(getPlayer().getPosition().same(current.getDestination())) {
			t.cancel();
			return;
		} else if(current.travelBack() && player.getPosition().same(current.getStart())) {
			t.cancel();
			return;
		}
		current.execute(getPlayer(), t);
	}
	
	@Override
	public void onStop() {
		if(getPlayer().getState() == NodeState.INACTIVE) {
			getPlayer().move(current.getStart());
			return;
		}
		current.onCancel(getPlayer());
		Skills.experience(getPlayer(), current.getExperience(), skill().getId());
		if(running) {
			player.getMovementQueue().setRunning(true);
		}
		player.getActivityManager().enable();
		
		onSuccess();
		
		crossedMessage().ifPresent(getPlayer().getMessages()::sendMessage);
	}

	public static void executeForcedMovementAction(Player player, Position beggining_position, Position ending_position, int first_speed, int second_speed, Object animation_id) {

		player.setForcedMovement(new ForcedMovement(player));

		ForcedMovement movement = player.getForcedMovement();

		movement.setActive(true);

		movement.setFirst(beggining_position);

		movement.setSecond(ending_position);

		movement.setFirstSpeed(first_speed);

		movement.setSecondSpeed(second_speed);

		movement.setAnimation(!(animation_id instanceof Animation) ? new Animation((Integer) animation_id) : new Animation((Integer) animation_id));

		ForcedMovementManager.submit(player, movement);

	}
	
	@Override
	public SkillData skill() {
		return SkillData.AGILITY;
	}
	
	public ObjectNode getObject() {
		return object;
	}
	
	@Override
	public boolean isPrioritized() {
		return true;
	}

	public AgilityCourseType getType() {
		return type;
	}
}
