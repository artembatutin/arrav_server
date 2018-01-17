package net.edge.world.entity.actor.combat.attack.listener.other.prayer.regular.attack;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.edge.world.entity.actor.player.Player;

public class ImprovedReflexesListener extends SimplifiedListener<Player> {

    @Override
    public int modifyAttackLevel(Player attacker, Actor defender, int damage) {
        return damage * 11 / 10;
    }

}
