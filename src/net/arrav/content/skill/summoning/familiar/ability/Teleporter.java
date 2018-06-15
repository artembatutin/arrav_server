package net.arrav.content.skill.summoning.familiar.ability;

import net.arrav.content.skill.summoning.familiar.FamiliarAbility;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

import java.util.Optional;

/**
 * Holds functionality for the teleport ability.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Teleporter extends FamiliarAbility {
	
	/**
	 * The destination this teleporter will teleport the summoner to.
	 */
	private final Position destination;
	
	/**
	 * The teleport policy chained to this teleporter ability.
	 */
	private final Optional<TeleportPolicy> policy;
	
	/**
	 * Constructs a new {@link Teleporter} ability type.
	 * @param destination {@link #destination}.
	 * @param policy {@link #policy}.
	 */
	public Teleporter(Position destination, Optional<TeleportPolicy> policy) {
		super(FamiliarAbilityType.TELEPORTER);
		
		this.destination = destination;
		this.policy = policy;
	}
	
	@Override
	public void initialise(Player player) {
		// this ability is not initialised when the familiar is summoned.
	}
	
	/**
	 * Attempts to teleport the player.
	 * @param player the player whom is being teleported.
	 * @return <true> if the player was teleported, <false> otherwise.
	 */
	public boolean teleport(Player player) {
		if(policy.isPresent()) {
			return false;
		}
		player.teleport(destination);
		player.message("Your familiar teleports you away...");
		return true;
	}
	
	/**
	 * Attempts to teleport the player.
	 * @param player the player whom is teleporting.
	 * @return <true> if this player was teleported, <false> otherwise.
	 */
	public boolean combatTeleport(Player player) {
		TeleportPolicy policy = this.policy.get();
		
		int amount = player.getMaximumHealth() / policy.percentage;
		
		if(policy.combat && !player.getCombat().inCombat() && player.getCurrentHealth() < amount) {
			player.message("This familiar will only teleport you while you're in combat.");
			return false;
		}
		
		if(player.getCurrentHealth() > amount) {
			return false;
		}
		
		player.teleport(destination);
		player.message("Your familiar teleports you safely away...");
		return true;
	}
	
	/**
	 * The class which holds support for the {@link Teleporter} policy which basically
	 * indicates if the player should be teleported when he reaches
	 * a certain amount of hitpoints.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class TeleportPolicy {
		
		/**
		 * Whether the player will only be teleported if he is in combat.
		 */
		private final boolean combat;
		
		/**
		 * The percentage of hitpoints this player will be teleported on.
		 */
		private final int percentage;
		
		/**
		 * Constructs a new {@link TeleportType}.
		 * @param combat {@link #combat}.
		 * @param percentage {@link #percentage}.
		 */
		public TeleportPolicy(boolean combat, int percentage) {
			this.combat = combat;
			this.percentage = percentage;
		}
	}
	
	/**
	 * @return the destination
	 */
	public Position getDestination() {
		return destination;
	}
}
