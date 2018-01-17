package net.edge.world.entity.actor.combat.attack.listener.other.prayer.regular.defence;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.edge.world.entity.actor.player.Player;

public class ThickSkinListener extends SimplifiedListener<Player> {

    @Override
    public int modifyDefenceLevel(Actor attacker, Player defender, int damage) {
        return damage * 21 / 20;
    }

}
