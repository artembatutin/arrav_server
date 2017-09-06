package net.edge.world.entity.actor.combat.formula;

import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatConstants;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.CombatUtil;
import net.edge.world.entity.actor.combat.attack.FightStyle;
import net.edge.world.entity.actor.player.Player;

/**
 * A {@link Formula} for the magic {@link CombatType}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class MagicFormula implements Formula {
	
	@Override
	public int attack(Actor attacker, Actor defender) {
		double level = attacker.getSkillLevel(Skills.MAGIC);
		int bonus = getOffensiveBonus(attacker, defender);
		if(attacker.isPlayer()) {
			Player player = attacker.toPlayer();
			//prayer adjustments
			if(Prayer.isActivated(player, Prayer.MYSTIC_MIGHT)) {
				level *= 1.15;
			} else if(Prayer.isActivated(player, Prayer.MYSTIC_LORE)) {
				level *= 1.1;
			} else if(Prayer.isActivated(player, Prayer.MYSTIC_WILL)) {
				level *= 1.05;
			}
			//rounding and add up 8
			level = (int) level;
			level += 8;
			//void magic
			if(CombatUtil.isFullVoid(player) && player.getEquipment().contains(11663)) {
				level *= 1.45;
			}
		}
		return (int) (level * (bonus + 64));
	}
	
	@Override
	public int defence(Actor attacker, Actor defender) {
		double level = defender.getSkillLevel(Skills.DEFENCE);
		int bonus = getDefensiveBonus(attacker, defender);
		if(defender.isPlayer()) {
			Player player = (Player) defender;
			if(Prayer.isActivated(player, Prayer.PIETY)) {
				level *= 1.25;
			} else if(Prayer.isActivated(player, Prayer.CHIVALRY)) {
				level *= 1.20;
			} else if(Prayer.isActivated(player, Prayer.STEEL_SKIN)) {
				level *= 1.15;
			} else if(Prayer.isActivated(player, Prayer.ROCK_SKIN)) {
				level *= 1.1;
			} else if(Prayer.isActivated(player, Prayer.THICK_SKIN)) {
				level *= 1.05;
			}
			//rounding and add up 8
			level = (int) level;
			level += 8;
			//stance bonuses
			if(player.getCombat().getFightType().getStyle() == FightStyle.DEFENSIVE) {
				level += 3;
			} else if(player.getCombat().getFightType().getStyle() == FightStyle.CONTROLLED) {
				level += 1;
			}
			//unique to magic defence
			level = (int) (level * 0.3);
			float magicLevel = defender.getSkillLevel(Skills.MAGIC);
			if(Prayer.isActivated(player, Prayer.MYSTIC_MIGHT)) {
				magicLevel *= 1.15;
			} else if(Prayer.isActivated(player, Prayer.MYSTIC_LORE)) {
				magicLevel *= 1.1;
			} else if(Prayer.isActivated(player, Prayer.MYSTIC_WILL)) {
				magicLevel *= 1.05;
			}
			level += (int) (magicLevel * 0.7);
		}
		return ((int) (level) * (bonus + 64));
	}
	
	@Override
	public int maxHit(Actor attacker, Actor defender) {
		return 0;//TODO: spells?
	}
	
	@Override
	public CombatType getType() {
		return CombatType.MAGIC;
	}
	
	@Override
	public int getStrength(Actor attacker, Actor defender) {
		return 0;
	}
	
	@Override
	public int getOffensiveBonus(Actor attacker, Actor defender) {
		if (attacker.isPlayer()) {
			return attacker.toPlayer().getEquipment().getBonuses()[CombatConstants.ATTACK_MAGIC];
		}
		return attacker.toMob().getDefinition().getCombat().getAttackMagic();
	}
	
	@Override
	public int getDefensiveBonus(Actor attacker, Actor defender) {
		if (defender.isPlayer()) {
			return defender.toPlayer().getEquipment().getBonuses()[CombatConstants.DEFENCE_MAGIC];
		}
		return defender.toMob().getDefinition().getCombat().getDefenceMagic();
	}
	
}
