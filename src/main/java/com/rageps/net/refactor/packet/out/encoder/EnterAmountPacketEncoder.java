package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.EnterAmountPacket;
import com.rageps.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnterAmountPacketEncoder implements PacketEncoder<EnterAmountPacket> {

    @Override
    public GamePacket encode(EnterAmountPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(27, PacketType.VARIABLE_BYTE);
        builder.putString(message.getTitle());
        return builder.toGamePacket();
    }

    @Override
    public boolean onSent(EnterAmountPacket message) {
        message.getPlayer().setEnterInputListener(Optional.of(message.getAction()));
        return false;
    }

}
