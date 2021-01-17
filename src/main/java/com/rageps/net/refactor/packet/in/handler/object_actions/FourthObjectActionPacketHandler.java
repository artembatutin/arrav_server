package com.rageps.net.refactor.packet.in.handler.object_actions;

import com.rageps.action.impl.ObjectAction;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.object_actions.FourthActionObjectPacket;
import com.rageps.net.refactor.packet.in.model.object_actions.ThirdActionObjectPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.object.*;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Boundary;
import com.rageps.world.locale.Position;

import java.util.Optional;

import static com.rageps.action.ActionContainers.*;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FourthObjectActionPacketHandler implements PacketHandler<FourthActionObjectPacket> {

    @Override
    public void handle(Player player, FourthActionObjectPacket packet) {
        int objectId = packet.getObjectId();
        int objectX = packet.getObjectX();
        int objectY = packet.getObjectY();

        //Validating data.
        Position position = new Position(objectX, objectY, player.getPosition().getZ());
        if(objectId < 0 || objectX < 0 || objectY < 0)
            return;
        player.facePosition(position);
        //construction clicks.
        if(player.getHouse().isOwnerHome()) {
            ObjectAction e = CONSTRUCTION.get(objectId);
            player.message(objectId + "");
            if(e != null) {
                player.getHouse().get().getPlan().setObjectX(objectX);
                player.getHouse().get().getPlan().setObjectY(objectY);
                Boundary boundary = new Boundary(position, ObjectDefinition.DEFINITIONS[objectId].getSize());
                player.getMovementListener().append(() -> {
                    if(boundary.within(player.getPosition(), player.size(), 1)) {
                        e.click(player, new DynamicObject(objectId, new Position(objectX, objectY, player.getPosition().getZ()), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, player.getInstance()), 4);
                    }
                });
            }
        }
        Region reg = World.getRegions().getRegion(position);
        if(reg == null)
            return;
        Optional<GameObject> o = reg.getObject(objectId, position.toLocalPacked());
        if(!o.isPresent())
            return;
        final GameObject object = o.get();
        if(player.getRights().equals(Rights.ADMINISTRATOR))
            player.message("[OBJ 1]:" + object.getId() + " - " + object.getPosition().toString());
        boolean distanceIgnore =  (objectId == 85584 || objectId == 85532 || objectId == 85534);
        Boundary boundary = new Boundary(position, object.getDefinition().getSize());

        player.getMovementListener().append(() -> {
            if (distanceIgnore || boundary.within(player.getPosition(), player.size(), 1)) {

                if(!MinigameHandler.execute(player, m -> m.onFourthClickObject(player, object)))
                    return;
                if(event(player, object))
                    return;
            }
        });
    }

    /**
     * Tries to handle the {@link ObjectAction} action.
     */
    private boolean event(Player player, GameObject object) {
        ObjectAction e = OBJECT_FOURTH.get(object.getId());
        if(e != null) {
            return e.click(player, object, 4);
        }
        return false;
    }

}
