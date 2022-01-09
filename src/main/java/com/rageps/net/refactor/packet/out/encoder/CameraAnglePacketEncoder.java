package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CameraAnglePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CameraAnglePacketEncoder implements PacketEncoder<CameraAnglePacket> {

    @Override
    public GamePacket encode(CameraAnglePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(177);
        builder.put(message.getPosition().getLocalX(message.getBase()));
        builder.put(message.getPosition().getLocalY(message.getBase()));
        builder.putShort(message.getHeight());
        builder.put(message.getMovementSpeed());
        builder.put(message.getRotationSpeed());
        return builder.toGamePacket();
    }
}
