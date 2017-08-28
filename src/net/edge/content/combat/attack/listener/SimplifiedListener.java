package net.edge.content.combat.attack.listener;

import net.edge.content.combat.CombatType;
import net.edge.content.combat.hit.Hit;
import net.edge.world.entity.actor.Actor;

public class SimplifiedListener<T extends Actor> implements CombatListener<T> {

    @Override
    public boolean canAttack(T attacker, Actor defender) {
        return true;
    }

    @Override
    public void start(T attacker, Actor defender, Hit[] hits) { }

    @Override
    public void attack(T attacker, Actor defender, Hit hit) { }

    @Override
    public void hit(T attacker, Actor defender, Hit hit) { }

    @Override
    public void hitsplat(T attacker, Actor defender, Hit hit) { }

    @Override
    public void block(Actor attacker, T defender, Hit hit, CombatType combatType) { }

    @Override
    public void onDeath(Actor attacker, T defender, Hit hit) { }

    @Override
    public void finish(T attacker, Actor defender) { }

}
