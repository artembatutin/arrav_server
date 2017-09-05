package net.edge.content.skill.prayer.curses;

import net.edge.task.Task;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

/**
 * @author Omicron
 */
public class AttackCurseEvent extends Task {
	private final Player attacker;
	private final Actor defender;

	public AttackCurseEvent(Actor defender, Player attacker) {
		super(1);
		this.defender = defender;
		this.attacker = attacker;
	}

	@Override
	public void execute() {
		if (defender.isDead() || attacker.isDead()) {
			cancel();
			return;
		}

		new Projectile(attacker, defender, 2231, 40, 60, 43, 31).sendProjectile();
		defender.graphic(new Graphic(2232, 0, 2));
		
		cancel();
	}
}
