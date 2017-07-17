package net.edge.content.teleport;

import net.edge.net.packet.out.SendWalkable;
import net.edge.util.ActionListener;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.skill.Skills;
import net.edge.locale.Position;
import net.edge.world.Spell;
import net.edge.world.World;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.activity.ActivityManager;

import java.util.Optional;

/**
 * The spell implementation that provides additional functions exclusively for
 * teleportation spells.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class TeleportSpell extends Spell {
	
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
		player.getCombatBuilder().reset();
		player.faceEntity(null);
		player.setFollowing(false);
		player.setFollowEntity(null);
		player.closeWidget();
		Skills.experience(player, this.baseExperience(), Skills.MAGIC);
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
		if(World.getExchangeSessionManager().inAnySession(player)) {
			World.getExchangeSessionManager().reset(player);
		}
		
		player.getActivityManager().execute(ActivityManager.ActivityType.TELEPORT);
		return true;
	}
	
	/**
	 * @return the destination
	 */
	public Position getDestination() {
		return destination;
	}
	
	public Optional<ActionListener> onDestination() {
		return action;
	}
	
	public TeleportSpell attach(Optional<ActionListener> action) {
		this.action = action;
		return this;
	}
	
	public TeleportSpell attach(ActionListener action) {
		this.action = Optional.of(action);
		return this;
	}
}