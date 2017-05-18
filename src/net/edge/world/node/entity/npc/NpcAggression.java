package net.edge.world.node.entity.npc;

import net.edge.utils.rand.RandomUtils;
import net.edge.world.GameConstants;
import net.edge.world.World;
import net.edge.world.locale.Location;
import net.edge.world.node.entity.player.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Manages the behavior of aggressive {@link Npc}s including the way the
 * interact towards various {@link Player}s.
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcAggression {
	
	/**
	 * The hash collection the holds the npc identifiers of aggressive
	 * {@link Npc}s.
	 */
	public static final Set<Integer> AGGRESSIVE = new HashSet<>();
	
	/**
	 * Prompts all aggressive {@link Npc}s to attack the unsuspecting
	 * {@code player}, if they do not attack the player they will go back to
	 * their default movement coordinate state.
	 * @param player the player that will be targeted.
	 */
	public static void sequence(Player player) {
		if(RandomUtils.inclusive(10) == 3 && (!player.getCombatBuilder().inCombat() || Location.inMultiCombat(player))) {
			for(Npc npc : player.getLocalNpcs()) {
				//if(GodwarsFaction.attack(npc)) {
				//	return;
				//}
				if(validate(npc, player, Location.inMultiCombat(npc)) && !World.getAreaManager().inArea(player, "GODWARS")) {
					npc.getCombatBuilder().attack(player);
				}/* else if(GodwarsFaction.attack(npc)) {
					//no need for code.
				}*/ else {
					npc.getMovementCoordinator().setCoordinate(npc.isOriginalRandomWalk());
				}
			}
		}
	}
	
	/**
	 * Determines if {@code npc} is able to target {@code player}.
	 * @param npc    the npc trying to target the player.
	 * @param player the player that is being targeted by the NPC.
	 * @return {@code true} if the player can be targeted, {@code false}
	 * otherwise.
	 */
	private static boolean validate(Npc npc, Player player, boolean multi) {
		boolean wilderness = Location.inWilderness(npc) && Location.inWilderness(player);
		boolean retreats = npc.getDefinition().retreats() || (player.getCombatBuilder().isBeingAttacked() && player.getCombatBuilder().getVictim() != npc);
		boolean tolerance = !(wilderness || npc.getDefinition().getCombatLevel() > 126) && player.getTolerance().elapsed(GameConstants.TOLERANCE_SECONDS, TimeUnit.SECONDS);
		if(npc.getAttr().get("isRetreating").getBoolean()) {
			if(npc.getPosition().withinDistance(npc.getOriginalPosition(), 1)) {
				npc.getAttr().get("isRetreating").set(false);
			}
			return false;
		}
		if(!AGGRESSIVE.contains(npc.getId()) && !wilderness || !npc.getDefinition().isAttackable() || npc.getCombatBuilder().inCombat())
			return false;
		if(!npc.getPosition().withinDistance(player.getPosition(), 40)) {
			retreat(npc);//Too far.
			return false;
		}
		if(!npc.getPosition().withinDistance(player.getPosition(), GameConstants.TARGET_DISTANCE) || !npc.getMovementCoordinator().getBoundary().within(player.getPosition(), npc.size(), GameConstants.TARGET_DISTANCE) && retreats) {
			retreat(npc);//Retreats check.
			return false;
		}
		if(!multi && player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked())
			return false;
		if(player.determineCombatLevel() > (npc.getDefinition().getCombatLevel() * 2) && !wilderness)
			return false;
		return !npc.getCombatBuilder().isAttacking() && !npc.getCombatBuilder().isBeingAttacked() && !tolerance;
	}
	
	public static void retreat(Npc npc) {
		npc.getCombatBuilder().reset();
		npc.getMovementQueue().smartWalk(npc.getOriginalPosition());
		npc.getAttr().get("isRetreating").set(true);
	}
	
}
