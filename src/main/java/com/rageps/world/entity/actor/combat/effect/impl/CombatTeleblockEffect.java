package com.rageps.world.entity.actor.combat.effect.impl;

import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.effect.CombatEffect;
import com.rageps.world.entity.actor.player.Player;

/**
 * The combat effect applied when a player needs to be teleblocked.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatTeleblockEffect extends CombatEffect {
	
	/**
	 * Creates a new {@link CombatTeleblockEffect}.
	 */
	public CombatTeleblockEffect() {
		super(50);
	}
	
	@Override
	public boolean apply(Actor c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.playerData.getTeleblockTimer().get() > 0) {
				return false;
			}
			player.playerData.getTeleblockTimer().set(3000);
			player.message("You have just been teleblocked!");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeOn(Actor c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.playerData.getTeleblockTimer().get() <= 0) {
				player.message("You feel the effects of the teleblock spell go away.");
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
			player.playerData.getTeleblockTimer().decrementAndGet(50, 0);
		}
	}
	
	@Override
	public boolean onLogin(Actor c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.playerData.getTeleblockTimer().get() > 0)
				return true;
		}
		return false;
	}
}
