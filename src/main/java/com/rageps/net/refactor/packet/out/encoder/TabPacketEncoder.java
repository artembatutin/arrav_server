package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.TabPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class TabPacketEncoder implements PacketEncoder<TabPacket> {

    @Override
    public GamePacket encode(TabPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(71);
        builder.putShort(message.getId());
        builder.put(message.getTab().getOld(), DataTransformation.ADD);
        builder.put(message.getTab().getNew(), DataTransformation.ADD);
        return builder.toGamePacket();
    }
}
