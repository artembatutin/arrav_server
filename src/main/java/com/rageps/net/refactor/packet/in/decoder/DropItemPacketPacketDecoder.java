package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.DropItemPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class DropItemPacketPacketDecoder implements PacketDecoder<DropItemPacketPacket> {

    @Override
    public DropItemPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int id = reader.getShort(false, DataTransformation.ADD);
        reader.get(false);
        reader.get(false);
        int slot = reader.getShort(false, DataTransformation.ADD);

        return new DropItemPacketPacket(id, slot);
    }
}
