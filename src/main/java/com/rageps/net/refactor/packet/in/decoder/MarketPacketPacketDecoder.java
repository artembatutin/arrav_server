package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.MarketPacketPacket;
import com.rageps.util.TextUtils;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MarketPacketPacketDecoder implements PacketDecoder<MarketPacketPacket> {

    @Override
    public MarketPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        String search = TextUtils.hashToName(reader.getLong());

        return new MarketPacketPacket(search);
    }
}
