package net.edge.content.skill.hunter.trap;

import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.task.Task;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.GameObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectType;

/**
 * Represents a single trap on the world.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class Trap {

	/**
	 * The owner of this trap.
	 */
	protected final Player player;

	/**
	 * The type of this trap.
	 */
	private final TrapType type;

	/**
	 * The state of this trap.
	 */
	private TrapState state;

	/**
	 * The global object spawned on the world.
	 */
	private DynamicObject object;

	/**
	 * Determines if this trap is abandoned.
	 */
	private boolean abandoned = false;

	/**
	 * Constructs a new {@link Trap}.
	 *
	 * @param player {@link #player}.
	 * @param type   {@link #type}.
	 */
	public Trap(Player player, TrapType type) {
		this.player = player;
		this.type = type;
		this.state = TrapState.PENDING;
		this.object = new DynamicObject(type.objectId, player.getPosition(), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, 0);
	}

	/**
	 * Submits the trap task for this trap.
	 */
	public void submit() {
		this.onSetup();
	}

	/**
	 * Attempts to trap the specified {@code mob} by checking the prerequisites and initiating the
	 * abstract {@link #onCatch} method.
	 *
	 * @param mob the mob to trap.
	 */
	protected void trap(Mob mob) {
		if(!this.getState().equals(TrapState.PENDING) || !canCatch(mob) || this.isAbandoned()) {
			return;
		}
		onCatch(mob);
	}

	/**
	 * The array containing every larupia item set.
	 */
	private static final int[] LARUPIA_SET = new int[]{10041, 10043, 10045};

	/**
	 * Determines fi the player has equiped any set that boosts the success formula.
	 *
	 * @return the amount of items the player is wearing.
	 */
	public boolean hasLarupiaSetEquipped() {
		return player.getEquipment().containsAll(LARUPIA_SET);
	}

	/**
	 * Calculates the chance for the bird to be lured <b>or</b> trapped.
	 *
	 * @param mob the mob being caught.
	 * @return the double value which defines the chance.
	 */
	public int successFormula(Mob mob) {
		Player player = this.getPlayer();
		if(player == null) {
			return 0;
		}
		int chance = 70;
		if(this.hasLarupiaSetEquipped()) {
			chance = chance + 10;
		}

		Skill skill = player.getSkills()[Skills.HUNTER];

		chance = chance + (int) (skill.getLevel() / 1.5) + 10;

		if(skill.getLevel() < 65) {
			chance = (int) (chance * 1.05) + 3;
		} else if(skill.getLevel() < 60) {
			chance = (int) (chance * 1.1);
		} else if(skill.getLevel() < 55) {
			chance = (int) (chance * 1.2);
		} else if(skill.getLevel() < 50) {
			chance = (int) (chance * 1.3) + 1;
		} else if(skill.getLevel() < 40) {
			chance = (int) (chance * 1.4) + 3;
		} else if(skill.getLevel() < 25) {
			chance = (int) (chance * 1.5) + 8;
		}
		return chance;
	}

	/**
	 * Determines if the trap can catch.
	 *
	 * @param mob the mob to check.
	 * @return {@code true} if the player can, {@code false} otherwise.
	 */
	public abstract boolean canCatch(Mob mob);

	/**
	 * The functionality that should be handled when the trap is picked up.
	 */
	public abstract void onPickUp();

	/**
	 * The functionality that should be handled when the trap is being set-up.
	 */
	public abstract void onSetup();

	/**
	 * The functionality that should be handled when the trap has catched.
	 *
	 * @param mob the mob that was catched.
	 */
	public abstract void onCatch(Mob mob);

	/**
	 * The functionality that should be handled every 600ms.
	 *
	 * @param t the task this method is dependant of.
	 */
	public abstract void onSequence(Task t);

	/**
	 * The reward for this player.
	 * return an array of items defining the reward.
	 */
	public abstract Item[] reward();

	/**
	 * The experience gained for catching this npc.
	 *
	 * @return a numerical value defining the amount of experience gained.
	 */
	public abstract double experience();

	/**
	 * Determines if the trap can be claimed.
	 *
	 * @param object the object that was interacted with.
	 * @return {@code true} if the trap can, {@code false} otherwise.
	 */
	public abstract boolean canClaim(GameObject object);

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the type
	 */
	public TrapType getType() {
		return type;
	}

	/**
	 * @return the state
	 */
	public TrapState getState() {
		return state;
	}

	/**
	 * @param state the state to set.
	 */
	public void setState(TrapState state, Mob mob) {
		this.state = state;
	}

	/**
	 * @return the object
	 */
	public DynamicObject getObject() {
		return object;
	}

	/**
	 * Sets the object id.
	 *
	 * @param id the id to set.
	 */
	public void updateObject(int id) {
		int slot = object.getElements();
		this.object = object.setId(id).toDynamic();
		this.object.setElements(slot);
		this.object.publish();
	}

	/**
	 * @return the abandoned
	 */
	public boolean isAbandoned() {
		return abandoned;
	}

	/**
	 * @param abandoned the abandoned to set
	 */
	public void setAbandoned(boolean abandoned) {
		this.abandoned = abandoned;
	}

	/**
	 * The enumerated type whose elements represent a set of constants
	 * used to define the type of a trap.
	 *
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum TrapType {
		BOX_TRAP(19187, 10008),
		FAILED_BOX_TRAP(19192, 10008),
		BIRD_SNARE(19175, 10006),
		FAILED_BIRD_SNARE(19174, 10006);

		/**
		 * The object id for this trap.
		 */
		private final int objectId;

		/**
		 * The item id for this trap.
		 */
		private final int itemId;

		/**
		 * Constructs a new {@link TrapType}.
		 *
		 * @param objectId {@link #objectId}.
		 * @param itemId   {@link #itemId}.
		 */
		TrapType(int objectId, int itemId) {
			this.objectId = objectId;
			this.itemId = itemId;
		}

		/**
		 * @return the object id
		 */
		public int getObjectId() {
			return objectId;
		}

		/**
		 * @return the item id
		 */
		public int getItemId() {
			return itemId;
		}

	}

	/**
	 * The enumerated type whose elements represent a set of constants
	 * used to define the state of a trap.
	 *
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum TrapState {
		PENDING,
		CATCHING,
		CAUGHT,
		FALLEN
	}

}
