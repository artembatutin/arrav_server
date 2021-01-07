package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CombatTargetPacket;
import com.rageps.net.refactor.packet.out.model.ProjectilePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CombatTargetPacketEncoder implements PacketEncoder<CombatTargetPacket> {

    @Override
    public GamePacket encode(CombatTargetPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(174);
        builder.putShort(message.getOpponent() == null ? 0 : message.getOpponent().getSlot());
        builder.put(message.getOpponent() == null ? 1 : message.getOpponent().isPlayer() ? 2 : 3);
        return builder.toGamePacket();
    }
}
