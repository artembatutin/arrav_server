package net.arrav.world.entity.actor.mob;

import net.arrav.GameConstants;
import net.arrav.world.entity.actor.player.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Manages the behavior of aggressive {@link Mob}s including the way the
 * interact towards various {@link Player}s.
 * @author lare96 <http://github.com/lare96>
 */
public final class MobAggression {
	
	/**
	 * The hash collection the holds the npc identifiers of aggressive
	 * {@link Mob}s.
	 */
	public static final Set<Integer> AGGRESSIVE = new HashSet<>();
	
	/**
	 * Prompts all aggressive {@link Mob}s to attack the unsuspecting
	 * {@code player}, if they do not attack the player they will go back to
	 * their default movement coordinate state.
	 * @param player the player that will be targeted.
	 */
	public static void sequence(Player player) {
		if(player.getMinigame().isPresent() && player.getMinigame().get().aggression()) {
			return;
		}
		player.aggressionTick++;
		if(player.aggressionTick == 5) {
			player.aggressionTick = 0;
		}
		if(player.aggressionTick == 1 && (!player.getCombat().inCombat() || player.inMulti())) {
			for(Mob mob : player.getLocalMobs()) {
				if(!mob.getDefinition().isAttackable()) {
					continue;
				}
				if(!mob.getMobType().isAggressive()) {
					continue;
				}
				if(validate(mob, player)) {
					mob.getCombat().attack(player);
				} else {
					mob.getMovementCoordinator().setCoordinate(mob.isOriginalRandomWalk());
				}
			}
		}
	}
	
	/**
	 * Determines if {@code mob} is able to target {@code player}.
	 * @param mob    the mob trying to target the player.
	 * @param player the player that is being targeted by the NPC.
	 * @return {@code true} if the player can be targeted, {@code false}
	 * otherwise.
	 */
	private static boolean validate(Mob mob, Player player) {
		boolean wilderness = player.inWilderness();
		boolean retreats = mob.getDefinition().retreats() || (player.getCombat().isUnderAttack() && !player.getCombat().isAttacking(mob));
		boolean tolerance = !(wilderness || mob.getDefinition().getCombatLevel() > 126) && player.getTolerance().elapsed(GameConstants.TOLERANCE_SECONDS, TimeUnit.SECONDS);
		if(mob.getAttr().get("isRetreating").getBoolean()) {
			if(mob.getPosition().withinDistance(mob.getOriginalPosition(), 1)) {
				mob.getAttr().get("isRetreating").set(false);
			}
			return false;
		}
		if(!player.isVisible() || player.getCombat().isUnderAttack() && !player.inMulti()) {
			return false;
		}
		if(!AGGRESSIVE.contains(mob.getId()) && !wilderness || mob.getCombat().inCombat())
			return false;
		if(!mob.getPosition().withinDistance(player.getPosition(), 12)) {
			retreat(mob);//Too far.
			return false;
		}
		if(!mob.getPosition().withinDistance(player.getPosition(), GameConstants.TARGET_DISTANCE)) {
			retreat(mob);
			return false;
		}
		if(!mob.getMovementCoordinator().getBoundary().within(player.getPosition(), mob.size(), GameConstants.TARGET_DISTANCE) && retreats) {
			retreat(mob);//Retreats check.
			return false;
		}
		if(player.determineCombatLevel() > (mob.getDefinition().getCombatLevel() * 2) && !wilderness && !player.getAttr().get("ignoredAggressionLevel").getBoolean())
			return false;
		return !mob.getCombat().isAttacking() && !mob.getCombat().isUnderAttack() && !tolerance;
	}
	
	public static void retreat(Mob mob) {
		mob.getCombat().reset(false, true);
		mob.getMovementQueue().smartWalk(mob.getOriginalPosition());
		mob.getAttr().get("isRetreating").set(true);
	}
	
}
