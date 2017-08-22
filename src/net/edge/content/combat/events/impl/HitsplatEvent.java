package net.edge.content.combat.events.impl;

import net.edge.content.combat.CombatType;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.listener.CombatListener;
import net.edge.content.combat.events.CombatEvent;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;

import java.util.List;

public class HitsplatEvent<T extends Actor> extends CombatEvent<T> {
    private final T attacker;
    private final Actor defender;
    private final CombatStrategy<? super T> strategy;
    private final CombatHit hit;

    public HitsplatEvent(T attacker, Actor defender, List<CombatListener<? super T>> listeners, CombatStrategy<? super T> strategy, CombatHit hit, int hitsplatDelay) {
        super(listeners, hitsplatDelay);
        this.attacker = attacker;
        this.defender = defender;
        this.strategy = strategy;
        this.hit = hit;
    }

    @Override
    protected void execute() {
        if (!CombatUtil.canAttack(attacker, defender)) {
            attacker.getCombat().reset();
            setActive(false);
            return;
        }

        strategy.hitsplat(attacker, defender, hit);
        listeners.forEach(listener -> listener.hitsplat(attacker, defender, hit));

        defender.getCombat().block(attacker, hit, strategy.getCombatType());
        defender.getCombat().getDamageCache().add(attacker, hit.getDamage());

        if (strategy.getCombatType() != CombatType.MAGIC || hit.isAccurate()) {
            defender.damage(hit);

            if (defender.getCurrentHealth() <= 0) {
                defender.getCombat().onDeath(attacker, hit);
                attacker.getCombat().reset();
                defender.getCombat().reset();
            }
        }
        attacker.getCombat().decrementQueuedHits();
        setActive(false);
    }

    @Override
    public boolean canExecute() {
        return defender.canAppendHit() && super.canExecute();
    }

}