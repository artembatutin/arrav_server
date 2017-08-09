package net.edge.world.entity.actor.mob.impl;

import net.edge.net.packet.out.SendGraphic;
import net.edge.task.LinkedTaskSequence;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Hit;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.strategy.impl.GiantMoleStrategy;
import net.edge.world.entity.actor.mob.strategy.impl.SkeletalHorrorStrategy;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

import java.util.Optional;
import java.util.Random;

/**
 * Created by Dave/Ophion
 * Date: 09/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class GiantMole extends Mob {



    public GiantMole(Position position) {
        super(3340, position);
        setStrategy(Optional.of(new GiantMoleStrategy(this)));

    }

    @Override
    public Mob create() {
        return new GiantMole(getPosition());
    }
}
