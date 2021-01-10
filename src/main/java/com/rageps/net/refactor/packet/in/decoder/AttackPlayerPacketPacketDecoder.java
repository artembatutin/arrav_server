package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataType;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.AttackPlayerPacketPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class AttackPlayerPacketPacketDecoder implements PacketDecoder<AttackPlayerPacketPacket> {

    @Override
    public AttackPlayerPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int index = reader.getShort(true, DataOrder.LITTLE);

        if(index < 0 || index > World.get().getPlayers().capacity())
            return null;

        Player victim = World.get().getPlayers().get(index - 1);
        return new AttackPlayerPacketPacket(victim);
    }
}
