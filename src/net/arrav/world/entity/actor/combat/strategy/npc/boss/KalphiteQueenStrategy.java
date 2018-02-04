package net.arrav.world.entity.actor.combat.strategy.npc.boss;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.strategy.npc.MultiStrategy;
import net.arrav.world.entity.actor.mob.Mob;

/**
 * Created by Dave/Ophion
 * Date: 05/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class KalphiteQueenStrategy extends MultiStrategy {

    @Override
    public boolean canAttack(Mob attacker, Actor defender) {
        return false;
    }
}
