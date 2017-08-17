package net.edge.content.newcombat.strategy.player;

import net.edge.content.combat.weapon.FightType;
import net.edge.content.newcombat.CombatType;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.hit.Hit;
import net.edge.content.newcombat.strategy.basic.MeleeStrategy;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

public class PlayerMeleeStrategy extends MeleeStrategy<Player> {

    @Override
    public void attack(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        attacker.animation(getAttackAnimation(attacker, defender));
        addCombatExperience(attacker, hit);
    }

    @Override
    public void block(Actor attacker, Player defender, Hit hit, Hit[] hits) {
        defender.animation(getBlockAnimation(defender, attacker));
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        return new CombatHit[]{nextMeleeHit(attacker, defender, 1, 1)};
    }

    @Override
    public int getAttackDelay(FightType fightType) {
        return 4;
    }

    @Override
    public int getAttackDistance(FightType fightType) {
        return 1;
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        FightType fightType = attacker.getNewCombat().getFightType();
        int animation = attacker.getWeaponAnimation().getAttacking()[fightType.getStyle().ordinal()];
        return new Animation(animation, Animation.AnimationPriority.HIGH);
    }

    @Override
    public Animation getBlockAnimation(Player player, Actor defender) {
        int animation = 404;
        if (player.getShieldAnimation() != null) {
            animation = player.getShieldAnimation().getBlock();
        } else if (player.getWeaponAnimation() != null) {
            animation = player.getWeaponAnimation().getBlocking();
        }
        return new Animation(animation, Animation.AnimationPriority.LOW);
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MELEE;
    }
}
