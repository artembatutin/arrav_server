package com.rageps.combat.listener.other.prayer.curses.leech;

import com.rageps.content.skill.prayer.Prayer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.combat.listener.SimplifiedListener;
import com.rageps.world.entity.actor.combat.hit.Hit;

import static com.rageps.world.model.Animation.AnimationPriority.HIGH;

public class LeechAttackListener extends SimplifiedListener<Player> {
	
	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		if(attacker.curseManager.isActivated(Prayer.LEECH_ATTACK)) {
			defender.graphic(new Graphic(2232, 0, 2));
			attacker.animation(new Animation(12575, HIGH));
			attacker.message("You leech the targets attack.");
			attacker.curseManager.deactivate(Prayer.LEECH_ATTACK);
		}
	}
	
	@Override
	public int modifyAttackLevel(Player attacker, Actor defender, int level) {
		return attacker.curseManager.modifyOutgoingLevel(level, 5, 10, Prayer.LEECH_ATTACK);
	}
	
}
