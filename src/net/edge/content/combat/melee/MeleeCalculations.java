package net.edge.content.combat.melee;

import net.edge.Application;
import net.edge.content.combat.CombatConstants;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.magic.CombatWeaken;
import net.edge.content.combat.weapon.FightStyle;
import net.edge.content.combat.weapon.FightType;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Equipment;

import java.util.Objects;

/**
 * Created by Dave/Ophion
 * Date: 02/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class MeleeCalculations {

    /**
     * Formula calculating the players attack
     * @param character
     * @param victim
     * @return
     */
    public static int calculateMeleeAttack(Actor character, Actor victim) {

        int attackLevel = character.isPlayer() ? character.toPlayer().getSkills()[Skills.ATTACK].getLevel() : character.toNpc().getDefinition().getAttackLevel();
        int correspondingBonus = character.isPlayer() ? character.toPlayer().getEquipment().getBonuses()[character.toPlayer().getFightType().getBonus()] : 120;
        int combatStyleBonus = 0;
        double specialMultiplier = 1.0;
        double prayerMultiplier = 1.0;
        double prayerBonus = 0;
        double additionalBonusMultiplier = 1.0;

        if(character.isPlayer()) {
            Player player = (Player) character;
        if (Prayer.isActivated(player, Prayer.CLARITY_OF_THOUGHT)) {
            prayerMultiplier = 1.05;
        } else if (Prayer.isActivated(player, Prayer.IMPROVED_REFLEXES)) {
            prayerMultiplier = 1.1;
        } else if (Prayer.isActivated(player, Prayer.INCREDIBLE_REFLEXES)) {
            prayerMultiplier = 1.15;
        } else if (Prayer.isActivated(player, Prayer.CHIVALRY)) {
            prayerMultiplier = 1.15;
        } else if (Prayer.isActivated(player, Prayer.PIETY)) {
            prayerMultiplier = 1.20;
        } else if (Prayer.isActivated(player, Prayer.TURMOIL)) {
            if (!victim.isNpc()) {
                Player opponent = (Player) victim;
                prayerMultiplier = opponent.getSkills()[Skills.ATTACK].getLevel() * 0.15;
            }
            prayerMultiplier = 1.15 + (prayerBonus / 100);
        }

        if (player.getFightType().getStyle() == FightStyle.ACCURATE) {
            combatStyleBonus = 3;
        } else if (player.getFightType().getStyle() == FightStyle.CONTROLLED) {
            combatStyleBonus = 1;
        }

        if (player.isSpecialActivated()) {
            specialMultiplier = player.getCombatSpecial().getAccuracy();
        }
        attackLevel *= specialMultiplier;

        if (CombatUtil.isFullVoid(player) && player.getEquipment().contains(11665)) {
            additionalBonusMultiplier += 0.1;
        }
        player.message("Corresponding attack:" +correspondingBonus);
    }

        int baseAccuracy = (int) Math.round(Math.round(attackLevel * prayerMultiplier) + combatStyleBonus + 8 * additionalBonusMultiplier) * (correspondingBonus+64);
        //int baseAccuracy2 = Math.floor(((Math.round(attackLevel * prayerMultiplier) + combatStyleBonus + 8) * additionalBonusMultiplier) * (correspondingBonus+64));
        return baseAccuracy;
    }


    /**
     * Calculates the maximum hit that can be dealt using melee for
     * {@code character}.
     * @param character the character to calculate the max hit for.
     * @param victim    the victim being attacked.
     * @return the maximum hit this character can deal.
     */
    public static int calculateMaxMeleeHit(Actor character, Actor victim) {
        int maxHit;
        if(character.isNpc()) {
            Mob mob = character.toNpc();
            maxHit = mob.getDefinition().getMaxHit();
            if(victim.isPlayer()) {
                Player p = victim.toPlayer();
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

        Player player = (Player) character;
        int level = player.getSkills()[Skills.STRENGTH].getLevel();
        int equipmentBonus = player.getEquipment().getBonuses()[CombatConstants.BONUS_STRENGTH];
        double prayer = 1.0;
        double specialMultiplier = 1;
        // TODO: void melee = 1.2, slayer helm = 1.15, salve amulet = 1.15,
        // salve amulet(e) = 1.2
        if(Prayer.isActivated(player, Prayer.BURST_OF_STRENGTH)) {
            prayer = 1.05;
        } else if(Prayer.isActivated(player, Prayer.SUPERHUMAN_STRENGTH)) {
            prayer = 1.1;
        } else if(Prayer.isActivated(player, Prayer.ULTIMATE_STRENGTH)) {
            prayer = 1.15;
        } else if(Prayer.isActivated(player, Prayer.CHIVALRY)) {
            prayer = 1.18;
        } else if(Prayer.isActivated(player, Prayer.PIETY)) {
            prayer = 1.23;
        } else if(Prayer.isActivated(player, Prayer.TURMOIL)) {
            if(victim.isPlayer()) {
                //1000 because if strength is 99 / 100 * 10 = 9.9 which is wayy to much.
                prayer = (victim.toPlayer().getSkills()[Skills.STRENGTH].getRealLevel() / 1000) * 10;
            }
            prayer = prayer + 0.23;
        }
        //double cumulativeStr = Math.floor(level * prayer);
        int combatStyleBonus = 0;
        if(player.getFightType().getStyle() == FightStyle.AGGRESSIVE) {
            combatStyleBonus = 3;
        } else if(player.getFightType().getStyle() == FightStyle.CONTROLLED) {
            combatStyleBonus = 1;
        }

        double additionalBonusMultiplier = 1.0;
        if(CombatUtil.isFullDharoks(player)) {
            additionalBonusMultiplier += 1.0 + (((player.getMaximumHealth() / 10) - (player.getCurrentHealth() / 10)) * 0.01);
        } else if(CombatUtil.isFullVoid(player) && player.getEquipment().contains(11665)) {
            additionalBonusMultiplier += 0.1;
        } else {
		    /*int itemId = player.getEquipment().get(Equipment.HEAD_SLOT).getId();
		    if (itemId >= 8901 && itemId <= 8921) {
				if (victim.isNpc() && ((Mob) victim).getId() == player.getSlayer...) {
					return 1.15;
				}
			}*/
        }

        if(victim.isNpc()) {
            Mob mob = (Mob) victim;
            /**
             * This should be in accuracy
             */
//			if(mob.getWeakenedBy() == CombatWeaken.DEFENCE_LOW) {
//				additionalBonuses += 0.10;
//			} else if(mob.getWeakenedBy() == CombatWeaken.DEFENCE_HIGH) {
//				additionalBonuses += 0.20;
//			}

			/* SLAYER */
            if(player.getSlayer().isPresent() && mob.getDefinition().getSlayerKey() != null && Objects.equals(mob.getDefinition().getSlayerKey(), player.getSlayer().get().getKey())) {
                Item head = player.getEquipment().get(Equipment.HEAD_SLOT);
                if(head != null) {
                    if(head.getId() == 13263)
                        additionalBonusMultiplier += 0.12;
                    if(head.getId() == 15492)
                        additionalBonusMultiplier += 0.25;//full slayer
                    if(head.getId() == 15488 && player.getCombat().getCombatType() == CombatType.MAGIC)
                        additionalBonusMultiplier += 0.125;//hexcrest
                    if(head.getId() == 15490 && player.getCombat().getCombatType() == CombatType.RANGED)
                        additionalBonusMultiplier += 0.125;//focus sight
                    if(head.getDefinition().getName().contains("Black mask") && player.getCombat().getCombatType() == CombatType.MELEE)
                        additionalBonusMultiplier += 0.125;//black mask
                }
            }
        }
        double baseDamage = Math.floor(0.5 + (Math.round((level * prayer)) + combatStyleBonus + 8) * additionalBonusMultiplier) * (equipmentBonus+64) /640;
        //double baseDamage = ((16 + cumulativeStr + (bonus / 8) + ((cumulativeStr * bonus) * 0.016865)) / 10);
        if(player.isSpecialActivated()) {
            specialMultiplier = player.getCombatSpecial().getStrength();
        }

        maxHit = (int) (baseDamage * specialMultiplier);

        if(Application.DEBUG)
            player.message("[DEBUG]: Maximum hit this turn " + "is [" + maxHit + "].");
        return maxHit * 10;

    }
}
