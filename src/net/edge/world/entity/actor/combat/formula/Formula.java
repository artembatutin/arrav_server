package net.edge.world.entity.actor.combat.formula;

import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.attack.FightType;
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
		FightType fightType = defender.getCombat().getFightType();
		int bonus = getDefensiveBonus(attacker, defender);
		double level = defender.getSkillLevel(Skills.DEFENCE);
		if (defender.isPlayer()) {
			Player player = defender.toPlayer();
			if(Prayer.isActivated(player, Prayer.THICK_SKIN)) {
				level *= 1.05;
			} else if(Prayer.isActivated(player, Prayer.ROCK_SKIN)) {
				level *= 1.1;
			} else if(Prayer.isActivated(player, Prayer.STEEL_SKIN)) {
				level *= 1.15;
			} else if(Prayer.isActivated(player, Prayer.CHIVALRY)) {
				level *= 1.20;
			} else if(Prayer.isActivated(player, Prayer.PIETY)) {
				level *= 1.15 + (attacker.isPlayer() ? (attacker.toPlayer().getSkills()[Skills.ATTACK].getRealLevel() / 1000D) : 0.0);
			}
			level = (int) level;
			level += 8;
			level += fightType.getStyle().getDefensiveIncrease();
			if (Prayer.isActivated(player, Prayer.TURMOIL)) {
				if (attacker != null && attacker.isPlayer()) {
					// 1000 because if defence is 99 / 100 * 10 = 9.9 which is way to much.
					level *= attacker.toPlayer().getSkills()[Skills.DEFENCE].getRealLevel() / 1000;
				}
			}
		}
		return ((int) level) * (bonus + 64);
	}
	
	/**
	 * Calculates the max hit possible.
	 * @param attacker attacker.
	 * @param defender defender.
	 * @return the max hit.
	 */
	int maxHit(Actor attacker, Actor defender);
	
	/**
	 * Gets the {@link CombatType} of this {@link Formula}.
	 * @return combat type.
	 */
	CombatType getType();
	
	/**
	 * Gets an offensive strength bonus for an actor.
	 * @param attacker the attacker.
	 * @param defender the defender.
	 * @return the offensive bonus.
	 */
	int getStrength(Actor attacker, Actor defender);
	
	/**
	 * Gets an offensive bonus for an actor.
	 * @param attacker the attacker.
	 * @param defender the defender.
	 * @return the offensive bonus.
	 */
	int getOffensiveBonus(Actor attacker, Actor defender);
	
	/**
	 * Gets a defensive roll for an actor.
	 * @param attacker the attacker.
	 * @param defender the defender.
	 * @return the defensive attack roll
	 */
	int getDefensiveBonus(Actor attacker, Actor defender);
	
}
