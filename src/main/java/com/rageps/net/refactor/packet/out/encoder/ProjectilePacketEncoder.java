package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CoordinatesPacket;
import com.rageps.net.refactor.packet.out.model.ProjectilePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ProjectilePacketEncoder implements PacketEncoder<ProjectilePacket> {

    @Override
    public GamePacket encode(ProjectilePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(117);
        builder.put(0);
        builder.put(message.getOffset().getX());
        builder.put(message.getOffset().getY());
        builder.putShort(message.getLockon());
        builder.putShort(message.getGfxMoving());
        builder.put(message.getStartHeight());
        builder.put(message.getEndHeight());
        builder.putShort(message.getTime());
        builder.putShort(message.getSpeed());
        builder.put(16);
        builder.put(64);
        return builder.toGamePacket();
    }

    @Override
    public CoordinatesPacket coordinatePacket(ProjectilePacket message) {
        return new CoordinatesPacket(message.getPosition(), message.getPlayer().getLastRegion());
    }
}
