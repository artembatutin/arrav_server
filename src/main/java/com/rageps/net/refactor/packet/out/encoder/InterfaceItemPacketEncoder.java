package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.InterfaceItemPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceItemPacketEncoder implements PacketEncoder<InterfaceItemPacket> {

    @Override
    public GamePacket encode(InterfaceItemPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(0);
        builder.putInt(message.getWidget());
        builder.putInt(message.getItemId());
        return builder.toGamePacket();
    }
}
