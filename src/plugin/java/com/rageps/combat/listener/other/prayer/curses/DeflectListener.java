package com.rageps.combat.listener.other.prayer.curses;

import com.rageps.combat.listener.SimplifiedListener;
import com.rageps.combat.listener.other.prayer.regular.ProtectionPrayerListener;
import com.rageps.content.skill.Skill;
import com.rageps.content.skill.Skills;
import com.rageps.content.skill.prayer.Prayer;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.Visualize;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.combat.CombatUtil;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.HitIcon;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.player.Player;

import java.util.concurrent.ThreadLocalRandom;

public class DeflectListener extends ProtectionPrayerListener {

	private final Visualize VISUALIZATION = new Visualize(new Animation(12573), new Graphic(2228, 2));

	@Override
	public void block(Actor attacker, Player defender, Hit hit, CombatType combatType) {
		if(Prayer.isAnyActivated(defender, CombatUtil.getProtectingPrayer(combatType)) && hit.getDamage() != 0) {
			VISUALIZATION.play(defender);
			int deflectDamage = (int) (hit.getDamage() * 0.20);
			attacker.damage(new Hit(deflectDamage, Hitsplat.NORMAL, HitIcon.DEFLECT));
		}
		super.block(attacker, defender, hit, combatType);
	}
}
