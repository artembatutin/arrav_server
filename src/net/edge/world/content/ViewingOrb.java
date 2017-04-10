package net.edge.world.content;

import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.update.UpdateFlag;

import java.util.stream.IntStream;

/**
 * The container class that handles the opening, closing, and navigation of
 * viewing orbs.
 * @author lare96 <http://github.com/lare96>
 */
public final class ViewingOrb {
	
	/**
	 * The player that is viewing the orb.
	 */
	private final Player player;
	
	/**
	 * The starting position of the player viewing the orb.
	 */
	private final Position start;
	
	/**
	 * The centre position corresponding to the viewing orb.
	 */
	private final Position centre;
	
	/**
	 * The north-west position corresponding to the viewing orb.
	 */
	private final Position northWest;
	
	/**
	 * The north-east position corresponding to the viewing orb.
	 */
	private final Position northEast;
	
	/**
	 * The south-west position corresponding to the viewing orb.
	 */
	private final Position southWest;
	
	/**
	 * The south-east position corresponding to the viewing orb.
	 */
	private final Position southEast;
	
	/**
	 * Creates a new {@link ViewingOrb}.
	 * @param player    the player that is viewing the orb.
	 * @param centre    the centre position corresponding to the viewing orb.
	 * @param northWest the north-west position corresponding to the viewing orb.
	 * @param northEast the north-east position corresponding to the viewing orb.
	 * @param southWest the south-west position corresponding to the viewing orb.
	 * @param southEast the south-east position corresponding to the viewing orb.
	 */
	public ViewingOrb(Player player, Position centre, Position northWest, Position northEast, Position southWest, Position southEast) {
		this.player = player;
		this.start = player.getPosition().copy();
		this.centre = centre;
		this.northWest = northWest;
		this.northEast = northEast;
		this.southWest = southWest;
		this.southEast = southEast;
	}
	
	/**
	 * Opens the viewing orb navigation interface in the sidebar.
	 */
	public void open() {
		IntStream.rangeClosed(0, 13).filter(v -> v != 7 && v != 10).forEach(v -> player.getMessages().sendSidebarInterface(v, -1, 2));
		player.getMessages().sendSidebarInterface(10, 3209, 0);
		player.getMessages().sendSidebarInterface(5, 3209, 1);
		player.getMessages().sendForceTab(TabInterface.INVENTORY.getNew());
		player.getMessages().sendString("@yel@Centre", 15239);
		player.getMessages().sendString("@yel@North-West", 15240);
		player.getMessages().sendString("@yel@North-East", 15241);
		player.getMessages().sendString("@yel@South-East", 15242);
		player.getMessages().sendString("@yel@South-West", 15243);
		player.getMovementQueue().setLockMovement(true);
		player.setVisible(false);
		player.getActivityManager().disable();
		player.getMessages().sendMinimapState(2);
		player.setPlayerNpc(2982);
		player.getFlags().flag(UpdateFlag.APPEARANCE);
		move("Centre", 15239, centre);
	}
	
	/**
	 * Closes the viewing orb navigation interface in the sidebar and teleports
	 * the player back to the starting position.
	 */
	public void close() {
		player.sendDefaultSidebars();
		player.getMovementQueue().setLockMovement(false);
		player.setVisible(true);
		player.getActivityManager().enable();
		player.setPlayerNpc(-1);
		player.getFlags().flag(UpdateFlag.APPEARANCE);
		player.getMessages().sendMinimapState(0);
		player.move(start);
	}
	
	/**
	 * Moves the player to {@code position} and updates the navigation interface
	 * with the position that the player is being moved to.
	 * @param positionName   the name of the position being moved to.
	 * @param positionLineId the interface string id of the position being moved to.
	 * @param position       the position being moved to.
	 */
	public void move(String positionName, int positionLineId, Position position) {
		if(position.same(player.getPosition()))
			return;
		player.getMessages().sendString("@yel@Centre", 15239);
		player.getMessages().sendString("@yel@North-West", 15240);
		player.getMessages().sendString("@yel@North-East", 15241);
		player.getMessages().sendString("@yel@South-East", 15242);
		player.getMessages().sendString("@yel@South-West", 15243);
		player.getMessages().sendString("@whi@" + positionName, positionLineId);
		player.move(position);
	}
	
	/**
	 * Gets the starting position of the player viewing the orb.
	 * @return the starting position.
	 */
	public Position getStart() {
		return start;
	}
	
	/**
	 * Gets the centre position corresponding to the viewing orb.
	 * @return the centre position.
	 */
	public Position getCentre() {
		return centre;
	}
	
	/**
	 * Gets the north-west position corresponding to the viewing orb.
	 * @return the north-west position.
	 */
	public Position getNorthWest() {
		return northWest;
	}
	
	/**
	 * Gets the north-east position corresponding to the viewing orb.
	 * @return the north-east position.
	 */
	public Position getNorthEast() {
		return northEast;
	}
	
	/**
	 * Gets the south-west position corresponding to the viewing orb.
	 * @return the south-west position.
	 */
	public Position getSouthWest() {
		return southWest;
	}
	
	/**
	 * Gets the south-east position corresponding to the viewing orb.
	 * @return the south-east position.
	 */
	public Position getSouthEast() {
		return southEast;
	}
}
