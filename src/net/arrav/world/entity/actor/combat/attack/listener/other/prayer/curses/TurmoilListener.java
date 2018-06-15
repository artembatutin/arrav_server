package net.arrav.world.entity.actor.combat.attack.listener.other.prayer.curses;

import net.arrav.content.skill.Skill;
import net.arrav.content.skill.Skills;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.arrav.world.entity.actor.player.Player;

public class TurmoilListener extends SimplifiedListener<Player> {

	@Override
	public int modifyAttackLevel(Player attacker, Actor defender, int level) {
		double bonus = 1.15;

		if(defender.isPlayer()) {
			Skill skill = defender.toPlayer().getSkills()[Skills.ATTACK];
			int current = skill.getLevel();
			int max = skill.getRealLevel();
			bonus += current * 0.15 / max;
		}

		return (int) (level * bonus);
	}

	@Override
	public int modifyStrengthLevel(Player attacker, Actor defender, int level) {
		double bonus = 1.23;

		if(defender.isPlayer()) {
			Skill skill = defender.toPlayer().getSkills()[Skills.STRENGTH];
			int current = skill.getLevel();
			int max = skill.getRealLevel();
			bonus += current * 0.10 / max;
		}

		return (int) (level * bonus);
	}

	@Override
	public int modifyDefenceLevel(Actor attacker, Player defender, int level) {
		double bonus = 1.15;

		if(attacker.isPlayer()) {
			Skill skill = attacker.toPlayer().getSkills()[Skills.DEFENCE];
			int current = skill.getLevel();
			int max = skill.getRealLevel();
			bonus += current * 0.15 / max;
		}

		return (int) (level * bonus);
	}

}
