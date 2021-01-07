package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.ObjectActionPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ObjectActionPacketPacketDecoder implements PacketDecoder<ObjectActionPacketPacket> {

    @Override
    public ObjectActionPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        return new ObjectActionPacketPacket();
    }
}
