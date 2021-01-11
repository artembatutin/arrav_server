package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.DefaultPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class DefaultPacketPacketDecoder implements PacketDecoder<DefaultPacketPacket> {

    @Override
    public DefaultPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        return new DefaultPacketPacket();
    }
}
