package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.AdvanceDialoguePacketPacket;
import com.rageps.net.refactor.packet.in.model.MagicOnPlayerPacket;

/**
 *@author Tamatea <tamateea@gmail.com>
 */
public class MagicOnPlayerPacketDecoder implements PacketDecoder<MagicOnPlayerPacket> {

    @Override
    public MagicOnPlayerPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int index = reader.getShort(true, DataTransformation.ADD);
        int spellId = reader.getShort(true, DataOrder.LITTLE);

        return new MagicOnPlayerPacket(index, spellId);
    }
}
