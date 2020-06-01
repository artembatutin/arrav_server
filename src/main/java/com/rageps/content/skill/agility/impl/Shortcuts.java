package com.rageps.content.skill.agility.impl;

import com.rageps.content.skill.agility.AgilityCourse;
import com.rageps.content.skill.agility.obstacle.ObstacleAction;
import com.rageps.content.skill.agility.obstacle.ObstacleType;
import com.rageps.content.skill.agility.obstacle.impl.Movable;
import com.rageps.content.skill.agility.obstacle.impl.Steppable;
import com.rageps.content.skill.agility.obstacle.impl.Walkable;
import com.rageps.action.impl.ObjectAction;
import com.rageps.content.skill.agility.AgilityCourseType;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

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
	 * @param player {@link #getPlayer()}.
	 * @param object {@link #getObject()}.
	 * @param definition {@link #definition}.
	 */
	public Shortcuts(Player player, GameObject object, ShortcutsData definition) {
		super(player, object, AgilityCourseType.SHORTCUTS);
		this.definition = definition;
	}
	
	public static void action() {
		for(ShortcutsData data : ShortcutsData.values()) {
			ObjectAction perform = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int click) {
					Shortcuts shortcut = new Shortcuts(player, object, data);
					shortcut.start();
					return true;
				}
			};
			for(int object : data.getObjects()) {
				perform.registerFirst(object);
				if(object > 42003)
					perform.registerFirst(object - 42003);
			}
		}
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
		 * The object identification for this object.
		 */
		private final int[] objectIds;
		
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
		 * @param objectIds {@link #objectIds}.
		 * @param message {@link #message}.
		 * @param crossedMessage {@link #crossedMessage}.
		 * @param obstacleAction {@link #obstacleAction}.
		 */
		ShortcutsData(int[] objectIds, String message, Optional<String> crossedMessage, ObstacleAction obstacleAction) {
			this.objectIds = objectIds;
			this.message = message;
			this.crossedMessage = crossedMessage;
			this.obstacleAction = obstacleAction;
		}
		
		/**
		 * Constructs a new {@link ShortcutsData}.
		 * @param objectIds {@link #objectIds}.
		 * @param message {@link #message}.
		 * @param crossedMessage {@link #crossedMessage}.
		 * @param obstacleAction {@link #obstacleAction}.
		 */
		ShortcutsData(int[] objectIds, String message, String crossedMessage, ObstacleAction obstacleAction) {
			this.objectIds = objectIds;
			this.message = message;
			this.crossedMessage = Optional.ofNullable(crossedMessage);
			this.obstacleAction = obstacleAction;
		}
		
		/**
		 * Constructs a new {@link ShortcutsData}.
		 * @param objectIds {@link #objectIds}.
		 * @param type {@link #message}.
		 * @param obstacleAction {@link #obstacleAction}.
		 */
		ShortcutsData(int[] objectIds, ObstacleType type, ObstacleAction obstacleAction) {
			this.objectIds = objectIds;
			this.message = type.getMessage();
			this.crossedMessage = type.getCrossedMessage();
			this.obstacleAction = obstacleAction;
		}
		
		/**
		 * Gets the objects of this shortcut.
		 */
		public int[] getObjects() {
			return objectIds;
		}
	}
}
