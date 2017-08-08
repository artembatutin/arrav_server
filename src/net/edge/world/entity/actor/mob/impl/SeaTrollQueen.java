package net.edge.world.entity.actor.mob.impl;

import net.edge.content.combat.strategy.mob.SeaTrollQueenStrategy;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.strategy.impl.KalphiteQueenStrategy;
import net.edge.world.locale.Position;

import java.util.Optional;

/**
 * Created by Dave/Ophion
 * Date: 08/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class SeaTrollQueen extends Mob {

    public SeaTrollQueen(Position position) {
        super(3847, position);
        this.getMovementQueue().setLockMovement(true);
    }

    @Override
    public Mob create() {
        return new SeaTrollQueen(getPosition());
    }

}
