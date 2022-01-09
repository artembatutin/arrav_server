package com.rageps.world.entity.actor.combat.effect.impl;

import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.effect.CombatEffect;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.world.entity.sync.block.SynchronizationBlock;

/**
 * The combat effect applied when a player needs to be skulled.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatSkullEffect extends CombatEffect {
	
	/**
	 * Creates a new {@link CombatSkullEffect}.
	 */
	public CombatSkullEffect() {
		super(50);
	}
	
	@Override
	public boolean apply(Actor c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.playerData.getSkullTimer().get() > 0) {
				return false;
			}
			player.playerData.getSkullTimer().set(3000);
			player.skullIcon = Player.WHITE_SKULL;
			player.updateAppearance();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeOn(Actor c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.playerData.getSkullTimer().get() <= 0) {
				player.skullIcon = -1;
				player.updateAppearance();
				return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public void process(Actor c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			player.playerData.getSkullTimer().decrementAndGet(50, 0);
		}
	}
	
	@Override
	public boolean onLogin(Actor c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.playerData.getSkullTimer().get() > 0) {
				player.skullIcon = Player.WHITE_SKULL;
				return true;
			}
			//if(FightCavesHandler.isChampion(player))
			//  player.setSkullIcon(Player.RED_SKULL);
		}
		return false;
	}
}
