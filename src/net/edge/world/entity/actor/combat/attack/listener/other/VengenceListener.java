package net.edge.world.entity.actor.combat.attack.listener.other;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.hit.HitIcon;
import net.edge.world.entity.actor.player.Player;

/**
 * no more bugs
 */
public class VengenceListener extends SimplifiedListener<Player> {

	public static final VengenceListener INSTANCE = new VengenceListener();

	private VengenceListener() {
	}

	@Override
	public void block(Actor attacker, Player defender, Hit hit, CombatType combatType) {
		if(hit.getDamage() < 2) {
			return;
		}
		defender.forceChat("Taste vengeance!");
		Hit recoil = new Hit((int) (hit.getDamage() * 0.75), HitIcon.DEFLECT);
		attacker.damage(recoil);
		attacker.getCombat().getDamageCache().add(defender, recoil.getDamage());
	}

	@Override
	public void finish(Player attacker, Actor defender) {
		attacker.getCombat().removeListener(this);
		attacker.venged = false;
	}

}
