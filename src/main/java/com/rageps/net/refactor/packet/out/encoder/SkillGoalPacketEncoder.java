package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.SkillGoalPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SkillGoalPacketEncoder implements PacketEncoder<SkillGoalPacket> {

    @Override
    public GamePacket encode(SkillGoalPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(135);
        builder.put(message.getId());
        builder.put(message.getGoal());
        return builder.toGamePacket();
    }
}
