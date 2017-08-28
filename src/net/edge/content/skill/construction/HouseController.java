package net.edge.content.skill.construction;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.skill.construction.furniture.ConstructionPlan;
import net.edge.content.skill.construction.room.Room;
import net.edge.world.locale.Position;

/**
 * A house controller class which stores data for one single player.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class HouseController {

	/**
	 * The current state of the respective player.
	 */
	private State state = State.AWAY;

	/**
	 * The condition if the player is currently in building mode.
	 */
	private boolean building;

	/**
	 * The active house the player is in.
	 */
	private House active;

	/**
	 * Last build position to restore player on his previous position.
	 */
	private Position buildPosition;

	/**
	 * The player's contruction plan to build something.
	 */
	private ConstructionPlan plan = new ConstructionPlan();

	/**
	 * The player's saved rooms.
	 */
	private Room[][][] rooms = new Room[5][13][13];

	/**
	 * All of the built portals.
	 */
	private final ObjectList<Portal> portals = new ObjectArrayList<>();

	public Position buildPosition() {
		return buildPosition;
	}

	public void setBuildPosition(Position constructionPos) {
		this.buildPosition = constructionPos;
	}

	public Room[][][] getRooms() {
		return rooms;
	}

	public void setRooms(Room[][][] rooms) {
		this.rooms = rooms;
	}

	public ObjectList<Portal> getPortals() {
		return portals;
	}

	/**
	 * @return Condition if the player is in building mode.
	 */
	public boolean isBuilding() {
		return building;
	}

	/**
	 * Sets the building condition.
	 *
	 * @param building building flag.
	 */
	public void setBuilding(boolean building) {
		this.building = building;
	}

	/**
	 * Setting the player's state.
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @return Gets the player's state.
	 */
	public State getState() {
		return state;
	}

	/**
	 * @return Condition if the player is in a dungeon.
	 */
	public boolean isDungeon() {
		return state == State.HOME_DUNGEON || state == State.VISTING_DUNGEON;
	}

	/**
	 * Gets the active house the player is in, if any.
	 *
	 * @return active house.
	 */
	public House getActive() {
		return active;
	}

	/**
	 * Sets an active house that the player is in.
	 *
	 * @param active visited house.
	 */
	public void setActive(House active) {
		this.active = active;
	}

	/**
	 * @return Gets the construction plan of this player.
	 */
	public ConstructionPlan getPlan() {
		return plan;
	}

	/**
	 * Sets a new construction plane to build something.
	 *
	 * @param plan new plan.
	 */
	public void setPlan(ConstructionPlan plan) {
		this.plan.setPanel(null);
		this.plan = plan;
	}

	/**
	 * Enumeration of possible states.
	 */
	public enum State {
		/**
		 * In his house.
		 */
		HOME, /**
		 * In his house in dungeon.
		 */
		HOME_DUNGEON, /**
		 * Visiting a friend's house.
		 */
		VISITING, /**
		 * Visting a friend's house dungeon.
		 */
		VISTING_DUNGEON, /**
		 * Away in the world, not in a house.
		 */
		AWAY
	}
}
