package net.edge.net.packet.in;

import net.edge.content.minigame.Minigame;
import net.edge.content.minigame.MinigameHandler;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.World;
import net.edge.world.entity.actor.combat.CombatUtil;
import net.edge.world.entity.actor.combat.magic.CombatSpell;
import net.edge.world.entity.actor.combat.magic.lunars.LunarSpells;
import net.edge.world.entity.actor.combat.strategy.player.PlayerMagicStrategy;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.item.container.session.ExchangeSession;
import net.edge.world.entity.item.container.session.ExchangeSessionManager;
import net.edge.world.entity.item.container.session.impl.DuelSession;
import net.edge.world.locale.loc.Location;

import java.util.Optional;

/**
 * The message sent from the client when a player attacks another player.
 * @author lare96 <http://github.com/lare96>
 */
public final class AttackPlayerPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ATTACK_PLAYER))
			return;
		switch(opcode) {
			case 249:
				attackMagic(player, payload);
				break;
			case 153:
				attackOther(player, payload);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.ATTACK_PLAYER);
	}
	
	/**
	 * Attempts to attack a player with a magic spell.
	 * @param player  the player to attempt to attack.
	 * @param payload the payloadfer for reading the sent data.
	 */
	private void attackMagic(Player player, IncomingMsg payload) {
		int index = payload.getShort(true, ByteTransform.A);
		int spellId = payload.getShort(true, ByteOrder.LITTLE);
		Player victim = World.get().getPlayers().get(index - 1);
		
		if(LunarSpells.castCombatSpells(player, victim, spellId)) {
			return;
		}
		
		CombatSpell spell = CombatSpell.get(spellId);
		if(spell == null || index < 0 || index > World.get().getPlayers().capacity() || spellId < 0 || !checkAttack(player, victim)) {
			return;
		}
		player.getCombat().setStrategy(new PlayerMagicStrategy(spell));
		player.getCombat().attack(victim);
	}
	
	/**
	 * Attempts to attack a player with any other form of combat such as melee
	 * or ranged.
	 * @param player  the player to attempt to attack.
	 * @param payload the payload for reading the sent data.
	 */
	private void attackOther(Player player, IncomingMsg payload) {
		int index = payload.getShort(true, ByteOrder.LITTLE);
		Player victim = World.get().getPlayers().get(index - 1);
		if(index < 0 || index > World.get().getPlayers().capacity() || !checkAttack(player, victim))
			return;
		if(!checkAttack(player, victim)) {
			return;
		}
		player.getCombat().attack(victim);
	}
	
	/**
	 * Determines if an attack can be made by the {@code attacker} on
	 * {@code victim}.
	 * @param attacker the player that is trying to attack.
	 * @param victim   the player that is being targeted.
	 * @return {@code true} if an attack can be made, {@code false} otherwise.
	 */
	private boolean checkAttack(Player attacker, Player victim) {
		if(victim == null || victim.same(attacker)) {
			attacker.getMovementQueue().reset();
			return false;
		}
		if(!attacker.isVisible()) {
			attacker.message("You're invisible and unable to attack other players.");
			return false;
		}
		if(!attacker.inMulti() && attacker.getCombat().isUnderAttack() && !attacker.getCombat().isUnderAttackBy(victim)) {
			attacker.message("You are already under attack!");
			attacker.getMovementQueue().reset();
			return false;
		}
		if(attacker.getMinigame().isPresent() && Location.inWilderness(attacker)) {
			attacker.message("Something went wrong there! You are still in a minigame, please re-log!");
			return false;
		}
		if(Location.inDuelArena(attacker) && !attacker.getMinigame().isPresent()) {
			ExchangeSessionManager.get().request(new DuelSession(attacker, victim, ExchangeSession.REQUEST));
			attacker.getMovementQueue().reset();
			return false;
		}
		Optional<Minigame> optional = MinigameHandler.getMinigame(attacker);
		if(!optional.isPresent()) {
			if(Location.inFunPvP(attacker) && Location.inFunPvP(victim)) {
				return true;
			}
			if(!attacker.inWilderness() || !victim.inWilderness()) {
				attacker.message("Both you and " + victim.getFormatUsername() + " need to be in the wilderness to fight!");
				attacker.getMovementQueue().reset();
				return false;
			}
			int combatDifference = CombatUtil.combatLevelDifference(attacker.determineCombatLevel(), victim.determineCombatLevel());
			if(combatDifference > attacker.getWildernessLevel() || combatDifference > victim.getWildernessLevel()) {
				attacker.message("Your combat level difference is too great to attack that player here.");
				attacker.getMovementQueue().reset();
				return false;
			}
		}
		return true;
	}
}
