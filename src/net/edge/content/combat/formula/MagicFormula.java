package net.edge.content.combat.formula;

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
 * Date: 03/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class MagicFormula implements Formula {
	
	@Override
	public int attack(Actor attacker, Actor defender) {
		float magicLevel = attacker.isPlayer() ? attacker.toPlayer().getSkills()[Skills.MAGIC].getLevel() : attacker.toMob().getDefinition().getMagicLevel();
		int correspondingBonus = attacker.isPlayer() ? attacker.toPlayer().getEquipment().getBonuses()[CombatConstants.ATTACK_MAGIC] : attacker.toMob().getDefinition().getCombat().getAttackMagic();
		
		if(attacker.isPlayer()) {
			Player player = (Player) attacker;
			//prayer adjustments
			if(Prayer.isActivated(player, Prayer.MYSTIC_WILL)) {
				magicLevel *= 1.05;
			} else if(Prayer.isActivated(player, Prayer.MYSTIC_LORE)) {
				magicLevel *= 1.1;
			} else if(Prayer.isActivated(player, Prayer.MYSTIC_MIGHT)) {
				magicLevel *= 1.15;
			}
			//rounding and add up 8
			magicLevel = Math.round(magicLevel) + 8;
			//void magic
			if(CombatUtil.isFullVoid(player) && player.getEquipment().contains(11663)) {
				magicLevel *= 1.45;
			}
		} else if(attacker.isMob()) {
			Mob mob = attacker.toMob();
			if(mob.getWeakenedBy() == CombatWeaken.ATTACK_LOW || mob.getWeakenedBy() == CombatWeaken.ATTACK_HIGH)
				magicLevel -= (int) ((mob.getWeakenedBy().getRate()) * (magicLevel));
		}
		return Math.round(magicLevel) * (correspondingBonus + 64);
	}
	
	@Override
	public int defence(Actor attacker, Actor defender) {
		//Magic defence accuracy is different from all others for players.
		int type = attacker.isPlayer() ? attacker.toPlayer().getFightType().getCorrespondingBonus() : getType() == CombatType.MAGIC ? CombatConstants.DEFENCE_MAGIC : getType() == CombatType.RANGED ? CombatConstants.DEFENCE_RANGED : CombatConstants.DEFENCE_STAB;
		float defenceLevel = defender.isPlayer() ? defender.toPlayer().getSkills()[Skills.DEFENCE].getLevel() : defender.toMob().getDefinition().getCombat().getDefenceLevel();
		int correspondingBonus = defender.isPlayer() ? defender.toPlayer().getEquipment().getBonuses()[type] : getMobDefence(type, defender.toMob());
		if(defender.isPlayer()) {
			Player player = (Player) defender;
			if(Prayer.isActivated(player, Prayer.THICK_SKIN)) {
				defenceLevel *= 1.05;
			} else if(Prayer.isActivated(player, Prayer.ROCK_SKIN)) {
				defenceLevel *= 1.1;
			} else if(Prayer.isActivated(player, Prayer.STEEL_SKIN)) {
				defenceLevel *= 1.15;
			} else if(Prayer.isActivated(player, Prayer.CHIVALRY)) {
				defenceLevel *= 1.20;
			} else if(Prayer.isActivated(player, Prayer.PIETY)) {
				defenceLevel *= 1.25;
			}
			//rounding and add up 8
			defenceLevel = Math.round(defenceLevel) + 8;
			//stance bonuses
			if(player.getFightType().getStyle() == FightStyle.DEFENSIVE) {
				defenceLevel += 3;
			} else if(player.getFightType().getStyle() == FightStyle.CONTROLLED) {
				defenceLevel += 1;
			}
			//unique to magic defence
			defenceLevel = Math.round(defenceLevel * 0.3);
			float magicLevel = attacker.isPlayer() ? attacker.toPlayer().getSkills()[Skills.MAGIC].getLevel() : attacker.toMob().getDefinition().getMagicLevel();
			if(Prayer.isActivated(player, Prayer.MYSTIC_WILL)) {
				magicLevel *= 1.05;
			} else if(Prayer.isActivated(player, Prayer.MYSTIC_LORE)) {
				magicLevel *= 1.1;
			} else if(Prayer.isActivated(player, Prayer.MYSTIC_MIGHT)) {
				magicLevel *= 1.15;
			}
			//rounding and add up 8
			magicLevel = Math.round(magicLevel * 0.7);
			defenceLevel += magicLevel;
		} else if(defender.isMob()) {
			Mob mob = defender.toMob();
			if(mob.getWeakenedBy() == CombatWeaken.DEFENCE_LOW || mob.getWeakenedBy() == CombatWeaken.DEFENCE_HIGH)
				defenceLevel -= (int) ((mob.getWeakenedBy().getRate()) * (defenceLevel));
		}
		return Math.round(defenceLevel) * (correspondingBonus+64);
	}
	
	@Override
	public int maxHit(Actor attacker, Actor defender) {
		int max = attacker.getCurrentlyCasting().maximumHit();
		if(defender.isPlayer() && attacker.isMob()) {
			Player p = defender.toPlayer();
			if(p.isIronMan())//Iron man monsters tougher.
				max *= 1.2;
		}
		return max;
	}
	
	@Override
	public int getMobDefence(int type, Mob mob) {
		return mob.getDefinition().getCombat().getDefenceMagic();
	}
	
	@Override
	public CombatType getType() {
		return CombatType.MAGIC;
	}
}
