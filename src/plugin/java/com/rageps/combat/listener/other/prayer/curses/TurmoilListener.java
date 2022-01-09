package com.rageps.combat.listener.other.prayer.curses;

import com.rageps.content.skill.Skill;
import com.rageps.content.skill.Skills;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.Actor;
import com.rageps.combat.listener.SimplifiedListener;

public class TurmoilListener extends SimplifiedListener<Player> {

	@Override
	public int modifyAttackLevel(Player attacker, Actor defender, int level) {
		double bonus = 1.15;

		if(defender.isPlayer()) {
			Skill skill = defender.toPlayer().getSkills()[Skills.ATTACK];
			int current = skill.getCurrentLevel();
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
			int current = skill.getCurrentLevel();
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
			int current = skill.getCurrentLevel();
			int max = skill.getRealLevel();
			bonus += current * 0.15 / max;
		}

		return (int) (level * bonus);
	}

}
