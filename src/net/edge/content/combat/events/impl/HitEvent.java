package net.edge.content.combat.events.impl;

import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.listener.CombatListener;
import net.edge.content.combat.events.CombatEvent;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;

import java.util.List;

public class HitEvent<T extends Actor> extends CombatEvent<T> {
    private final T attacker;
    private final Actor defender;
    private final CombatStrategy<? super T> strategy;
    private final CombatHit hit;

    public HitEvent(T attacker, Actor defender, List<CombatListener<? super T>> listeners, CombatStrategy<? super T> strategy, CombatHit hit) {
        super(listeners, hit.getHitDelay());
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

        strategy.hit(attacker, defender, hit);
        listeners.forEach(listener -> listener.hit(attacker, defender, hit));
        setActive(false);
    }

}