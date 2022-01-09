package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.IdleStatePacketPacket;
import com.rageps.world.entity.actor.player.Player;

/**
 * The message sent from the client when an {@link Player} enters an idle state.
 * @author Tamatea <tamateea@gmail.com>
 */
public class IdleStatePacketPacketDecoder implements PacketDecoder<IdleStatePacketPacket> {

    @Override
    public IdleStatePacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        return new IdleStatePacketPacket();
    }
}
