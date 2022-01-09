package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.EnergyPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnergyPacketEncoder implements PacketEncoder<EnergyPacket> {

    @Override
    public GamePacket encode(EnergyPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(110);
        builder.put(message.getEnergy());
        return builder.toGamePacket();
    }
}
