package com.rageps.net.refactor.packet.in.decoder.interface_actions;

import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.interface_actions.FirstItemInterfacePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FirstItemInterfacePacketDecoder implements PacketDecoder<FirstItemInterfacePacket> {

    @Override
    public FirstItemInterfacePacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int interfaceId = reader.getShort(DataTransformation.ADD);
        int slot = reader.getShort(DataTransformation.ADD);
        int itemId = reader.getShort(DataTransformation.ADD);

        return new FirstItemInterfacePacket(interfaceId, slot, itemId);
    }
}
