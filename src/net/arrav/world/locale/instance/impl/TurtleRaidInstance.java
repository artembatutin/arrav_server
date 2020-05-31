/*package net.arrav.world.locale.instance.impl;

import net.arrav.world.locale.instance.Instance;
import net.arrav.world.locale.instance.InstanceDisposePolicy;
import net.arrav.world.Area;
import net.arrav.world.entity.Entity;
import net.arrav.world.entity.actor.player.Player;

import java.util.Optional;

public class TurtleRaidInstance extends Instance {


    public static final Area AREA = Area.region(11330);

    public TurtleRaidInstance(Player player) {
        super("turtle_raid"+player.getFormatUsername());
        setDisposePolicy(InstanceDisposePolicy.DISPOSE_ON_NO_PLAYERS);
        setInstancedArea(AREA, true);
    }


    @Override
    public void onCreate(Player creator) {

    }

    @Override
    public void onJoin(Player player) {

    }

    @Override
    public void onExit(Player player) {
        if(player.isPlayer())
        player.setMinigame(Optional.empty());
    }

    @Override
    public void onDeath(Entity player) {

    }

    @Override
    public void onRegionChange(Player Player) {

    }
}
*/