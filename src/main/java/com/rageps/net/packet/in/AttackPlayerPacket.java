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
		if(spell == null || index < 0 || index > World.get().getPlayers().capacity() || spellId < 0 || !CombatUtil.checkAttack(player, victim)) {
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
		if(index < 0 || index > World.get().getPlayers().capacity())
			return;
		if(!CombatUtil.checkAttack(player, victim)) {
			return;
		}
		player.getCombat().attack(victim);
	}
	

}
