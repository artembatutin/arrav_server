package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ProjectilePacket extends Packet {

    private final Position position, offset;
    private final int speed, gfxMoving, startHeight, endHeight, lockon, time;
    private final Player player;

    public ProjectilePacket(Player player, Position position, Position offset, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) {
        this.position = position;
        this.offset = offset;
        this.speed = speed;
        this.gfxMoving = gfxMoving;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.lockon = lockon;
        this.time = time;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Position getPosition() {
        return position;
    }

    public int getTime() {
        return time;
    }

    public int getEndHeight() {
        return endHeight;
    }

    public int getGfxMoving() {
        return gfxMoving;
    }

    public int getLockon() {
        return lockon;
    }

    public int getSpeed() {
        return speed;
    }

    public int getStartHeight() {
        return startHeight;
    }

    public Position getOffset() {
        return offset;
    }
}