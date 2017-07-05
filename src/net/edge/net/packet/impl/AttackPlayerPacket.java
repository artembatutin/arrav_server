package net.edge.net.packet.impl;

import net.edge.content.combat.Combat;
import net.edge.content.combat.magic.CombatSpells;
import net.edge.content.combat.magic.lunars.LunarSpells;
import net.edge.world.node.item.container.session.ExchangeSession;
import net.edge.world.node.item.container.session.impl.DuelSession;
import net.edge.content.minigame.Minigame;
import net.edge.content.minigame.MinigameHandler;
import net.edge.locale.loc.Location;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.PacketReader;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;

import java.util.Optional;

/**
 * The message sent from the client when a player attacks another player.
 * @author lare96 <http://github.com/lare96>
 */
public final class AttackPlayerPacket implements PacketReader {
	
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
		
		Optional<CombatSpells> spell = CombatSpells.getSpell(spellId);
		
		if(!spell.isPresent()) {
			LunarSpells.castCombatSpells(player, victim, spellId);
			return;
		}
		
		if(index < 0 || index > World.get().getPlayers().capacity() || spellId < 0 || !checkAttack(player, victim)) {
			return;
		}
		
		player.setCastSpell(spell.get().getSpell());
		player.getCombatBuilder().attack(victim);
	}
	
	/**
	 * Attempts to attack a player with any other form of combat such as melee
	 * or ranged.
	 * @param player  the player to attempt to attack.
	 * @param payload the payloadfer for reading the sent data.
	 */
	private void attackOther(Player player, IncomingMsg payload) {
		int index = payload.getShort(true, ByteOrder.LITTLE);
		Player victim = World.get().getPlayers().get(index - 1);
		if(index < 0 || index > World.get().getPlayers().capacity() || !checkAttack(player, victim))
			return;
		player.getCombatBuilder().attack(victim);
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
		if(!Location.inMultiCombat(attacker) && attacker.getCombatBuilder().isBeingAttacked() && attacker.getCombatBuilder().getAggressor() != victim && attacker.getCombatBuilder().pjingCheck()) {
			attacker.message("You are already under attack!");
			attacker.getMovementQueue().reset();
			return false;
		}
		if(Location.inDuelArena(attacker)) {
			World.getExchangeSessionManager().request(new DuelSession(attacker, victim, ExchangeSession.REQUEST));
			attacker.getMovementQueue().reset();
			return false;
		}
		Optional<Minigame> optional = MinigameHandler.getMinigame(attacker);
		if(!optional.isPresent()) {
			if(Location.inFunPvP(attacker) && Location.inFunPvP(victim)) {
				return true;
			}
			if(!Location.inWilderness(attacker) || !Location.inWilderness(victim)) {
				attacker.message("Both you and " + victim.getFormatUsername() + " need to be in the wilderness" + " to fight!");
				attacker.getMovementQueue().reset();
				return false;
			}
			int combatDifference = Combat.combatLevelDifference(attacker.determineCombatLevel(), victim.determineCombatLevel());
			if(combatDifference > attacker.getWildernessLevel() || combatDifference > victim.getWildernessLevel()) {
				attacker.message("Your combat level " + "difference is too great to attack that player here.");
				attacker.getMovementQueue().reset();
				return false;
			}
		}
		return true;
	}
}
