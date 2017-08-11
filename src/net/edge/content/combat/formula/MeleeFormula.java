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
public class MeleeFormula implements Formula {
	
	@Override
	public int attack(Actor attacker, Actor defender) {
		float attackLevel = attacker.isPlayer() ? attacker.toPlayer().getSkills()[Skills.ATTACK].getLevel() : attacker.toMob().getDefinition().getAttackLevel();
		int correspondingBonus = attacker.isPlayer() ? attacker.toPlayer().getEquipment().getBonuses()[attacker.toPlayer().getFightType().getBonus()] : attacker.toMob().getDefinition().getCombat().getAttackMelee();
		
		if(attacker.isPlayer()) {
			Player player = (Player) attacker;
			//prayer adjustments
			if(Prayer.isActivated(player, Prayer.CLARITY_OF_THOUGHT)) {
				attackLevel *= 1.05;
			} else if(Prayer.isActivated(player, Prayer.IMPROVED_REFLEXES)) {
				attackLevel *= 1.1;
			} else if(Prayer.isActivated(player, Prayer.INCREDIBLE_REFLEXES)) {
				attackLevel *= 1.15;
			} else if(Prayer.isActivated(player, Prayer.CHIVALRY)) {
				attackLevel *= 1.15;
			} else if(Prayer.isActivated(player, Prayer.PIETY)) {
				attackLevel *= 1.20;
			} else if(Prayer.isActivated(player, Prayer.TURMOIL)) {
				attackLevel += 10;
			}
			//rounding
			attackLevel = Math.round(attackLevel);
			//stance bonuses
			if(player.getFightType().getStyle() == FightStyle.ACCURATE) {
				attackLevel += 3;
			} else if(player.getFightType().getStyle() == FightStyle.CONTROLLED) {
				attackLevel += 1;
			}
			//add up 8
			attackLevel += 8;
			//void melee
			if(CombatUtil.isFullVoid(player) && player.getEquipment().contains(11665)) {
				attackLevel *= 1.1;
			}
			//special attack
			if(player.isSpecialActivated()) {
				attackLevel *= player.getCombatSpecial().getAccuracy();
			}
		} else if(attacker.isMob()) {
			Mob mob = attacker.toMob();
			if(mob.getWeakenedBy() == CombatWeaken.ATTACK_LOW || mob.getWeakenedBy() == CombatWeaken.ATTACK_HIGH)
				attackLevel -= (int) ((mob.getWeakenedBy().getRate()) * (attackLevel));
		}
		return Math.round(attackLevel) * (correspondingBonus + 64);
	}
	
	@Override
	public int maxHit(Actor attacker, Actor defender) {
		int maxHit;
		if(attacker.isMob()) {
			Mob mob = attacker.toMob();
			maxHit = mob.getDefinition().getMaxHit();
			if(defender.isPlayer()) {
				Player p = defender.toPlayer();
				if(p.isIronMan())//Iron man monsters tougher.
					maxHit *= 1.2;
			}
			if(mob.getWeakenedBy() == CombatWeaken.STRENGTH_LOW || mob.getWeakenedBy() == CombatWeaken.STRENGTH_HIGH)
				maxHit -= (int) ((mob.getWeakenedBy().getRate()) * (maxHit));
			if(mob.getId() == 2026) { //Dharok the wretched
				maxHit += (int) ((mob.getMaxHealth() / 10) - (mob.getCurrentHealth() / 10) * 0.15);
			}
			return maxHit;
		}
		
		Player player = (Player) attacker;
		float level = player.getSkills()[Skills.STRENGTH].getLevel();
		int bonus = player.getEquipment().getBonuses()[CombatConstants.BONUS_STRENGTH];
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
			level *= (defender.toPlayer().getSkills()[Skills.STRENGTH].getRealLevel() / 1000) * 10;
		}
		//rounding
		level = Math.round(level);
		//stance bonuses
		if(player.getFightType().getStyle() == FightStyle.AGGRESSIVE) {
			level += 3;
		} else if(player.getFightType().getStyle() == FightStyle.CONTROLLED) {
			level += 1;
		}
		//add up 8
		level += 8;
		//void melee and dharok
		if(CombatUtil.isFullDharoks(player)) {
			level *= (((player.getMaximumHealth()-player.getCurrentHealth()) / 10) * 0.009) + 1;
		}
	if(CombatUtil.isFullVoid(player) && player.getEquipment().contains(11665)) {
			level *= 0.1;
		}
		
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
		if(type == CombatConstants.DEFENCE_CRUSH)
			return mob.getDefinition().getCombat().getDefenceCrush();
		if(type == CombatConstants.DEFENCE_SLASH)
			return mob.getDefinition().getCombat().getDefenceSlash();
		return mob.getDefinition().getCombat().getDefenceStab();
	}
	
	@Override
	public CombatType getType() {
		return CombatType.MELEE;
	}
}
