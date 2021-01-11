package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.ConstructionPacketPacket;

/**
 * This message sent from the client when the player clicks a construction panel button.
 * @author Tamatea <tamateea@gmail.com>
 */
public class ConstructionPacketPacketDecoder implements PacketDecoder<ConstructionPacketPacket> {

    @Override
    public ConstructionPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int click = reader.get();
        return new ConstructionPacketPacket(click);
    }
}
