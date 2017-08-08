package net.edge.world.entity.actor.mob.impl;

import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.locale.Position;

/**
 * Created by Dave/Ophion
 * Date: 08/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class Phoenix extends Mob {

    public Phoenix(Position position) {
        super(8549, position);
        this.getMovementQueue().setLockMovement(true);
    }

    @Override
    public Mob create() {
        return new Phoenix(getPosition());
    }
}
