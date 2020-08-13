package com.rageps.content.skill.summoning.familiar.ability;

import com.rageps.content.skill.summoning.familiar.FamiliarAbility;
import com.rageps.world.model.Direction;
import com.rageps.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * Holds functionality for the remote view familiar ability.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class RemoteView extends FamiliarAbility {
	
	/**
	 * Identifies whether the summoner is already viewing.
	 */
	private boolean viewing;
	
	/**
	 * The direction this summoner is viewing.
	 */
	private Optional<Direction> direction;
	
	/**
	 * Constructs a new {@link RemoteView}.
	 */
	public RemoteView() {
		super(FamiliarAbilityType.REMOTE_VIEW);
		
		this.setViewing(false);
		this.setDirection(Optional.empty());
	}
	
	/**
	 * Attempts to view {@code direction} with the camera packet.
	 * @param player the player attempting to view this direction.
	 * @param direction the direction to view.
	 */
	public void view(Player player, Optional<Direction> direction) {
		//FIXME not done yet.
		this.setViewing(true);
		this.setDirection(direction);
	}
	
	@Override
	public void initialise(Player player) {
		// this ability is not initialised when the familiar is summoned.
	}
	
	/**
	 * @return the viewing
	 */
	public boolean isViewing() {
		return viewing;
	}
	
	/**
	 * @param viewing the viewing to set
	 */
	public void setViewing(boolean viewing) {
		this.viewing = viewing;
	}
	
	/**
	 * @return the direction
	 */
	public Optional<Direction> getDirection() {
		return direction;
	}
	
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Optional<Direction> direction) {
		this.direction = direction;
	}
}
