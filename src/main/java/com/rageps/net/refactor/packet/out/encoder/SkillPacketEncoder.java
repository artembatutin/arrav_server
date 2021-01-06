package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.SkillPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SkillPacketEncoder implements PacketEncoder<SkillPacket> {

    @Override
    public GamePacket encode(SkillPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(0);
        return builder.toGamePacket();
    }
}
