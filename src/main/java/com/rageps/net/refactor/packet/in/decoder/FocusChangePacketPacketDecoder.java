package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.FocusChangePacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FocusChangePacketPacketDecoder implements PacketDecoder<FocusChangePacketPacket> {

    @Override
    public FocusChangePacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        return new FocusChangePacketPacket();
    }
}
