package net.arrav.world.entity.actor.combat.attack.listener.other.prayer.regular.ranged;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.arrav.world.entity.actor.player.Player;

public class HawkEyeListener extends SimplifiedListener<Player> {

    @Override
    public int modifyRangedLevel(Player attacker, Actor defender, int level) {
        return level * 11 / 10;
    }

}
