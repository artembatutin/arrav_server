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
 * A {@link Formula} for the melee {@link CombatType}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class MeleeFormula implements Formula {
	
	@Override
	public int attack(Actor attacker, Actor defender) {
		double level = attacker.getSkillLevel(Skills.ATTACK);
		int bonus = getOffensiveBonus(attacker, defender);
		if(attacker.isPlayer()) {
			Player player = (Player) attacker;
			//prayer adjustments
			if(Prayer.isActivated(player, Prayer.CLARITY_OF_THOUGHT)) {
				level *= 1.05;
			} else if(Prayer.isActivated(player, Prayer.IMPROVED_REFLEXES)) {
				level *= 1.1;
			} else if(Prayer.isActivated(player, Prayer.INCREDIBLE_REFLEXES)) {
				level *= 1.15;
			} else if(Prayer.isActivated(player, Prayer.CHIVALRY)) {
				level *= 1.15;
			} else if(Prayer.isActivated(player, Prayer.PIETY)) {
				level *= 1.20;
			} else if(Prayer.isActivated(player, Prayer.TURMOIL)) {
				level *= 1.15 + (defender.isPlayer() ? (defender.toPlayer().getSkills()[Skills.ATTACK].getRealLevel() / 1000D) : 0.0);
			}
			//rounding
			level = (int) level;
			//stance bonuses
			if(player.getCombat().getFightType().getStyle() == FightStyle.ACCURATE) {
				level += 3;
			} else if(player.getCombat().getFightType().getStyle() == FightStyle.CONTROLLED) {
				level += 1;
			}
			//add up 8
			level += 8;
			//void melee
			if(CombatUtil.isFullVoid(player) && player.getEquipment().contains(11665)) {
				level *= 1.1;
			}
		}
		return ((int) (level) * (bonus + 64));
	}
	
	@Override
	public int maxHit(Actor attacker, Actor defender) {
		if(attacker.isMob()) {
			Mob mob = attacker.toMob();
			int max = mob.getDefinition().getMaxHit();
			if(mob.getId() == 2026) { //Dharok the wretched
				max += (int) ((mob.getMaxHealth() / 10) - (mob.getCurrentHealth() / 10) * 0.15);
			}
			return max;
		}
		Player player = attacker.toPlayer();
		int bonus = getStrength(attacker, defender);
		double level = attacker.getSkillLevel(Skills.STRENGTH);
		//prayer adjustments
		if(Prayer.isActivated(player, Prayer.BURST_OF_STRENGTH)) {
			level *= 1.05;
		} else if(Prayer.isActivated(player, Prayer.SUPERHUMAN_STRENGTH)) {
			level *= 1.1;
		} else if(Prayer.isActivated(player, Prayer.ULTIMATE_STRENGTH)) {
			level *= 1.15;
		} else if(Prayer.isActivated(player, Prayer.CHIVALRY)) {
			level *= 1.18;
		} else if(Prayer.isActivated(player, Prayer.PIETY)) {
			level *= 1.23;
		} else if(Prayer.isActivated(player, Prayer.TURMOIL)) {
			//1000 because if strength is 99 / 100 * 10 = 9.9 which is way to much.
			level *= 1.23 + (defender.isPlayer() ? (defender.toPlayer().getSkills()[Skills.STRENGTH].getRealLevel() / 1000D) : 0.0);
		}
		//rounding
		level = (int) level;
		//stance bonuses
		if(player.getCombat().getFightType().getStyle() == FightStyle.AGGRESSIVE) {
			level += 3;
		} else if(player.getCombat().getFightType().getStyle()  == FightStyle.CONTROLLED) {
			level += 1;
		}
		//add up 8
		level += 8;
		//void melee and dharok
		if(CombatUtil.isFullVoid(player) && player.getEquipment().contains(11665)) {
			level *= 1 + 0.1;
		}
		return (int) ((0.5 + level * (bonus + 64) / 640) * 10);
	}
	
	@Override
	public CombatType getType() {
		return CombatType.MELEE;
	}
	
	@Override
	public int getStrength(Actor attacker, Actor defender) {
		if (attacker.isPlayer()) {
			return attacker.toPlayer().getEquipment().getBonuses()[CombatConstants.BONUS_STRENGTH];
		}
		return attacker.toMob().getDefinition().getCombat().getStrengthLevel();
	}
	
	@Override
	public int getOffensiveBonus(Actor attacker, Actor defender) {
		if(attacker.isPlayer()) {
			Player player = attacker.toPlayer();
			FightType fightType = player.getCombat().getFightType();
			return player.getEquipment().getBonuses()[fightType.getBonus()];
		}
		return attacker.toMob().getDefinition().getCombat().getAttackMelee();
	}
	
	@Override
	public int getDefensiveBonus(Actor attacker, Actor defender) {
		FightType fightType = attacker.getCombat().getFightType();
		if (defender.isPlayer()) {
			Player player = defender.toPlayer();
			return player.getEquipment().getBonuses()[fightType.getCorrespondingBonus()];
		}
		if (fightType.getCorrespondingBonus() == CombatConstants.DEFENCE_STAB) {
			return defender.toMob().getDefinition().getCombat().getDefenceStab();
		} else if (fightType.getCorrespondingBonus() == CombatConstants.DEFENCE_CRUSH) {
			return defender.toMob().getDefinition().getCombat().getDefenceCrush();
		} else if (fightType.getCorrespondingBonus() == CombatConstants.DEFENCE_SLASH) {
			return defender.toMob().getDefinition().getCombat().getDefenceSlash();
		}
		return 0;
	}
}
