package com.rageps.combat.listener.other.prayer.regular;

import com.rageps.combat.listener.SimplifiedListener;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatConstants;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.HitIcon;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.model.Graphic;

public class RetributionListener extends SimplifiedListener<Player> {

	@Override
	public void preDeath(Actor attacker, Player defender, Hit hit) {
		defender.graphic(new Graphic(437));
		final int damage = RandomUtils.inclusive(CombatConstants.MAXIMUM_RETRIBUTION_DAMAGE);
		if(defender.inMulti()) {
			defender.getLocalMobs().stream().filter(n -> n.getPosition().withinDistance(defender.getPosition(), 2)).forEach(h -> h.damage(new Hit(damage, Hitsplat.NORMAL, HitIcon.NONE)));
			if(defender.inWilderness()) {
				defender.getLocalPlayers().stream().filter(p -> p.getPosition().withinDistance(defender.getPosition(), 2)).forEach(h -> h.damage(new Hit(damage, Hitsplat.NORMAL, HitIcon.NONE)));
			}
		} else {
			Actor victim = defender.getCombat().getLastDefender();
			if(victim != null && victim.getPosition().withinDistance(defender.getPosition(), 2)) {
				victim.damage(new Hit(RandomUtils.inclusive(damage), Hitsplat.NORMAL, HitIcon.NONE));
			}
		}
	}
}
