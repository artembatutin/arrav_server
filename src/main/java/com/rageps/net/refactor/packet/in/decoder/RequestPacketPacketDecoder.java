package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.RequestPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class RequestPacketPacketDecoder implements PacketDecoder<RequestPacketPacket> {

    @Override
    public RequestPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        return new RequestPacketPacket();
    }
}
