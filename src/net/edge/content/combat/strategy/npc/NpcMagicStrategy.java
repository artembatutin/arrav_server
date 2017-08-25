package net.edge.content.combat.strategy.npc;

import net.edge.content.combat.CombatEffect;
import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.effect.CombatPoisonEffect;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.basic.MagicStrategy;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class NpcMagicStrategy extends MagicStrategy<Mob> {

    private final CombatProjectileDefinition projectileDefinition;

    /** The spell splash graphic. */
    private static final Graphic SPLASH = new Graphic(85);

    public NpcMagicStrategy(CombatProjectileDefinition projectileDefinition) {
        this.projectileDefinition = projectileDefinition;
    }

    @Override
    public void attack(Mob attacker, Actor defender, Hit hit) {
        projectileDefinition.getAnimation().ifPresent(attacker::animation);
        projectileDefinition.getStart().ifPresent(attacker::graphic);
        projectileDefinition.sendProjectile(attacker, defender, true);
    }

    @Override
    public void hit(Mob attacker, Actor defender, Hit hit) {
        Predicate<CombatEffect> filter = effect -> effect.canEffect(attacker, defender, hit);
        Consumer<CombatEffect> execute = effect -> effect.execute(attacker, defender, hit, null);
        projectileDefinition.getEffect().filter(Objects::nonNull).filter(filter).ifPresent(execute);

        CombatPoisonEffect.getPoisonType(attacker.getId()).ifPresent(p -> {
            if(hit.isAccurate() && attacker.getDefinition().poisonous()) {
                defender.poison(p);
            }
        });

        if (!hit.isAccurate()) {
            defender.graphic(SPLASH);
        } else {
            projectileDefinition.getEnd().ifPresent(defender::graphic);
        }
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return new CombatHit[] { nextMagicHit(attacker, defender, attacker.getDefinition().getMaxHit()) };
    }

    @Override
    public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
        int delay = attacker.getDefinition().getAttackDelay();

        if (attacker.getPosition().getDistance(defender.getPosition()) > 4) {
            return 1 + delay;
        }

        return delay;
    }

    @Override
    public int getAttackDistance(Mob attacker, FightType fightType) {
        return 10;
    }

    @Override
    public Animation getAttackAnimation(Mob attacker, Actor defender) {
        return new Animation(attacker.getDefinition().getAttackAnimation(), Animation.AnimationPriority.HIGH);
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }

}
