package com.rageps.combat.listener.other.prayer.curses.leech;

import com.rageps.content.skill.prayer.Prayer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.combat.listener.SimplifiedListener;
import com.rageps.world.entity.actor.combat.hit.Hit;

import static com.rageps.world.Animation.AnimationPriority.HIGH;

public class LeechMagicListener extends SimplifiedListener<Player> {
	
	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		if(attacker.curseManager.isActivated(Prayer.LEECH_MAGIC)) {
			defender.graphic(new Graphic(2232, 0, 2));
			attacker.animation(new Animation(12575, HIGH));
			attacker.message("You leech the targets magic.");
			attacker.curseManager.deactivate(Prayer.LEECH_MAGIC);
		}
	}
	
	@Override
	public int modifyMagicLevel(Player attacker, Actor defender, int level) {
		return attacker.curseManager.modifyOutgoingLevel(level, 5, 10, Prayer.LEECH_MAGIC);
	}
	
}