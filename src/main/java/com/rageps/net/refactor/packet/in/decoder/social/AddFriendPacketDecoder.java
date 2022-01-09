package com.rageps.net.refactor.packet.in.decoder.social;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.AdvanceDialoguePacketPacket;
import com.rageps.net.refactor.packet.in.model.social.AddFriendPacket;

/**
 *@author Tamatea <tamateea@gmail.com>
 */
public class AddFriendPacketDecoder implements PacketDecoder<AddFriendPacket> {

    @Override
    public AddFriendPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        long name = reader.getLong();
        return new AddFriendPacket(name);
    }
}
