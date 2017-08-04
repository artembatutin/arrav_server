package net.edge.content.combat.magic;

import net.edge.content.combat.CombatConstants;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.weapon.FightStyle;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

/**
 * Created by Dave/Ophion
 * Date: 03/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class MagicCalculations {

    public static int calculateMageDefence(Actor defender, Actor attacker) {
        int defenceLevel = defender.isPlayer() ? defender.toPlayer().getSkills()[Skills.DEFENCE].getLevel() : defender.toNpc().getDefinition().getDefenceLevel();
        int magicLevel = defender.isPlayer() ? defender.toPlayer().getSkills()[Skills.MAGIC].getLevel() : defender.toNpc().getDefinition().getDefenceLevel();
        int correspondingBonus = defender.isPlayer() ? defender.toPlayer().getEquipment().getBonuses()[CombatConstants.DEFENCE_MAGIC] : 120;
        double prayerMultiplier = 1.0;
        int combatStyleBonus = 0;
    if(attacker.isPlayer()) {
        Player player = (Player) defender;
        if (Prayer.isActivated(player, Prayer.MYSTIC_WILL)) {
            prayerMultiplier = 1.05;
        } else if (Prayer.isActivated(player, Prayer.MYSTIC_LORE)) {
            prayerMultiplier = 1.1;
        } else if (Prayer.isActivated(player, Prayer.MYSTIC_MIGHT)) {
            prayerMultiplier = 1.15;
        }
    }
    int baseDefence = (int)(Math.round(Math.round(magicLevel * prayerMultiplier) * 0.70) + Math.round(Math.round(defenceLevel) + combatStyleBonus + 8) * 0.30) * (correspondingBonus + 64);
    return baseDefence;
    }
    /**
     * Formula calculating the players Ranged accuracy
     * @param character
     * @param victim
     * @return
     */
    public static int calculateMageAttack(Actor character, Actor victim) {

        int magicLevel = character.isPlayer() ? character.toPlayer().getSkills()[Skills.MAGIC].getLevel() : character.toNpc().getDefinition().getMagicLevel();
        int correspondingBonus = character.isPlayer() ? character.toPlayer().getEquipment().getBonuses()[CombatConstants.ATTACK_MAGIC] : 120;
        int combatStyleBonus = 0;
        double prayerMultiplier = 1.0;
        double additionalBonusMultiplier = 1.0;

        if (character.isPlayer()) {
            Player player = (Player) character;
            if (Prayer.isActivated(player, Prayer.MYSTIC_WILL)) {
                prayerMultiplier = 1.05;
            } else if (Prayer.isActivated(player, Prayer.MYSTIC_LORE)) {
                prayerMultiplier = 1.1;
            } else if (Prayer.isActivated(player, Prayer.MYSTIC_MIGHT)) {
                prayerMultiplier = 1.15;
            }

            if (CombatUtil.isFullVoid(player) && player.getEquipment().contains(11663)) {
                additionalBonusMultiplier += 0.45;
            }
            player.message("Corresponding attack:" + correspondingBonus);
        }
        int baseAccuracy = (int) Math.round(Math.round(magicLevel * prayerMultiplier) + combatStyleBonus + 8 * additionalBonusMultiplier) * (correspondingBonus+64);
        //int baseAccuracy2 = Math.floor(((Math.round(attackLevel * prayerMultiplier) + combatStyleBonus + 8) * additionalBonusMultiplier) * (correspondingBonus+64));
        return baseAccuracy;
    }
}
