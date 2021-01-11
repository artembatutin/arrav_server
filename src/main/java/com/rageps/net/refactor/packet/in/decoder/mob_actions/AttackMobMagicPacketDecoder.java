package com.rageps.net.refactor.packet.in.decoder.mob_actions;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.mob_actions.AttackMagicMobPacket;
import com.rageps.net.refactor.packet.in.model.mob_actions.ThirdMobActionPacket;

/**
 *@author Tamatea <tamateea@gmail.com>
 */
public class AttackMobMagicPacketDecoder implements PacketDecoder<AttackMagicMobPacket> {

    @Override
    public AttackMagicMobPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        
        int index = reader.getShort(true, DataTransformation.ADD, DataOrder.LITTLE);
        int spellId = reader.get(true, DataTransformation.ADD);
        
        return new AttackMagicMobPacket(index, spellId);
    }
}
