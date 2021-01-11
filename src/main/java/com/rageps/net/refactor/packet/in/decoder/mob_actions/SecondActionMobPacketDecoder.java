package com.rageps.net.refactor.packet.in.decoder.mob_actions;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.mob_actions.SecondMobActionPacket;

/**
 *@author Tamatea <tamateea@gmail.com>
 */
public class SecondActionMobPacketDecoder implements PacketDecoder<SecondMobActionPacket> {

    @Override
    public SecondMobActionPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int index = reader.getShort(false, DataTransformation.ADD, DataOrder.LITTLE);

        return new SecondMobActionPacket(index);
    }
}
