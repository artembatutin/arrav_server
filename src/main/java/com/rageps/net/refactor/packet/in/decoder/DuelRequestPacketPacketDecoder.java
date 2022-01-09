package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.DuelRequestPacketPacket;
import com.rageps.net.refactor.packet.in.model.TradeRequestPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class DuelRequestPacketPacketDecoder implements PacketDecoder<DuelRequestPacketPacket> {

    @Override
    public DuelRequestPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int index = reader.getShort(true, DataOrder.LITTLE);
        return new DuelRequestPacketPacket(index);
    }
}
