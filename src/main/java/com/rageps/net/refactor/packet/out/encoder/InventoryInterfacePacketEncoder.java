package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.InventoryInterfacePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InventoryInterfacePacketEncoder implements PacketEncoder<InventoryInterfacePacket> {

    @Override
    public GamePacket encode(InventoryInterfacePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(248);
        builder.putShort(message.getOpen(), DataTransformation.ADD);
        builder.putShort(message.getOverlay());
        return builder.toGamePacket();
    }
}
