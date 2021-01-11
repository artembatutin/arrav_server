package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.InterfaceActionPacketPacket;

/**
 * The message sent from the client when the player clicks some sort of button or
 * module.
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceActionPacketPacketDecoder implements PacketDecoder<InterfaceActionPacketPacket> {

    @Override
    public InterfaceActionPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int interfaceId = reader.getInt();
        int action = reader.getInt();

        return new InterfaceActionPacketPacket(interfaceId, action);
    }
}
