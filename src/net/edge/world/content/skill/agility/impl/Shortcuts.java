package net.edge.world.content.skill.agility.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.world.content.skill.agility.AgilityCourse;
import net.edge.world.content.skill.agility.AgilityCourseType;
import net.edge.world.content.skill.agility.obstacle.ObstacleAction;
import net.edge.world.content.skill.agility.obstacle.ObstacleType;
import net.edge.world.content.skill.agility.obstacle.impl.Movable;
import net.edge.world.content.skill.agility.obstacle.impl.Steppable;
import net.edge.world.content.skill.agility.obstacle.impl.Walkable;
import net.edge.world.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.object.ObjectNode;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Holds functionality for passing
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Shortcuts extends AgilityCourse {
	
	/**
	 * The shortcut this player is crossing.
	 */
	private final ShortcutsData definition;
	
	/**
	 * Constructs a new {@link Shortcuts}.
	 * @param player     {@link #getPlayer()}.
	 * @param object     {@link #getObject()}.
	 * @param definition {@link #definition}.
	 */
	public Shortcuts(Player player, ObjectNode object, ShortcutsData definition) {
		super(player, object, AgilityCourseType.SHORTCUTS);
		this.definition = definition;
	}
	
	/**
	 * Starts this skill action.
	 * @param player {@link #getPlayer()}.
	 * @param object the object this player is interacting with.
	 * @return <true> if the player can execute the logic, <false> otherwise.
	 */
	public static boolean execute(Player player, ObjectNode object) {
		Optional<ShortcutsData> definition = ShortcutsData.getDefinition(object.getId());
		
		if(!definition.isPresent()) {
			return false;
		}
		
		Shortcuts shortcut = new Shortcuts(player, object, definition.get());
		shortcut.start();
		return true;
	}
	
	@Override
	public ObstacleAction obstacleAction() {
		return definition.obstacleAction;
	}
	
	@Override
	public String message() {
		return definition.message;
	}
	
	@Override
	public Optional<String> crossedMessage() {
		return definition.crossedMessage;
	}
	
	@Override
	public double experience() {
		return definition.obstacleAction.activity(player).getExperience();
	}
	
	private enum ShortcutsData {
		TAVERLY_STRANGE_FLOOR(new int[]{51297}, ObstacleType.STRANGE_FLOOR, player -> new Movable(new Position(2880, 9813), new Position(2878, 9813), ObstacleType.STRANGE_FLOOR.getAnimation(), 50, 80, 0) {
			@Override
			public boolean travelBack() {
				return true;
			}
		}),
		TAVERLY_OBSTACLE_PIPE(new int[]{51296}, ObstacleType.PIPE, player -> new Walkable(new Position(2886, 9799), new Position(2892, 9799), ObstacleType.PIPE.getAnimation(), 70, 0) {
			@Override
			public boolean travelBack() {
				return true;
			}
		}),
		BRIMHAVEN_OBSTACLE_PIPE(new int[]{5100}, ObstacleType.PIPE, player -> new Walkable(new Position(2655, 9566), new Position(2655, 9573), ObstacleType.PIPE.getAnimation(), 70, 0) {
			@Override
			public boolean travelBack() {
				return true;
			}
		}),
		BRIMHAVEN_OBSTACLE_PIPE_SECOND(new int[]{5099}, ObstacleType.PIPE, player -> new Walkable(new Position(2698, 9492), new Position(2698, 9499), ObstacleType.PIPE.getAnimation(), 70, 0) {
			@Override
			public boolean travelBack() {
				return true;
			}
		}),
		BRIMHAVEN_LOG_BALANCE(new int[]{5088, 5090}, ObstacleType.LOG_BALANCE, player -> new Walkable(new Position(2682, 9506), new Position(2687, 9506), ObstacleType.LOG_BALANCE.getAnimation(), 70, 0) {
			@Override
			public boolean travelBack() {
				return true;
			}
		}),
		BRIMHAVEN_OBSTACLE_ROCKS(new int[]{5111}, ObstacleType.STEPPING_STONE, player -> new Steppable(new Position(2647, 9557), new Position[]{new Position(2647, 9558), new Position(2647, 9559), new Position(2647, 9560), new Position(2648, 9560), new Position(2649, 9560), new Position(2649, 9561)}, new Position(2649, 9562), ObstacleType.STEPPING_STONE.getAnimation(), 30, 0)),
		BRIMHAVEN_OBSTACLE_ROCKS_OTHERWAY(new int[]{5110}, ObstacleType.STEPPING_STONE, player -> new Steppable(new Position(2649, 9562), new Position[]{new Position(2649, 9561), new Position(2649, 9560), new Position(2648, 9560), new Position(2647, 9560), new Position(2647, 9559), new Position(2647, 9558)}, new Position(2647, 9557), ObstacleType.STEPPING_STONE.getAnimation(), 30, 0));
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<ShortcutsData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(ShortcutsData.class));
		
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
		 * Constructs a new {@link ShortcutsData}.
		 * @param objectId       {@link #objectId}.
		 * @param message        {@link #message}.
		 * @param crossedMessage {@link #crossedMessage}.
		 * @param obstacleAction {@link #obstacleAction}.
		 */
		ShortcutsData(int[] objectId, String message, Optional<String> crossedMessage, ObstacleAction obstacleAction) {
			this.objectId = objectId;
			this.message = message;
			this.crossedMessage = crossedMessage;
			this.obstacleAction = obstacleAction;
		}
		
		/**
		 * Constructs a new {@link ShortcutsData}.
		 * @param objectId       {@link #objectId}.
		 * @param message        {@link #message}.
		 * @param crossedMessage {@link #crossedMessage}.
		 * @param obstacleAction {@link #obstacleAction}.
		 */
		ShortcutsData(int[] objectId, String message, String crossedMessage, ObstacleAction obstacleAction) {
			this.objectId = objectId;
			this.message = message;
			this.crossedMessage = Optional.ofNullable(crossedMessage);
			this.obstacleAction = obstacleAction;
		}
		
		/**
		 * Constructs a new {@link ShortcutsData}.
		 * @param objectId       {@link #objectId}.
		 * @param type           {@link #message}.
		 * @param obstacleAction {@link #obstacleAction}.
		 */
		ShortcutsData(int[] objectId, ObstacleType type, ObstacleAction obstacleAction) {
			this.objectId = objectId;
			this.message = type.getMessage();
			this.crossedMessage = type.getCrossedMessage();
			this.obstacleAction = obstacleAction;
		}
		
		/**
		 * Gets an Optional of the matched value.
		 * @param identifier the identifier to check for matches.
		 * @return an optional with the {@link ShortcutsData} found, {@link Optional#empty} otherwise.
		 */
		protected static Optional<ShortcutsData> getDefinition(int identifier) {
			for(ShortcutsData data : VALUES) {
				for(int object : data.objectId) {
					if(object == identifier || object == identifier - 42003) {
						return Optional.of(data);
					}
				}
			}
			return Optional.empty();
		}
	}
}
