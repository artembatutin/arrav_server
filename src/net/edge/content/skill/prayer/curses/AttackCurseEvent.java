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

		Map<Actor, CurseModifier> offensiveModifier = attacker.getCombat().curseModifiers;

		if(!offensiveModifier.containsKey(defender)) {
			offensiveModifier.put(defender, new CurseModifier());
		}

		if(offensiveModifier.get(defender).getAttackBonus() < 0.05) {
			offensiveModifier.get(defender).attack(0.01);
		}

		Map<Actor, CurseModifier> defensiveModifier = defender.getCombat().curseModifiers;

		if(!defensiveModifier.containsKey(attacker)) {
			defensiveModifier.put(attacker, new CurseModifier().attack(-0.10));
			return;//don't decrease attack bonus further
		}

		if(defensiveModifier.get(attacker).getAttackBonus() > -0.25) {
			defensiveModifier.get(attacker).attack(-0.01);
		}


		cancel();
	}
}
