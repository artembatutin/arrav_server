package net.arrav.content.teleport;

import net.arrav.content.minigame.MinigameHandler;
import net.arrav.net.packet.out.SendWalkable;
import net.arrav.util.ActionListener;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager;
import net.arrav.world.entity.item.container.session.ExchangeSessionManager;
import net.arrav.world.locale.Position;

import java.util.Optional;

/**
 * The spell implementation that provides additional functions exclusively for teleportation spells.
 * @author Artem Batutin
 */
public abstract class TeleportSpell {
	
	/**
	 * The destination of this teleport spell
	 */
	private final Position destination;
	
	/**
	 * The action that should be executed when the player reaches his destination.
	 */
	private Optional<ActionListener> action = Optional.empty();
	
	/**
	 * Constructs a new {@link TeleportSpell}.
	 * @param destination the destination of this teleport spell.
	 */
	public TeleportSpell(Position destination) {
		this.destination = destination;
	}
	
	/**
	 * Resets the player flags for the specified {@code player}.
	 * @param player the player we're resetting the flags for.
	 */
	public final void resetPlayerFlags(Player player) {
		//FightCavesHandler.remove(player);
		player.widget(-1);
		player.out(new SendWalkable(-1));
		player.setTeleportStage(1);
		player.getCombat().reset(true, true);
		player.faceEntity(null);
		player.setFollowing(false);
		player.setFollowEntity(null);
		player.closeWidget();
	}
	
	/**
	 * Checks if this {@code player} can teleport.
	 * @param player the player to check for.
	 * @return <true> if the player can teleport, <false> otherwise.
	 */
	public final boolean canTeleport(Player player) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.TELEPORT)) {
			player.message("You may not teleport!");
			return false;
		}
		if(player.getForcedMovement() != null) {
			if(player.getForcedMovement().isActive()) {
				player.message("You currently can't do this...");
				return false;
			}
		}
		if(player.getWildernessLevel() >= 20) {
			player.message("You must be below level 20 wilderness to teleport!");
			return false;
		}
		if(player.getTeleblockTimer().get() > 0) {
			int time = player.getTeleblockTimer().get() * 600;
			if(time >= 1000 && time <= 60000) {
				player.message("You must wait approximately " + ((time) / 1000) + " seconds in order to teleport!");
				return false;
			} else if(time > 60000) {
				player.message("You must wait approximately " + ((time) / 60000) + " minutes in order to teleport!");
				return false;
			}
		}
		if(player.getTeleportStage() > 0) {
			player.message("You are already teleporting.");
			return false;
		}
		if(!MinigameHandler.execute(player, m -> m.canTeleport(player, getDestination().copy()))) {
			return false;
		}
		if(!this.canCast(player)) {
			return false;
		}
		if(ExchangeSessionManager.get().inAnySession(player)) {
			ExchangeSessionManager.get().reset(player);
		}
		
		player.getActivityManager().execute(ActivityManager.ActivityType.TELEPORT);
		return true;
	}
	
	/**
	 * Condition if it's possible to cast.
	 * @param caster the teleporter.
	 * @return true if possible.
	 */
	public boolean canCast(Player caster) {
		return true;
	}
	
	/**
	 * Gets the destination position.
	 * @return destination.
	 */
	public Position getDestination() {
		return destination;
	}
	
	/**
	 * A action listener executed on destination.
	 * @return destination listener.
	 */
	public Optional<ActionListener> onDestination() {
		return action;
	}
	
	/**
	 * Attaching a destination listener.
	 * @param action listener.
	 * @return teleporting spell.
	 */
	public TeleportSpell attach(Optional<ActionListener> action) {
		this.action = action;
		return this;
	}
	
	/**
	 * Attaching a destination listener.
	 * @param action listener.
	 * @return teleporting spell.
	 */
	public TeleportSpell attach(ActionListener action) {
		this.action = Optional.of(action);
		return this;
	}
}