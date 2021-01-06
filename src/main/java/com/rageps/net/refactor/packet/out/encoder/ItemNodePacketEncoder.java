package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ItemNodePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemNodePacketEncoder implements PacketEncoder<ItemNodePacket> {

    @Override
    public GamePacket encode(ItemNodePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(0);
        return builder.toGamePacket();
    }
}
