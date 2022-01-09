package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.MagicOnItemPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MagicOnItemPacketPacketDecoder implements PacketDecoder<MagicOnItemPacketPacket> {

    @Override
    public MagicOnItemPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int slot = reader.getShort(true, DataTransformation.NONE);
        int id = reader.getShort(true, DataTransformation.ADD);
        int interfaceId = reader.getShort(true, DataTransformation.NEGATE);
        int spellId = reader.getShort(true, DataTransformation.ADD);


        return new MagicOnItemPacketPacket(slot, id, interfaceId, spellId);
    }
}
