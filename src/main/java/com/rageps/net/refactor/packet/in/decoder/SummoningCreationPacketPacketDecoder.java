package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.SummoningCreationPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SummoningCreationPacketPacketDecoder implements PacketDecoder<SummoningCreationPacketPacket> {

    @Override
    public SummoningCreationPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int click = reader.get();

        return new SummoningCreationPacketPacket(click);
    }
}
