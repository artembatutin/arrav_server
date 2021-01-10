package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.EnterAmountPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnterAmountPacketPacketDecoder implements PacketDecoder<EnterAmountPacketPacket> {

    @Override
    public EnterAmountPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int amount = reader.getInt();
        return new EnterAmountPacketPacket(amount);
    }
}
