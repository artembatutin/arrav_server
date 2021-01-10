package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.FollowPlayerPacketPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FollowPlayerPacketPacketDecoder implements PacketDecoder<FollowPlayerPacketPacket> {

    @Override
    public FollowPlayerPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int index = reader.getShort(false, DataOrder.LITTLE);

        if(index < 0 || index > World.get().getPlayers().capacity())
            return null;

        Player follow = World.get().getPlayers().get(index - 1);

        return new FollowPlayerPacketPacket(follow);
    }
}
