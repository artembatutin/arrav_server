package net.edge.content.newcombat.strategy.npc;

import net.edge.content.combat.weapon.FightType;
import net.edge.content.newcombat.CombatType;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.hit.Hit;
import net.edge.content.newcombat.strategy.basic.MeleeStrategy;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

public class NpcMeleeStrategy extends MeleeStrategy<Mob> {

    @Override
    public boolean canAttack(Mob attacker, Actor defender) {
        return true;
    }

    @Override
    public void block(Actor attacker, Mob defender, Hit hit, Hit[] hits) {
        defender.animation(getBlockAnimation(defender, attacker));
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
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return new CombatHit[] { nextMeleeHit(attacker, defender, 1, 0) };
    }

    @Override
    protected Animation getAttackAnimation(Mob attacker, Actor defender) {
        return new Animation(attacker.getDefinition().getAttackAnimation(), Animation.AnimationPriority.HIGH);
    }

    @Override
    protected Animation getBlockAnimation(Mob defender, Actor attacker) {
        return new Animation(defender.getDefinition().getDefenceAnimation(), Animation.AnimationPriority.HIGH);
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MELEE;
    }
}
