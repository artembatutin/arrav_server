package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CameraResetPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CameraResetPacketEncoder implements PacketEncoder<CameraResetPacket> {
    @Override
    public GamePacket encode(CameraResetPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(107);
        return builder.toGamePacket();
    }
}
