package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ItemsOnInterfacePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemsOnInterfacePacketEncoder implements PacketEncoder<ItemsOnInterfacePacket> {

    @Override
    public GamePacket encode(ItemsOnInterfacePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(0);
        return builder.toGamePacket();
    }
}
