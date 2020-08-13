package com.rageps.content.skill.agility.impl.barb;

import com.rageps.content.achievements.Achievement;
import com.rageps.content.skill.agility.impl.barb.impl.BalanceBeam;
import com.rageps.content.skill.agility.impl.barb.impl.SlideDownRoof;
import com.rageps.content.skill.agility.obstacle.ObstacleAction;
import com.rageps.content.skill.agility.obstacle.ObstacleActivity;
import com.rageps.content.skill.agility.obstacle.ObstacleType;
import com.rageps.content.skill.agility.obstacle.impl.Climbable;
import com.rageps.content.skill.agility.obstacle.impl.Movable;
import com.rageps.content.skill.agility.obstacle.impl.Walkable;
import com.rageps.net.packet.out.SendObjectAnimation;
import com.rageps.action.impl.ObjectAction;
import com.rageps.content.skill.Skills;
import com.rageps.content.skill.agility.AgilityCourse;
import com.rageps.content.skill.agility.AgilityCourseType;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.task.Task;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.move.ForcedMovement;
import com.rageps.world.entity.actor.move.ForcedMovementManager;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.entity.object.ObjectDirection;
import com.rageps.world.entity.object.ObjectType;
import com.rageps.world.locale.Position;

import java.util.Optional;

/**
 * Holds functionality for passing obstacles for the BarbarianOutpost Agility course.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BarbarianOutpostAgility extends AgilityCourse {
	
	/**
	 * The definition for this obstacle course.
	 */
	private final BarbarianAgilityData obstacle;
	
	/**
	 * Constructs a new {@link BarbarianOutpostAgility} course.
	 * @param player {@link #getPlayer()}.
	 * @param object {@link #getObject()}.
	 * @param obstacle the obstacle this player is trying to cross.
	 */
	private BarbarianOutpostAgility(Player player, GameObject object, BarbarianAgilityData obstacle) {
		super(player, object, AgilityCourseType.BARBARIAN_AGILITY);
		this.obstacle = obstacle;
	}
	
	public static void action() {
		for(BarbarianAgilityData data : BarbarianAgilityData.values()) {
			ObjectAction perform = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int click) {
					BarbarianOutpostAgility agility = new BarbarianOutpostAgility(player, object, data);
					agility.start();
					return true;
				}
			};
			for(int object : data.getObjects()) {
				perform.registerFirst(object);
				perform.registerFirst(object + 42003);
			}
		}
	}
	
	@Override
	public void onSuccess() {
		player.getAgilityBonus().addBarbarianObstacle(obstacle);
		if(player.getAgilityBonus().hasCompletedBarbarianAgilityCourse()) {
			player.getAgilityBonus().clearBarbarianObstacles();
			player.message("You have successfully completed the barbarian agility laps.");
			Skills.experience(player, 500, Skills.AGILITY);
			Achievement.TOO_FAST.inc(player);
		}
	}
	
	@Override
	public ObstacleAction obstacleAction() {
		return obstacle.obstacleAction;
	}
	
	@Override
	public String message() {
		return obstacle.message;
	}
	
	@Override
	public Optional<String> crossedMessage() {
		return obstacle.crossedMessage;
	}
	
	@Override
	public double experience() {
		return obstacle.obstacleAction.activity(getPlayer()).getExperience();
	}
	
	public enum BarbarianAgilityData {
		ROPE_SWING(new int[]{43526}, ObstacleType.ROPE_SWING, player1 -> new Movable(new Position(player1.getPosition().getX(), 3554, 0), new Position(player1.getPosition().getX(), 3549, 0), ObstacleType.ROPE_SWING.getAnimation(), 90, 3, 35, 22) {
			@Override
			public boolean canExecute(Player player1) {
				if(player1.getPosition().same(new Position(2552, 3554, 0)) || player1.getPosition().same(new Position(2551, 3554, 0))) {
					return true;
				}
				ObstacleActivity.executeForcedMovementAction(player1, player1.getPosition(), getDestination(), 20, 20, getAnimation());
				//player1.message("You must be standing infront one of the ropes.");
				return false;
			}
			
			@Override
			public void prerequisites(Player player1) {
				player1.out(new SendObjectAnimation(new Position(player1.getPosition().getX(), 3550), 497, ObjectType.GENERAL_PROP, ObjectDirection.WEST));
			}
		}),
		LOG_BALANCE(new int[]{43595}, ObstacleType.LOG_BALANCE, player1 -> new Walkable(new Position(2551, 3546, 0), new Position(2541, 3546, 0), ObstacleType.LOG_BALANCE.getAnimation(), 35, 13.7)),
		
		OBSTACLE_NET_UP(new int[]{20211}, ObstacleType.NETTING, player1 -> new Climbable(new Position(2539, player1.getPosition().getY(), 0), new Position(2537, player1.getPosition().getY(), 1), ObstacleType.NETTING.getAnimation(), 2, 35, 8.2)),
		
		BALANCING_LEDGE(new int[]{2302}, ObstacleType.LEDGE, player1 -> new Walkable(new Position(2536, 3547, 1), new Position(2532, 3547, 1), ObstacleType.LEDGE.getAnimation(), 35, 22)),
		CRUMBLING_WALL(new int[]{1948}, ObstacleType.CRUMBLING_WALL, player1 -> {
			Position start = player1.getPosition().same(new Position(2536, 3553, 0)) ? new Position(2536, 3553, 0) : new Position(2541, 3553, 0);
			Position destination = player1.getPosition().same(new Position(2541, 3553)) ? new Position(2543, 3553, 0) : new Position(2538, 3553, 0);
			return new Movable(start, destination, ObstacleType.CRUMBLING_WALL.getAnimation(), 57, 2, 35, 13.7) {
				@Override
				public boolean canExecute(Player player1) {
					if(player1.getPosition().same(new Position(2536, 3553, 0)) || player1.getPosition().same(new Position(2541, 3553, 0))) {
						return true;
					}
					ObstacleActivity.executeForcedMovementAction(player1, player1.getPosition(), getDestination(), 20, 20, getAnimation());
					
					//player1.message("You can't cross this obstacle from this side.");
					if(player1.getPosition().same(new Position(2536, 3553, 0)) || player1.getPosition().same(new Position(2541, 3553, 0))) {
						return true;
					}
					
					return false;
				}
			};
		}),
		/**
		 * Advanced Barbarian course
		 */
		RUN_UP_WALL(new int[]{43533}, ObstacleType.RUN_UP_WALL, player1 -> new Movable(new Position(2538, 3542, 0), new Position(2538, 3545, 2), ObstacleType.RUN_UP_WALL.getAnimation(), 50, 1, 90, 15) {
			@Override
			public void onSubmit(Player player1) {
				player1.facePosition(new Position(2538, 3543, 0));
				LinkedTaskSequence sequence = new LinkedTaskSequence();
				sequence.connect(1, () -> player1.animation(new Animation(10492)));
				sequence.connect(7, () -> {
					player1.move(new Position(2538, 3543, 2));
					ForcedMovement movement = new ForcedMovement(player1);
					movement.setFirst(player1.getPosition());
					movement.setSecond(new Position(2538, 3545, 2));
					movement.setSecondSpeed(20);
					movement.setAnimation(10493);
					ForcedMovementManager.submit(player1, movement);
				});
				sequence.start();
			}
		}),
		CLIMB_UP_WALL(new int[]{43597}, ObstacleType.CLIMB_UP_WALL, player1 -> new Climbable(new Position(2537, 3546, 2), new Position(2536, 3546, 3), ObstacleType.CLIMB_UP_WALL.getAnimation(), 3, 90, 15) {
			@Override
			public boolean canExecute(Player player1) {
				if(!player1.getPosition().same(getStart())) {
					player1.getMovementQueue().walk(getStart());
					return false;
				}
				player1.facePosition(new Position(2536, 3546));
				return true;
			}
			
			@Override
			public void onSubmit(Player player1) {
				player1.animation(getAnimation());
			}
			
			@Override
			public void execute(Player player1, Task t) {
				player1.move(getDestination());
				player1.animation(new Animation(11794));
			}
		}),
		SPRING_DEVICE(new int[]{43587}, ObstacleType.SPRING_DEVICE, player1 -> new Movable(new Position(2533, 3547, 3), new Position(2532, 3553, 3), ObstacleType.SPRING_DEVICE.getAnimation(), 80, 4, 90, 15) {
			@Override
			public void prerequisites(Player player1) {
				player1.facePosition(new Position(player1.getPosition().getX(), player1.getPosition().getY() + 1));
				player1.out(new SendObjectAnimation(new Position(2532, 3544, 3), 11819, ObjectType.GENERAL_PROP, ObjectDirection.EAST));
			}
		}),
		BALANCE_BEAM(new int[]{43527}, ObstacleType.BALANCE_BEAM, player1 -> new BalanceBeam()),
		JUMP_GAP(new int[]{85534}, ObstacleType.JUMP_GAP, player1 -> new Movable(new Position(2535, 3553, 3), new Position(2537, 3553, 2), ObstacleType.JUMP_GAP.getAnimation(), 35, 1, 90, 15) {
			@Override
			public void onCancel(Player player) {
				player.move(new Position(2539, 3553, 2));
				player.animation(new Animation(2588));
			}
		}),
		SLIDE_DOWN_ROOF(new int[]{43532}, ObstacleType.JUMP_GAP, SlideDownRoof::new);
		
		/**
		 * The object identification for this object.
		 */
		private final int[] objectId;
		
		/**
		 * The message sent to this player when he attempts to cross the obstacle.
		 */
		private final String message;
		
		/**
		 * The message sent to this player when he successfully crossed the obstacle.
		 */
		private final Optional<String> crossedMessage;
		
		/**
		 * The agility policy linked to this obstacle action.
		 */
		private final ObstacleAction obstacleAction;
		
		/**
		 * Constructs a new {@link BarbarianAgilityData}.
		 * @param objectId {@link #objectId}.
		 * @param message {@link #message}.
		 * @param crossedMessage {@link #crossedMessage}.
		 * @param obstacleAction {@link #obstacleAction}.
		 */
		BarbarianAgilityData(int[] objectId, String message, Optional<String> crossedMessage, ObstacleAction obstacleAction) {
			this.objectId = objectId;
			this.message = message;
			this.crossedMessage = crossedMessage;
			this.obstacleAction = obstacleAction;
		}
		
		/**
		 * Constructs a new {@link BarbarianAgilityData}.
		 * @param objectId {@link #objectId}.
		 * @param message {@link #message}.
		 * @param crossedMessage {@link #crossedMessage}.
		 * @param obstacleAction {@link #obstacleAction}.
		 */
		BarbarianAgilityData(int[] objectId, String message, String crossedMessage, ObstacleAction obstacleAction) {
			this.objectId = objectId;
			this.message = message;
			this.crossedMessage = Optional.ofNullable(crossedMessage);
			this.obstacleAction = obstacleAction;
		}
		
		/**
		 * Constructs a new {@link BarbarianAgilityData}.
		 * @param objectId {@link #objectId}.
		 * @param type {@link #message} and {@link #crossedMessage}.
		 * @param obstacleAction {@link #obstacleAction}.
		 */
		BarbarianAgilityData(int[] objectId, ObstacleType type, ObstacleAction obstacleAction) {
			this.objectId = objectId;
			this.message = type.getMessage();
			this.crossedMessage = type.getCrossedMessage();
			this.obstacleAction = obstacleAction;
		}
		
		/**
		 * Objects of this course.
		 */
		public int[] getObjects() {
			return objectId;
		}
	}
}
