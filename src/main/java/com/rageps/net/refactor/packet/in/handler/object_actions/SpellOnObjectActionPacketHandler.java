package com.rageps.net.refactor.packet.in.handler.object_actions;

import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.object_actions.SpellOnObjectPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.*;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Boundary;
import com.rageps.world.locale.Position;

import java.util.Optional;


/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SpellOnObjectActionPacketHandler implements PacketHandler<SpellOnObjectPacket> {

    @Override
    public void handle(Player player, SpellOnObjectPacket packet) {
        int objectId = packet.getObjectId();
        int objectX = packet.getObjectX();
        int objectY = packet.getObjectY();
        int spell = packet.getSpellId();

        //Validating data.
        Position position = new Position(objectX, objectY, player.getPosition().getZ());
        if(spell < 0 || objectId < 0 || objectX < 0 || objectY < 0)
            return;
        Region reg = World.getRegions().getRegion(position);
        if(reg == null)
            return;
        Optional<GameObject> o = reg.getObject(objectId, position.toLocalPacked());
        if(!o.isPresent())
            return;
        //Controlling data.
        player.facePosition(position);
        final GameObject object = o.get();
        player.getMovementListener().append(() -> {
            if(new Boundary(position, object.getDefinition().getSize()).within(player.getPosition(), player.size(), 1)) {
                switch(objectId) {
                    //TODO: Create event if will be used.
                }
            }
        });
    }


}
