package com.rageps.combat.strategy.player.custom;

import com.rageps.combat.strategy.PlayerWeaponStrategyMeta;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.combat.hit.CombatHit;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.combat.strategy.player.PlayerMeleeStrategy;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

/**
 *
 * A basic proof of concept for area of effect weapons
 *
 * @author Tamatea Schofield <tamateea@gmail.com>
 */
@PlayerWeaponStrategyMeta(ids = 4151)
public class AOEWeaponStrategy extends PlayerMeleeStrategy {

    private static final AOEWeaponStrategy INSTANCE = new AOEWeaponStrategy();

    public String name() {
        return "Test weapon";
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MELEE;
    }

    /** Atack delay. **/

    @Override
    public int getAttackDelay(Player attacker, Actor defender, FightType fightType) {
        return 2;
    }

    /** Instane's the class to be called upon,and applied to an item. **/
    public static AOEWeaponStrategy get() {
        return INSTANCE;
    }

    @Override
    public void attack(Player attacker, Actor defender, Hit hit) {
        super.attack(attacker, defender, hit);

        for(Mob mob : attacker.getLocalMobs()) {
            if(mob.getPosition().copy().withinDistance(attacker.getPosition().copy(), 5) && mob.getDefinition().isAttackable() && !mob.isDead() && !mob.equals(defender)) {
                CombatProjectile.getDefinition("Tormented_Demon Magic").projectile.send(attacker, mob, false);
                mob.damage(getHits(attacker, mob));
            }
        }
    }

    @Override
    public void start(Player attacker, Actor defender, Hit[] hits) {
        super.start(attacker, defender, hits);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
       CombatHit hits = nextMeleeHit(attacker, defender);
        hits.setHitDelay(3);
        return new CombatHit[] {
                hits
        };
    }

    @Override
    public int modifyAccuracy(Player attacker, Actor defender, int roll) {
        return roll * 5 / 3;
    }

}