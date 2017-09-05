package net.edge.content.skill.prayer.curses;

import net.edge.task.Task;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

/**
 * @author Omicron
 */
public class RangeCurseEvent extends Task {
	private final Player attacker;
	private final Actor defender;

	public RangeCurseEvent(Actor defender, Player attacker) {
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
		new Projectile(attacker, defender, 2236, 40, 60, 43, 31).sendProjectile();
		defender.graphic(new Graphic(2237, 0, 2));
		cancel();
	}
}
