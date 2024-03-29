package net.arrav.world.entity.actor.combat.effect.impl;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.effect.CombatEffect;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.update.UpdateFlag;

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
			if(player.getSkullTimer().get() > 0) {
				return false;
			}
			player.getSkullTimer().set(3000);
			player.skullIcon = Player.WHITE_SKULL;
			player.getFlags().flag(UpdateFlag.APPEARANCE);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeOn(Actor c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.getSkullTimer().get() <= 0) {
				player.skullIcon = -1;
				player.getFlags().flag(UpdateFlag.APPEARANCE);
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
			player.getSkullTimer().decrementAndGet(50, 0);
		}
	}
	
	@Override
	public boolean onLogin(Actor c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.getSkullTimer().get() > 0) {
				player.skullIcon = Player.WHITE_SKULL;
				return true;
			}
			//if(FightCavesHandler.isChampion(player))
			//  player.setSkullIcon(Player.RED_SKULL);
		}
		return false;
	}
}
