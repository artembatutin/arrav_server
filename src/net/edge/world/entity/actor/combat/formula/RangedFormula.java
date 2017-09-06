package net.edge.world.entity.actor.combat.formula;

import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatConstants;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.CombatUtil;
import net.edge.world.entity.actor.combat.attack.FightStyle;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

/**
 * A {@link Formula} for the ranged {@link CombatType}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class RangedFormula implements Formula {
	
	@Override
	public int attack(Actor attacker, Actor defender) {
		double level = attacker.getSkillLevel(Skills.RANGED);
		int bonus = getOffensiveBonus(attacker, defender);
		if(attacker.isPlayer()) {
			Player player = (Player) attacker;
			//prayer adjustments
			if(Prayer.isActivated(player, Prayer.SHARP_EYE)) {
				level *= 1.05;
			} else if(Prayer.isActivated(player, Prayer.HAWK_EYE)) {
				level *= 1.1;
			} else if(Prayer.isActivated(player, Prayer.EAGLE_EYE)) {
				level *= 1.15;
			}
			//rounding
			level = (int) level;
			//stance bonuses
			if(player.getCombat().getFightType().getStyle() == FightStyle.ACCURATE) {
				level += 3;
			}
			//add up 8
			level += 8;
			//void range
			if(CombatUtil.isFullVoid(player) && player.getEquipment().contains(11664)) {
				level *= 1.1;
			}
		}
		return ((int) level) * (bonus + 64);
	}
	
	@Override
	public int maxHit(Actor attacker, Actor defender) {
		if(attacker.isMob()) {
			Mob mob = (Mob) attacker;
			int max = mob.getDefinition().getMaxHit();
			return max;
		}
		
		Player player = (Player) attacker;
		int bonus = getOffensiveBonus(attacker, defender);
		double level = attacker.getSkillLevel(Skills.STRENGTH);
		//prayer adjustments
		if(Prayer.isActivated(player, Prayer.SHARP_EYE)) {
			level *= 1.05;
		} else if(Prayer.isActivated(player, Prayer.HAWK_EYE)) {
			level *= 1.1;
		} else if(Prayer.isActivated(player, Prayer.EAGLE_EYE)) {
			level *= 1.15;
		}
		//rounding
		level = (int) level;
		//stance bonuses
		if(player.getCombat().getFightType().getStyle() == FightStyle.ACCURATE) {
			level += 3;
		}
		//add up
		level += 8;
		if(CombatUtil.isFullVoid(attacker) && player.getEquipment().contains(11665)) {
			level *= 1.1;
		}
		//rounding
		level = (int) level;
		return (int) ((0.5 + level * (bonus + 64) / 640) * 10);
	}
	
	@Override
	public CombatType getType() {
		return CombatType.RANGED;
	}
	
	@Override
	public int getStrength(Actor attacker, Actor defender) {
		if (attacker.isPlayer()) {
			return attacker.toPlayer().getEquipment().getBonuses()[CombatConstants.BONUS_RANGED_STRENGTH];
		}
		return attacker.toMob().getDefinition().getCombat().getStrengthLevel();
	}
	
	@Override
	public int getOffensiveBonus(Actor attacker, Actor defender) {
		if (attacker.isPlayer()) {
			Player player = attacker.toPlayer();
			FightType fightType = player.getCombat().getFightType();
			return player.getEquipment().getBonuses()[fightType.getBonus()];
		}
		return attacker.toMob().getDefinition().getCombat().getAttackRanged();
	}
	
	@Override
	public int getDefensiveBonus(Actor attacker, Actor defender) {
		if (defender.isPlayer()) {
			Player player = defender.toPlayer();
			FightType fightType = attacker.getCombat().getFightType();
			return player.getEquipment().getBonuses()[fightType.getCorrespondingBonus()];
		}
		return defender.toMob().getDefinition().getCombat().getDefenceMagic();
	}
}
