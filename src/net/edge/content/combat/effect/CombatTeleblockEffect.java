package net.edge.content.combat.effect;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

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
			if(player.getTeleblockTimer().get() > 0) {
				return false;
			}
			player.getTeleblockTimer().set(3000);
			player.message("You have just been teleblocked!");
			return true;
		}
		return false;
	}

	@Override
	public boolean removeOn(Actor c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.getTeleblockTimer().get() <= 0) {
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
			player.getTeleblockTimer().decrementAndGet(50, 0);
		}
	}

	@Override
	public boolean onLogin(Actor c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.getTeleblockTimer().get() > 0)
				return true;
		}
		return false;
	}
}
