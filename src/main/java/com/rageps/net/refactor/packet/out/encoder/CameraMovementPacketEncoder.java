package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CameraMovementPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CameraMovementPacketEncoder implements PacketEncoder<CameraMovementPacket> {

    @Override
    public GamePacket encode(CameraMovementPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(166);
        builder.put(message.getPosition().getLocalX(message.getBase()));
        builder.put(message.getPosition().getLocalY(message.getBase()));
        builder.putShort(message.getHeight());
        builder.put(message.getMovementSpeed());
        builder.put(message.getRotationSpeed());
        return builder.toGamePacket();
    }
}
