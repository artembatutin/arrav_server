package com.rageps.net.refactor.packet.in.decoder.mob_actions;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.mob_actions.ThirdMobActionPacket;

/**
 *@author Tamatea <tamateea@gmail.com>
 */
public class ThirdActionMobPacketDecoder implements PacketDecoder<ThirdMobActionPacket> {

    @Override
    public ThirdMobActionPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        
        int index = reader.getShort(true);
        
        return new ThirdMobActionPacket(index);
    }
}
