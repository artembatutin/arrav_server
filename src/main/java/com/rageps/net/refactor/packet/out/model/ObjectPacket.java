package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.object.DynamicObject;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.entity.object.ObjectDirection;
import com.rageps.world.entity.object.ObjectType;
import com.rageps.world.locale.Position;

import java.util.Optional;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ObjectPacket extends Packet {

    private final GameObject object;

    private final Player player;

    public ObjectPacket(Player player, GameObject object) {
        this.player = player;
        this.object = object;
    }

    public static void construction(Player player, int objectX, int objectY, int objectId, int face, int objectType, int height) {
        Optional<ObjectDirection> dir = ObjectDirection.valueOf(face);
        Optional<ObjectType> type = ObjectType.valueOf(objectType);
        if(!dir.isPresent()) {
            if(player.getRights() == Rights.ADMINISTRATOR)
                player.message("Couldn't find direction, " + face);
            return;
        }
        if(!type.isPresent()) {
            if(player.getRights() == Rights.ADMINISTRATOR)
                player.message("Couldn't find type, " + objectType);
            return;
        }
        player.send(new ObjectPacket(player, new DynamicObject(objectId, new Position(objectX, objectY, height), dir.get(), type.get(), false, 0, player.getInstance())));
    }

    public GameObject getObject() {
        return object;
    }

    public Player getPlayer() {
        return player;
    }
}