package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ClanBannedPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ClanBannedPacketEncoder implements PacketEncoder<ClanBannedPacket> {

    @Override
    public GamePacket encode(ClanBannedPacket message) {
        throw new UnsupportedOperationException("No longer used, remove this from client.");
    }
}
