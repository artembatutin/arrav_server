package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.LogoutPacket;
import com.rageps.world.World;
import com.rageps.world.entity.EntityState;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class LogoutPacketEncoder implements PacketEncoder<LogoutPacket> {

    @Override
    public boolean onSent(LogoutPacket message) {
        if(message.getPlayer().getState() != EntityState.AWAITING_REMOVAL && message.getPlayer().getState() != EntityState.INACTIVE)
            //World.get().queueLogout(player);
            World.get().getGameService().unregisterPlayer(message.getPlayer());

        return true;
    }

    @Override
    public GamePacket encode(LogoutPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(109, PacketType.VARIABLE_SHORT);
        return builder.toGamePacket();
    }
}
