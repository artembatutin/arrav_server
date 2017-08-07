package net.edge.content.combat.strategy.mob;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.Strategy;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

public final class TzKihStrategy implements Strategy {

	@Override
	public boolean canOutgoingAttack(Actor actor, Actor victim) {
		return actor.isMob();
	}

	@Override
	public CombatHit outgoingAttack(Actor actor, Actor victim) {
		if(victim.isPlayer()) {
			Player player = victim.toPlayer();
			String type = actor.toMob().isFamiliar() ? "Spirit tz-kih" : "Tz-kih";
			Skill prayer = player.getSkills()[Skills.PRAYER];
			prayer.decreaseLevel(1);
			Skills.refresh(player, Skills.PRAYER);
			player.message("The " + type + " drained your prayer level...");
		}
		return new CombatHit(actor, victim, 1, CombatType.MELEE, true);
	}

	@Override
	public int attackDelay(Actor actor) {
		return 4;
	}

	@Override
	public int attackDistance(Actor actor) {
		return 1;
	}

	@Override
	public int[] getMobs() {
		return new int[]{2627, 7361};
	}

}
