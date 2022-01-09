package com.rageps.combat.listener.other.prayer.regular;

import com.rageps.combat.listener.SimplifiedListener;
import com.rageps.content.skill.prayer.Prayer;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.combat.CombatUtil;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.player.Player;

import java.util.concurrent.ThreadLocalRandom;

public class ProtectionPrayerListener extends SimplifiedListener<Player> {

	@Override
	public void block(Actor attacker, Player defender, Hit hit, CombatType combatType) {
		if(Prayer.isAnyActivated(defender, CombatUtil.getProtectingPrayer(combatType))) {

			//todo look into extending this to override.

			//reduce the damage.
			int damage = hit.getDamage();
			double mod = 0.5;
			int soak = ((int)(damage * mod));
			hit.setSoak(soak);
			hit.setDamage(damage - soak);

			mod = Math.round(ThreadLocalRandom.current().nextDouble() * 100.0) / 100.0;
			if (mod <= CombatUtil.PRAYER_ACCURACY_REDUCTION) {
				hit.setAccurate(false);
			}

		}
		super.block(attacker, defender, hit, combatType);
	}

}
