package com.rageps.net.refactor.packet.in.decoder.object_actions;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.object_actions.FourthActionObjectPacket;
import com.rageps.net.refactor.packet.in.model.object_actions.ThirdActionObjectPacket;

/**
 *@author Tamatea <tamateea@gmail.com>
 */
public class FourthActionObjectPacketDecoder implements PacketDecoder<FourthActionObjectPacket> {

    @Override
    public FourthActionObjectPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int objectId = reader.getMedium();
        int objectX = reader.getShort(false);
        int objectY = reader.getShort(false);

        return new FourthActionObjectPacket(objectId, objectX, objectY);
    }
}
