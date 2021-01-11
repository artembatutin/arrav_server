package com.rageps.content.skill.prayer.curses;

import com.rageps.content.skill.Skills;
import com.rageps.task.Task;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.model.Graphic;
import com.rageps.world.model.Projectile;

/**
 * @author Omicron
 */
public class SoulsplitEvent extends Task {
	private Actor defender;
	private Player attacker;
	private Hit hit;

	public SoulsplitEvent(Actor defender, Player attacker, Hit hit) {
		super(1);
		this.defender = defender;
		this.attacker = attacker;
		this.hit = hit;
	}

	@Override
	public void execute() {
		if(defender == null) {
			cancel();
			return;
		}

		if(!defender.isDead()) {
			if(defender.isPlayer()) {
				Player player = (Player) defender;

				player.getSkills()[Skills.PRAYER].setLevel(0, false);
				Skills.refresh(player, Skills.PRAYER);

				player.healEntity(hit.getDamage() / 5);

				new Projectile(attacker, defender, 2263, 40, 60, 43, 31).sendProjectile();
				defender.graphic(new Graphic(2264, 0, 2));
			}
		}

		cancel();
	}
}
