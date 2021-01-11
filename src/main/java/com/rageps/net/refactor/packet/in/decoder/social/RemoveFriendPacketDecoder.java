package com.rageps.net.refactor.packet.in.decoder.social;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.social.AddFriendPacket;
import com.rageps.net.refactor.packet.in.model.social.RemoveFriendPacket;

/**
 *@author Tamatea <tamateea@gmail.com>
 */
public class RemoveFriendPacketDecoder implements PacketDecoder<RemoveFriendPacket> {

    @Override
    public RemoveFriendPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        long name = reader.getLong();
        return new RemoveFriendPacket(name);
    }
}
