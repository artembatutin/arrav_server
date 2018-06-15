package net.arrav.content.skill.prayer.curses;

import net.arrav.content.skill.Skills;
import net.arrav.task.Task;
import net.arrav.world.Graphic;
import net.arrav.world.Projectile;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.player.Player;

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
