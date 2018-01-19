package net.arrav.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.arrav.content.minigame.Minigame;
import net.arrav.content.minigame.MinigameHandler;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.World;
import net.arrav.world.entity.actor.combat.CombatUtil;
import net.arrav.world.entity.actor.combat.magic.CombatSpell;
import net.arrav.world.entity.actor.combat.magic.lunars.LunarSpells;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager;
import net.arrav.world.entity.item.container.session.ExchangeSession;
import net.arrav.world.entity.item.container.session.ExchangeSessionManager;
import net.arrav.world.entity.item.container.session.impl.DuelSession;
import net.arrav.world.locale.loc.Location;

import java.util.Optional;

/**
 * The message sent from the client when a player attacks another player.
 * @author lare96 <http://github.com/lare96>
 */
public final class AttackPlayerPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
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
	 * @param player  the player to attempt to attack.
	 * @param buf the buffer for reading the sent data.
	 */
	private void attackMagic(Player player, ByteBuf buf) {
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
	 * @param player  the player to attempt to attack.
	 * @param buf the buffer for reading the sent data.
	 */
	private void attackOther(Player player, ByteBuf buf) {
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
