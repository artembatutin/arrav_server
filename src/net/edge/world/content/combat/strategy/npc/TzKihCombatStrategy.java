package net.edge.world.content.combat.strategy.npc;

import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.content.skill.Skill;
import net.edge.world.content.skill.Skills;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.player.Player;

public final class TzKihCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return character.isNpc();
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		if(victim.isPlayer()) {
			Player player = victim.toPlayer();
			String type = character.toNpc().isFamiliar() ? "Spirit tz-kih" : "Tz-kih";
			Skill prayer = player.getSkills()[Skills.PRAYER];
			prayer.decreaseLevel(1);
			Skills.refresh(player, Skills.PRAYER);
			player.message("The " + type + " drained your prayer level...");
		}
		return new CombatSessionData(character, victim, 1, CombatType.MELEE, true);
	}

	@Override
	public int attackDelay(EntityNode character) {
		return 4;
	}

	@Override
	public int attackDistance(EntityNode character) {
		return 1;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{2627, 7361};
	}

}
