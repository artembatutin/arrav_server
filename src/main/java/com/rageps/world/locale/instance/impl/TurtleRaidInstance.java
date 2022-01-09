/*package com.rageps.world.locale.instance.impl;

import com.rageps.world.locale.instance.Instance;
import com.rageps.world.locale.instance.InstanceDisposePolicy;
import Area;
import Entity;
import Player;

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