package com.rageps.content.skill.prayer.curses;

import com.rageps.task.Task;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.model.Graphic;
import com.rageps.world.model.Projectile;

/**
 * @author Omicron
 */
public class SpecialCurseEvent extends Task {
	private final Actor defender;
	private final Player attacker;

	public SpecialCurseEvent(Actor defender, Player attacker) {
		super(1);
		this.attacker = attacker;
		this.defender = defender;
	}

	@Override
	public void execute() {
		if(defender.isDead() || attacker.isDead()) {
			cancel();
			return;
		}
		new Projectile(attacker, defender, 2256, 40, 60, 43, 31).sendProjectile();
		defender.graphic(new Graphic(2258, 0, 2));
		cancel();
	}
}
