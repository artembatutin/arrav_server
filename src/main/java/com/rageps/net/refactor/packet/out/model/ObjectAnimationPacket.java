package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.ObjectDirection;
import com.rageps.world.entity.object.ObjectType;
import com.rageps.world.locale.Position;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ObjectAnimationPacket extends Packet {

    private final Position position;
    private final int type;
    private final int direction;
    private final int animation;
    private final Player player;

    public ObjectAnimationPacket(Player player, Position position, int animation, ObjectType type, ObjectDirection direction) {
        this.position = position;
        this.animation = animation;
        this.type = type.getId();
        this.direction = direction.getId();
        this.player = player;
    }

    public ObjectAnimationPacket(Player player, Position position, int animation, ObjectType type, int direction) {
        this.position = position;
        this.animation = animation;
        this.type = type.getId();
        this.direction = direction;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Position getPosition() {
        return position;
    }

    public int getAnimation() {
        return animation;
    }

    public int getDirection() {
        return direction;
    }

    public int getType() {
        return type;
    }
}