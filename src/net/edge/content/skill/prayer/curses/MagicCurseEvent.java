package net.edge.content.skill.prayer.curses;

import net.edge.task.Task;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.CurseModifier;
import net.edge.world.entity.actor.player.Player;

import java.util.Map;

/**
 * @author Omicron
 */
public class MagicCurseEvent extends Task {
	private final Actor defender;
	private final Player attacker;

	public MagicCurseEvent(Actor defender, Player attacker) {
		super(1);
		this.attacker = attacker;
		this.defender = defender;
	}

	@Override
	public void execute() {
		if (defender.isDead() || attacker.isDead()) {
			cancel();
			return;
		}
		new Projectile(attacker, defender, 2240, 40, 60, 43, 31).sendProjectile();
		defender.graphic(new Graphic(2241, 0, 2));


		Map<Actor, CurseModifier> offensiveModifier = attacker.getCombat().curseModifiers;

		if(!offensiveModifier.containsKey(attacker)) {
			offensiveModifier.put(attacker, new CurseModifier());
			offensiveModifier.put(attacker, new CurseModifier().magic(0.05));
		}

		if(offensiveModifier.get(attacker).getMagicBonus() < 0.10) {
			offensiveModifier.get(attacker).magic(0.01);
		}

		Map<Actor, CurseModifier> defensiveModifier = defender.getCombat().curseModifiers;

		if(!defensiveModifier.containsKey(defender)) {
			defensiveModifier.put(defender, new CurseModifier().magic(-0.10));
			return;//don't decrease attack bonus further
		}

		if(defensiveModifier.get(defender).getMagicBonus() > -0.25) {
			defensiveModifier.get(defender).magic(-0.01);
		}

		cancel();
	}
}
