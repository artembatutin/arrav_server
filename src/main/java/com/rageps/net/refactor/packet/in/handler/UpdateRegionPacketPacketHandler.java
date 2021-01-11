package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.UpdateRegionPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.World;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.loc.Locations;
import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class UpdateRegionPacketPacketHandler implements PacketHandler<UpdateRegionPacketPacket> {

    @Override
    public void handle(Player player, UpdateRegionPacketPacket packet) {
        if(player.isUpdateRegion()) {
            World.getRegions().updateRegionObjects(player);
            Locations.process(player);
            player.getTolerance().reset();
            player.setUpdates(false, false);
            player.setUpdateRegion(false);
            ObjectList<Region> surrounding = World.getRegions().getAllSurroundingRegions(player.getPosition().getRegion());
            if(surrounding != null) {
                for(Region r : surrounding) {
                    if(r.getState() != EntityState.ACTIVE) {
                        r.setState(EntityState.ACTIVE);
                    }
                    r.onEnter(player);
                }
            }
            if(player.getRights().greater(Rights.ADMINISTRATOR))
                player.message("DEBUG[region= " + player.getPosition().getRegion() + "]");
        }
    }
}
