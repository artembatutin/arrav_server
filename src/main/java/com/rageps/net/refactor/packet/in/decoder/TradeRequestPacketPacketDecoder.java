package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.TradeRequestPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class TradeRequestPacketPacketDecoder implements PacketDecoder<TradeRequestPacketPacket> {

    @Override
    public TradeRequestPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int index = reader.getShort(true, DataOrder.LITTLE);
        return new TradeRequestPacketPacket(index);
    }
}
