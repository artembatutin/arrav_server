package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ScorePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ScorePacketEncoder implements PacketEncoder<ScorePacket> {

    @Override
    public GamePacket encode(ScorePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(30, PacketType.VARIABLE_SHORT);
        builder.putShort(message.getIndex());
        builder.putShort(message.getKills());
        builder.putShort(message.getDeaths());
        builder.putShort(message.getKillstreak());
        builder.putString(message.getTitle());
        return builder.toGamePacket();
    }
}
