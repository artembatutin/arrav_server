package net.arrav.content.skill.agility.impl.wild;

import net.arrav.action.impl.ObjectAction;
import net.arrav.content.skill.Skills;
import net.arrav.content.skill.agility.AgilityCourse;
import net.arrav.content.skill.agility.AgilityCourseType;
import net.arrav.content.skill.agility.impl.wild.impl.ClimbRocks;
import net.arrav.content.skill.agility.obstacle.ObstacleAction;
import net.arrav.content.skill.agility.obstacle.ObstacleType;
import net.arrav.content.skill.agility.obstacle.impl.Movable;
import net.arrav.content.skill.agility.obstacle.impl.Steppable;
import net.arrav.content.skill.agility.obstacle.impl.Walkable;
import net.arrav.net.packet.out.SendObjectAnimation;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.object.GameObject;
import net.arrav.world.entity.object.ObjectDirection;
import net.arrav.world.entity.object.ObjectType;
import net.arrav.world.locale.Position;

import java.util.Optional;

import static net.arrav.content.achievements.Achievement.TOO_FAST;

/**
 * Holds functionality for passing obstacles for the Wilderness Agility course.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class WildernessAgility extends AgilityCourse {
	
	/**
	 * The definition for this obstacle course.
	 */
	private final WildernessAgilityData data;
	
	/**
	 * Constructs a new {@link WildernessAgilityData} course.
	 * @param player {@link #getPlayer()}.
	 * @param object {@link #getObject()}.
	 * @param data the obstacle this player is trying to cross.
	 */
	public WildernessAgility(Player player, GameObject object, WildernessAgilityData data) {
		super(player, object, AgilityCourseType.WILDERNESS_AGILITY);
		this.data = data;
	}
	
	public static void action() {
		for(WildernessAgilityData data : WildernessAgilityData.values()) {
			ObjectAction perform = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int click) {
					WildernessAgility agility = new WildernessAgility(player, object, data);
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
		player.getAgilityBonus().addWildernessObstacle(data);
		if(player.getAgilityBonus().hasCompletedWildernessAgilityCourse()) {
			player.getAgilityBonus().clearWildernessObstacles();
			player.message("You have successfully completed the wilderness agility laps.");
			Skills.experience(player, 800, Skills.AGILITY);
			TOO_FAST.inc(player);
		}
	}
	
	@Override
	public ObstacleAction obstacleAction() {
		return data.obstacleAction;
	}
	
	@Override
	public String message() {
		return data.message;
	}
	
	@Override
	public Optional<String> crossedMessage() {
		return data.crossedMessage;
	}
	
	@Override
	public double experience() {
		return data.obstacleAction.activity(getPlayer()).getExperience();
	}
	
	public enum WildernessAgilityData {
		GATE(new int[]{2309}, ObstacleType.WILDERNESS_GATE, player1 -> new Walkable(new Position(2998, 3916, 0), new Position(2998, 3931, 0), ObstacleType.WILDERNESS_GATE.getAnimation(), 52, 0)),
		GATE_BACK(new int[]{2307}, ObstacleType.WILDERNESS_GATE, player1 -> new Walkable(new Position(2998, 3931), new Position(2998, 3916), ObstacleType.WILDERNESS_GATE.getAnimation(), 52, 0)),
		PIPE(new int[]{2288}, ObstacleType.PIPE, player1 -> new Walkable(new Position(3004, 3937), new Position(3004, 3950), ObstacleType.PIPE.getAnimation(), 52, 12.5)),
		ROPE_SWING(new int[]{2283}, ObstacleType.ROPE_SWING, player1 -> new Movable(new Position(3005, 3953, 0), new Position(3005, 3958, 0), ObstacleType.ROPE_SWING.getAnimation(), 90, 3, 52, 12.5) {
			@Override
			public void prerequisites(Player player1) {
				player1.facePosition(new Position(3005, 3958, 0));
				player1.out(new SendObjectAnimation(new Position(3005, 3952, 0), 497, ObjectType.GENERAL_PROP, ObjectDirection.EAST));
			}
		}),
		STEPPING_STONES(new int[]{37704}, ObstacleType.STEPPING_STONE, player1 -> new Steppable(new Position(3002, 3960, 0), new Position[]{new Position(3001, 3960, 0), new Position(3000, 3960, 0), new Position(2999, 3960, 0), new Position(2998, 3960, 0), new Position(2997, 3960, 0)}, new Position(2996, 3960, 0), ObstacleType.STEPPING_STONE.getAnimation(), 52, 20)),
		LOG_BALANCE(new int[]{2297}, ObstacleType.LOG_BALANCE, player1 -> new Walkable(new Position(3002, 3945, 0), new Position(2994, 3945, 0), ObstacleType.LOG_BALANCE.getAnimation(), 52, 20)),
		CLIMB_ROCKS(new int[]{2328}, ObstacleType.ROCKS, player1 -> new ClimbRocks(player1));
		
		/**
		 * The object identifications for this Obstacle.
		 */
		private final int[] objectIds;
		
		/**
		 * The message sent when attempting to cross this obstacle.
		 */
		private final String message;
		
		/**
		 * The message sent when successfully crossing this obstacle.
		 */
		private final Optional<String> crossedMessage;
		
		/**
		 * The policy chained to this Obstacle.
		 */
		private final ObstacleAction obstacleAction;
		
		/**
		 * Constructs a new {@link WildernessAgilityData}.
		 * @param objectIds {@link #objectIds}.
		 * @param message {@link #message}.
		 * @param crossedMessage {@link #crossedMessage}.
		 * @param obstacleAction {@link #obstacleAction}.
		 */
		WildernessAgilityData(int[] objectIds, String message, Optional<String> crossedMessage, ObstacleAction obstacleAction) {
			this.objectIds = objectIds;
			this.message = message;
			this.crossedMessage = crossedMessage;
			this.obstacleAction = obstacleAction;
		}
		
		/**
		 * Constructs a new {@link WildernessAgilityData}.
		 * @param objectIds {@link #objectIds}.
		 * @param message {@link #message}.
		 * @param crossedMessage {@link #crossedMessage}.
		 * @param obstacleAction {@link #obstacleAction}.
		 */
		WildernessAgilityData(int[] objectIds, String message, String crossedMessage, ObstacleAction obstacleAction) {
			this.objectIds = objectIds;
			this.message = message;
			this.crossedMessage = Optional.ofNullable(crossedMessage);
			this.obstacleAction = obstacleAction;
		}
		
		/**
		 * Constructs a new {@link WildernessAgilityData}.
		 * @param objectIds {@link #objectIds}.
		 * @param type {@link #message} and {@link #crossedMessage}.
		 * @param obstacleAction {@link #obstacleAction}.
		 */
		WildernessAgilityData(int[] objectIds, ObstacleType type, ObstacleAction obstacleAction) {
			this.objectIds = objectIds;
			this.message = type.getMessage();
			this.crossedMessage = type.getCrossedMessage();
			this.obstacleAction = obstacleAction;
		}
		
		/**
		 * Gets the objects of this course.
		 */
		public int[] getObjects() {
			return objectIds;
		}
	}
	
}
