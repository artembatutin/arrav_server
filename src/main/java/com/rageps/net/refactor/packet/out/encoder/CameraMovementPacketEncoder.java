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
        GamePacketBuilder builder = new GamePacketBuilder(0);
        return builder.toGamePacket();
    }
}
