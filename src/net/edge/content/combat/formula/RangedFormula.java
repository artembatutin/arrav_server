package net.edge.content.combat.formula;

import net.edge.Application;
import net.edge.content.combat.CombatConstants;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.magic.CombatWeaken;
import net.edge.content.combat.weapon.FightStyle;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

/**
 * Created by Dave/Ophion
 * Date: 02/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class RangedFormula implements Formula {
	
	@Override
	public int attack(Actor attacker, Actor defender) {
		float rangeLevel = attacker.isPlayer() ? attacker.toPlayer().getSkills()[Skills.RANGED].getLevel() : attacker.toMob().getDefinition().getRangedLevel();
		int correspondingBonus = attacker.isPlayer() ? attacker.toPlayer().getEquipment().getBonuses()[attacker.toPlayer().getFightType().getBonus()] : attacker.toMob().getDefinition().getCombat().getAttackRanged();
		
		if(attacker.isPlayer()) {
			Player player = (Player) attacker;
			//prayer adjustments
			if(Prayer.isActivated(player, Prayer.SHARP_EYE)) {
				rangeLevel *= 1.05;
			} else if(Prayer.isActivated(player, Prayer.HAWK_EYE)) {
				rangeLevel *= 1.1;
			} else if(Prayer.isActivated(player, Prayer.EAGLE_EYE)) {
				rangeLevel *= 1.15;
			}
			//rounding
			rangeLevel = Math.round(rangeLevel);
			//stance bonuses
			if(player.getFightType().getStyle() == FightStyle.ACCURATE) {
				rangeLevel += 3;
			}
			//add up 8
			rangeLevel += 8;
			//void range
			if(CombatUtil.isFullVoid(player) && player.getEquipment().contains(11664)) {
				rangeLevel *= 1.1;
			}
			//special attack
			if(player.isSpecialActivated()) {
				rangeLevel *= player.getCombatSpecial().getAccuracy();
			}
		} else if(attacker.isMob()) {
			Mob mob = attacker.toMob();
			if(mob.getWeakenedBy() == CombatWeaken.ATTACK_LOW || mob.getWeakenedBy() == CombatWeaken.ATTACK_HIGH)
				rangeLevel -= (int) ((mob.getWeakenedBy().getRate()) * (rangeLevel));
		}
		return Math.round(rangeLevel) * (correspondingBonus + 64);
	}
	
	@Override
	public int maxHit(Actor attacker, Actor defender) {
		int maxHit;
		if(attacker.isMob()) {
			Mob mob = (Mob) attacker;
			maxHit = mob.getDefinition().getMaxHit();
			if(defender.isPlayer()) {
				Player p = defender.toPlayer();
				if(p.isIronMan())//Iron man monsters tougher.
					maxHit *= 1.2;
			}
			return maxHit;
		}
		
		Player player = (Player) attacker;
		int bonus = player.getEquipment().getBonuses()[CombatConstants.BONUS_RANGED_STRENGTH];
		int level = player.getSkills()[Skills.RANGED].getLevel();
		//prayer adjustments
		if(Prayer.isActivated(player, Prayer.SHARP_EYE)) {
			level *= 1.05;
		} else if(Prayer.isActivated(player, Prayer.HAWK_EYE)) {
			level *= 1.1;
		} else if(Prayer.isActivated(player, Prayer.EAGLE_EYE)) {
			level *= 1.15;
		}
		//rounding
		level = Math.round(level);
		//stance bonuses
		if(player.getFightType().getStyle() == FightStyle.ACCURATE) {
			level += 3;
		}
		//add up
		level += 8;
		if(CombatUtil.isFullVoid(attacker) && player.getEquipment().contains(11665)) {
			level *= 1.1;
		}
		//rounding
		level = Math.round(level);
		
		double baseDamage = Math.floor(0.5 + level * (bonus + 64) / 640);
		if(player.isSpecialActivated()) {
			baseDamage *= player.getCombatSpecial().getStrength();
		}
		if(Application.DEBUG)
			player.message("[DEBUG]: Maximum hit this turn " + "is [" + ((int) (baseDamage * 10)) + "].");
		return (int) (baseDamage * 10);
	}
	
	@Override
	public int getMobDefence(int type, Mob mob) {
		return mob.getDefinition().getCombat().getDefenceRanged();
	}
	
	@Override
	public CombatType getType() {
		return CombatType.RANGED;
	}
}
