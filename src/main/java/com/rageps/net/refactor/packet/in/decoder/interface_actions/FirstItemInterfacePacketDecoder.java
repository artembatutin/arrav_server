package com.rageps.net.refactor.packet.in.decoder.interface_actions;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.ItemInterfacePacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FirstItemInterfacePacketDecoder implements PacketDecoder<ItemInterfacePacketPacket> {

    @Override
    public ItemInterfacePacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        return new ItemInterfacePacketPacket();
    }
}
