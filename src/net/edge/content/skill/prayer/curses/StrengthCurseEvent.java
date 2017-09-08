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
public class StrengthCurseEvent extends Task  {
	private final Player attacker;
	private final Actor defender;

	public StrengthCurseEvent(Actor defender, Player attacker) {
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

		new Projectile(attacker, defender, 2248, 40, 60, 43, 31).sendProjectile();
		defender.graphic(new Graphic(2250, 0, 2));


		Map<Actor, CurseModifier> offensiveModifier = attacker.getCombat().curseModifiers;

		if(!offensiveModifier.containsKey(attacker)) {
			offensiveModifier.put(attacker, new CurseModifier());
			offensiveModifier.put(attacker, new CurseModifier().strength(0.05));
		}

		if(offensiveModifier.get(attacker).getStrengthBonus() < 0.10) {
			offensiveModifier.get(attacker).strength(0.01);
		}

		Map<Actor, CurseModifier> defensiveModifier = defender.getCombat().curseModifiers;

		if(!defensiveModifier.containsKey(defender)) {
			defensiveModifier.put(defender, new CurseModifier().strength(-0.10));
			return;//don't decrease attack bonus further
		}

		if(defensiveModifier.get(defender).getStrengthBonus() > -0.25) {
			defensiveModifier.get(defender).strength(-0.01);
		}


		cancel();
	}
}
