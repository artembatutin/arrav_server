package com.rageps.combat.listener.other;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.combat.listener.SimplifiedListener;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;

/**
 * no more bugs
 */
public class VengeanceListener extends SimplifiedListener<Player> {
	
	private static final VengeanceListener INSTANCE = new VengeanceListener();
	
	private VengeanceListener() {
	}
	
	@Override
	public void block(Actor attacker, Player defender, Hit hit, CombatType combatType) {
		if(hit.getDamage() < 2) {
			return;
		}
		
		defender.forceChat("Taste vengeance!");
		defender.getCombat().removeListener(this);
		Hit recoil = new Hit((int) (hit.getDamage() * 0.75), Hitsplat.NORMAL_LOCAL);
		attacker.damage(recoil);
		attacker.getCombat().getDamageCache().add(defender, recoil);
		defender.venged = false;
	}
	
	public static VengeanceListener get() {
		return INSTANCE;
	}
	
}
