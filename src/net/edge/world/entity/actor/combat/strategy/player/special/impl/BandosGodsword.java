package net.edge.world.entity.actor.combat.strategy.player.special.impl;

import net.edge.content.skill.SkillData;
import net.edge.content.skill.Skills;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.world.entity.actor.player.Player;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 2-9-2017.
 */
public class BandosGodsword extends PlayerMeleeStrategy {
    private static final Animation ANIMATION = new Animation(11991, Animation.AnimationPriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(2114);

    @Override
    public void start(Player attacker, Actor defender, Hit[] hits) {
        super.start(attacker, defender, hits);
        attacker.graphic(GRAPHIC);
    }

    @Override
    public void attack(Player attacker, Actor defender, Hit h) {
        super.attack(attacker, defender, h);
        if (defender.isPlayer() && h.isAccurate()) {
            Player victim = defender.toPlayer();
            int damage = h.getDamage();
            int[] skillOrder = {Skills.DEFENCE, Skills.STRENGTH, Skills.ATTACK, Skills.PRAYER, Skills.MAGIC, Skills.RANGED};
            for (int s : skillOrder) {
                //Getting the skill value to decrease.
                int removeFromSkill;
                if (h.getDamage() > victim.getSkills()[s].getLevel()) {
                    int difference = damage - victim.getSkills()[s].getLevel();
                    removeFromSkill = damage - difference;
                } else
                    removeFromSkill = damage;
                //Decreasing the skill.
                victim.getSkills()[s].decreaseLevel(removeFromSkill);
                Skills.refresh(victim, s);
                //Changing the damage left to decrease.
                damage -= removeFromSkill;
                SkillData data = SkillData.forId(s);
                String skill = data.toString();
                attacker.message("You've drained " + victim.credentials.formattedUsername + "'s " + skill + " level by " + removeFromSkill + ".");
                victim.message("Your " + skill + " level has been drained.");
            }
        }
    }

    @Override
    public int getAttackDelay(Player attacker, Actor defender, FightType fightType) {
        return 4;
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        return ANIMATION;
    }

    @Override
    public int modifyAccuracy(Player attacker, Actor defender, int roll) {
        return roll * 2;
    }

    @Override
    public int modifyDamage(Player attacker, Actor defender, int damage) {
        return (int) (damage * 1.21);
    }

}
