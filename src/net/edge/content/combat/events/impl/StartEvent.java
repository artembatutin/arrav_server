package net.edge.content.combat.events.impl;

import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.listener.CombatListener;
import net.edge.content.combat.events.CombatEvent;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;

import java.util.List;

public class StartEvent<T extends Actor> extends CombatEvent<T> {
    private final T attacker;
    private final Actor defender;
    private final CombatStrategy<? super T> strategy;

    public StartEvent(T attacker, Actor defender, List<CombatListener<? super T>> listeners, CombatStrategy<? super T> strategy) {
        super(listeners, 0);
        this.attacker = attacker;
        this.defender = defender;
        this.strategy = strategy;
    }

    @Override
    protected void execute() {
        if (!CombatUtil.canAttack(attacker, defender)) {
            attacker.getCombat().reset();
            setActive(false);
            return;
        }

        strategy.start(attacker, defender);
        listeners.forEach(listener -> listener.start(attacker, defender));
        setActive(false);
    }

}