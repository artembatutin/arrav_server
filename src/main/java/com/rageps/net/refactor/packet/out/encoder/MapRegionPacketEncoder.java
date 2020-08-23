package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.DataType;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.MapRegionPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MapRegionPacketEncoder implements PacketEncoder<MapRegionPacket> {

    @Override
    public GamePacket encode(MapRegionPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(73);
        builder.put(DataType.SHORT, DataTransformation.ADD, message.getPosition().getCentralRegionX());
        builder.put(DataType.SHORT, message.getPosition().getCentralRegionY());
        return builder.toGamePacket();
    }
}
