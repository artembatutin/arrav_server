package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.AddFriendPacket;
import com.rageps.net.refactor.packet.out.model.ArrowPositionPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class AddFriendPacketEncoder implements PacketEncoder<AddFriendPacket> {

    @Override
    public GamePacket encode(AddFriendPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(50);
        int world = message.getWorld() != 0 ? message.getWorld() + 9 : message.getWorld();
        builder.putLong(message.getUsername());
        builder.put(world);
        builder.put(message.isDisplay()? 1 : 0);
        return builder.toGamePacket();
    }
}
