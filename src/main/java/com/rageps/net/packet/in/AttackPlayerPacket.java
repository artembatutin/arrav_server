package com.rageps.net.packet.in;

import com.rageps.content.minigame.Minigame;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.combat.CombatUtil;
import com.rageps.world.entity.actor.combat.magic.CombatSpell;
import com.rageps.world.entity.actor.combat.magic.lunars.LunarSpells;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.container.session.ExchangeSession;
import com.rageps.world.entity.item.container.session.ExchangeSessionManager;
import com.rageps.world.entity.item.container.session.impl.DuelSession;

import java.util.Optional;

/**
 * The message sent from the client when a player attacks another player.
 * @author lare96 <http://github.com/lare96>
 */
public final class AttackPlayerPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ATTACK_PLAYER))
			return;
		switch(opcode) {
			case 249:
				attackMagic(player, buf);
				break;
			case 153:
				attackOther(player, buf);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.ATTACK_PLAYER);
	}
	
	/**
	 * Attempts to attack a player with a magic spell.
	 * @param player the player to attempt to attack.
	 * @param buf the buffer for reading the sent data.
	 */
	private void attackMagic(Player player, GamePacket buf) {
		int index = buf.getShort(true, ByteTransform.A);
		int spellId = buf.getShort(true, ByteOrder.LITTLE);
		Player victim = World.get().getPlayers().get(index - 1);
		
		if(LunarSpells.castCombatSpells(player, victim, spellId)) {
			return;
		}
		
		CombatSpell spell = CombatSpell.get(spellId);
		if(spell == null || index < 0 || index > World.get().getPlayers().capacity() || spellId < 0 || !checkAttack(player, victim)) {
			return;
		}
		player.setSingleCast(spell);
		player.getCombat().attack(victim);
	}
	
	/**
	 * Attempts to attack a player with any other form of combat such as melee
	 * or ranged.
	 * @param player the player to attempt to attack.
	 * @param buf the buffer for reading the sent data.
	 */
	private void attackOther(Player player, GamePacket buf) {
		int index = buf.getShort(true, ByteOrder.LITTLE);
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
	 * @param victim the player that is being targeted.
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
		if(attacker.getMinigame().isPresent() && attacker.getLocation().inWilderness()) {
			attacker.message("Something went wrong there! You are still in a minigame, please re-log!");
			return false;
		}
		if(attacker.getLocation().inDuelArena() && !attacker.getMinigame().isPresent()) {
			ExchangeSessionManager.get().request(new DuelSession(attacker, victim, ExchangeSession.REQUEST));
			attacker.getMovementQueue().reset();
			return false;
		}
		Optional<Minigame> optional = MinigameHandler.getMinigame(attacker);
		if(!optional.isPresent()) {
			if(attacker.getLocation().inFunPvP() && victim.getLocation().inFunPvP()) {
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
