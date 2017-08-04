package net.edge.content.combat.strategy.mob;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

public final class TzKihCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return character.isNpc();
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		if(victim.isPlayer()) {
			Player player = victim.toPlayer();
			String type = character.toMob().isFamiliar() ? "Spirit tz-kih" : "Tz-kih";
			Skill prayer = player.getSkills()[Skills.PRAYER];
			prayer.decreaseLevel(1);
			Skills.refresh(player, Skills.PRAYER);
			player.message("The " + type + " drained your prayer level...");
		}
		return new CombatHit(character, victim, 1, CombatType.MELEE, true);
	}

	@Override
	public int attackDelay(Actor character) {
		return 4;
	}

	@Override
	public int attackDistance(Actor character) {
		return 1;
	}

	@Override
	public int[] getMobs() {
		return new int[]{2627, 7361};
	}

}
