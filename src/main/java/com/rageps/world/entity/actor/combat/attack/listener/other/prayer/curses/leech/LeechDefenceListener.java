package com.rageps.world.entity.actor.combat.attack.listener.other.prayer.curses.leech;

import com.rageps.content.skill.prayer.Prayer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.attack.listener.SimplifiedListener;
import com.rageps.world.entity.actor.combat.hit.Hit;

import static com.rageps.world.Animation.AnimationPriority.HIGH;

public class LeechDefenceListener extends SimplifiedListener<Player> {

	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		if(attacker.curseManager.isActivated(Prayer.LEECH_DEFENCE)) {
			defender.graphic(new Graphic(2232, 0, 2));
			attacker.animation(new Animation(12575, HIGH));
			attacker.message("You leech the targets defence.");
			attacker.curseManager.deactivate(Prayer.LEECH_DEFENCE);
		}
	}

	@Override
	public int modifyDefenceLevel(Actor attacker, Player defender, int level) {
		return defender.curseManager.modifyOutgoingLevel(level, 5, 10, Prayer.LEECH_DEFENCE);
	}

}
