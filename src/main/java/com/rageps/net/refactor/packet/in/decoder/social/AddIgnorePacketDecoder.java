package com.rageps.net.refactor.packet.in.decoder.social;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.social.AddIgnorePacket;
import com.rageps.net.refactor.packet.in.model.social.RemoveFriendPacket;

/**
 *@author Tamatea <tamateea@gmail.com>
 */
public class AddIgnorePacketDecoder implements PacketDecoder<AddIgnorePacket> {

    @Override
    public AddIgnorePacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        long name = reader.getLong();
        return new AddIgnorePacket(name);
    }
}
