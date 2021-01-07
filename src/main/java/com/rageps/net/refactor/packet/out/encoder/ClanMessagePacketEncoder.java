package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ClanMessagePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ClanMessagePacketEncoder implements PacketEncoder<ClanMessagePacket> {

    @Override
    public GamePacket encode(ClanMessagePacket message) {
        throw new UnsupportedOperationException("No longer used, remove this from client.");
    }
}
