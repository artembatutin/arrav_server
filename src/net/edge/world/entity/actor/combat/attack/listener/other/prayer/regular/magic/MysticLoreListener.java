package net.edge.world.entity.actor.combat.attack.listener.other.prayer.regular.magic;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.edge.world.entity.actor.player.Player;

public class MysticLoreListener extends SimplifiedListener<Player> {

    @Override
    public int modifyMagicLevel(Player attacker, Actor defender, int level) {
        return level * 11 / 10;
    }

}
