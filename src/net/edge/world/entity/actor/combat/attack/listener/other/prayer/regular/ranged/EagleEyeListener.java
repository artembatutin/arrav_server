package net.edge.world.entity.actor.combat.attack.listener.other.prayer.regular.ranged;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.edge.world.entity.actor.player.Player;

public class EagleEyeListener extends SimplifiedListener<Player> {

    @Override
    public int modifyRangedLevel(Player attacker, Actor defender, int damage) {
        return damage * 23 / 20;
    }

}
