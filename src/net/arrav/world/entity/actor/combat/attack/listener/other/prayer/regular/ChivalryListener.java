package net.arrav.world.entity.actor.combat.attack.listener.other.prayer.regular;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.arrav.world.entity.actor.player.Player;

public class ChivalryListener extends SimplifiedListener<Player> {

    @Override
    public int modifyAttackLevel(Player attacker, Actor defender, int level) {
        return level * 23 / 20;
    }

    @Override
    public int modifyStrengthLevel(Player attacker, Actor defender, int level) {
        return level * 59 / 50;
    }

    @Override
    public int modifyDefenceLevel(Actor attacker, Player defender, int level) {
        return level * 6 / 5;
    }

}
