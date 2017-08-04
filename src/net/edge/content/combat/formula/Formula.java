package net.edge.content.combat.formula;

import net.edge.content.combat.CombatConstants;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatWeaken;
import net.edge.content.combat.weapon.FightStyle;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

/**
 * An interface implementation of a combat formula.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public interface Formula {
	
	/**
	 * Calculates the attacking accuracy.
	 * @param attacker attacker.
	 * @param defender defender.
	 * @return the attacking accuracy.
	 */
	int attack(Actor attacker, Actor defender);
	
	/**
	 * Calculates the defending accuracy.
	 * @param attacker attacker.
	 * @param defender defender.
	 * @return the defending accuracy.
	 */
	default int defence(Actor attacker, Actor defender) {
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
			//rounding
			defenceLevel = Math.round(defenceLevel);
			//stance bonuses
			if(player.getFightType().getStyle() == FightStyle.DEFENSIVE) {
				defenceLevel += 3;
			} else if(player.getFightType().getStyle() == FightStyle.CONTROLLED) {
				defenceLevel += 1;
			}
		} else if(defender.isMob()) {
			Mob mob = defender.toMob();
			if(mob.getWeakenedBy() == CombatWeaken.DEFENCE_LOW || mob.getWeakenedBy() == CombatWeaken.DEFENCE_HIGH)
				defenceLevel -= (int) ((mob.getWeakenedBy().getRate()) * (defenceLevel));
		}
		return Math.round(defenceLevel + 8) * (correspondingBonus+64);
	}
	
	/**
	 * Calculates the max hit possible.
	 * @param attacker attacker.
	 * @param defender defender.
	 * @return the max hit.
	 */
	int maxHit(Actor attacker, Actor defender);
	
	/**
	 * Gets the mob defensive value.
	 * @param type the fight style type.
	 * @param mob mob
	 * @return defensive bonus.
	 */
	int getMobDefence(int type, Mob mob);
	
	/**
	 * Gets the {@link CombatType} of this {@link Formula}.
	 * @return combat type.
	 */
	CombatType getType();
	
}
