package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.PrivacyOptionPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PrivacyOptionPacketPacketDecoder implements PacketDecoder<PrivacyOptionPacketPacket> {

    @Override
    public PrivacyOptionPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        final int publicMode = reader.get();
        final int privateMode = reader.get();
        final int tradeMode = reader.get();
        final int clanMode = reader.get();


        return new PrivacyOptionPacketPacket(publicMode, privateMode, tradeMode, clanMode);
    }
}
