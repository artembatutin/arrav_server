package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.DataType;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CoordinatesPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CoordinatesPacketEncoder implements PacketEncoder<CoordinatesPacket> {

    @Override
    public GamePacket encode(CoordinatesPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(85);
        builder.put(DataType.BYTE, DataTransformation.NEGATE, message.getPosition().getY() - (message.getLastRegion().getRegionY() * 8));
        builder.put(DataType.BYTE, DataTransformation.NEGATE, message.getPosition().getX() - (message.getLastRegion().getRegionX() * 8));
        return null;
    }


}
