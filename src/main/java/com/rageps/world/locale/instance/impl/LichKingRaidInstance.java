/*package com.rageps.world.locale.instance.impl;

import com.rageps.world.locale.instance.Instance;
import com.rageps.world.locale.instance.InstanceDisposePolicy;
import Area;
import Entity;
import Player;

import java.util.Optional;

public class LichKingRaidInstance extends Instance {


    public static final Area AREA = Area.region(9030);

    public LichKingRaidInstance(Player player) {
        super("lich_raid"+player.getFormatUsername());
        setDisposePolicy(InstanceDisposePolicy.DISPOSE_ON_NO_PLAYERS);
        setMulti(true);
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
        player.setMinigame(Optional.empty());
    }

    @Override
    public void onDeath(Entity player) {

    }

    @Override
    public void onRegionChange(Player entity) {

    }
}
*/