package com.rageps.net.refactor.packet.in.handler.social;

import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.social.RemoveIgnorePacket;
import com.rageps.net.refactor.packet.in.model.social.SendPMPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.relations.PrivateChatMessage;

import java.util.Optional;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SendPMPacketHandler implements PacketHandler<SendPMPacket> {

    @Override
    public void handle(Player player, SendPMPacket packet) {

        long to = packet.getTo();

        final Optional<Player> result = World.get().getWorldUtil().getPlayer(to);

        if (!result.isPresent()) {
            return;
        }

        final Player other = result.get();
        player.relations.message(other, new PrivateChatMessage(packet.getDecoded(), packet.getCompressed()));
    }
}
