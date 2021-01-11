package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.OperateEquipmentPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class OperateEquipmentPacketPacketDecoder implements PacketDecoder<OperateEquipmentPacketPacket> {

    @Override
    public OperateEquipmentPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int option = reader.getShort(DataTransformation.ADD);
        int slot = reader.getShort(DataTransformation.ADD);

        return new OperateEquipmentPacketPacket(option, slot);
    }
}
