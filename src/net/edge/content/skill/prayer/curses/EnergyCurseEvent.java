package net.edge.content.skill.prayer.curses;

import net.edge.task.Task;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

/**
 * @author Omicron
 */
public class EnergyCurseEvent extends Task {
	private final Actor defender;
	private final Player attacker;

	public EnergyCurseEvent(Actor defender, Player attacker) {
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
		
		new Projectile(attacker, defender, 2252, 40, 60, 41, 31).sendProjectile();
		defender.graphic(new Graphic(2254, 0, 2));
		cancel();
	}
}
