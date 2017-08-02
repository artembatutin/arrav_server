package net.edge.content.combat.ranged;

import net.edge.Application;
import net.edge.content.combat.CombatConstants;
import net.edge.content.combat.CombatUtil;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

/**
 * Created by Dave/Ophion
 * Date: 02/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class RangedFormulas {
    /**
     * Calculates the maximum hit that can be dealt using ranged for
     * {@code character}.
     * @param character the character to calculate the max hit for.
     * @param victim    the victim being attacked.
     * @return the maximum hit this character can deal.
     */
    public static int calculateMaxRangedHit(Actor character, Actor victim) {
        int maxHit = 0;
        if(character.isNpc()) {
            Mob mob = (Mob) character;
            maxHit = mob.getDefinition().getMaxHit();
            if(victim.isPlayer()) {
                Player p = victim.toPlayer();
                if(p.isIronMan())//Iron man monsters tougher.
                    maxHit *= 1.2;
            }
            return maxHit;
        }

        Player player = (Player) character;

        double specialMultiplier = 1;
        double prayerMultiplier = 1;
        double additionalBonusMultiplier = 1;
        double voidMultiplier = 1;
        int rangedStrength = player.getEquipment().getBonuses()[CombatConstants.BONUS_RANGED_STRENGTH];
        int rangeLevel = player.getSkills()[Skills.RANGED].getLevel();

        int combatStyleBonus = 0;

        if(Prayer.isActivated(player, Prayer.SHARP_EYE)) {
            prayerMultiplier = 1.05;
        } else if(Prayer.isActivated(player, Prayer.HAWK_EYE)) {
            prayerMultiplier = 1.1;
        } else if(Prayer.isActivated(player, Prayer.EAGLE_EYE)) {
            prayerMultiplier = 1.15;
        }

        switch(player.getFightType().getStyle()) {
            case ACCURATE:
                combatStyleBonus = 3;
                break;
            default:
                break;
        }

        if (CombatUtil.isFullVoid(character) && player.getEquipment().contains(11665)) {
            voidMultiplier = 1.1;
        }

        //int effectiveRangeDamage = (int) ((rangeLevel * prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
        //double baseDamage = 1.3 + (effectiveRangeDamage / 10) + (rangedStrength / 80) + ((effectiveRangeDamage * rangedStrength) / 640);
        double baseDamage = 0.5 + (Math.round((rangeLevel * prayerMultiplier)) + combatStyleBonus + 8) * additionalBonusMultiplier * voidMultiplier * (rangedStrength+64) /640;
        if(player.isSpecialActivated()) {
            specialMultiplier = player.getCombatSpecial().getStrength();
        }

        maxHit = (int) (baseDamage * specialMultiplier);

        if(Application.DEBUG)
            player.message("[DEBUG]: Maximum hit this turn " + "is [" + maxHit + "].");
        return maxHit * 10;
    }
}
