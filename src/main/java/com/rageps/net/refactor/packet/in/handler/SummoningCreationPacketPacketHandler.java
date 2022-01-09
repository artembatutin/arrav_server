package com.rageps.net.refactor.packet.in.handler;

import com.rageps.content.skill.summoning.PouchCreation;
import com.rageps.content.skill.summoning.SummoningData;
import com.rageps.net.refactor.packet.in.model.SummoningCreationPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

import static com.rageps.content.skill.summoning.SummoningData.VALUES;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SummoningCreationPacketPacketHandler implements PacketHandler<SummoningCreationPacketPacket> {

    @Override
    public void handle(Player player, SummoningCreationPacketPacket packet) {

        int click = packet.getClick();

        if(click < 0 || click >= VALUES.length)
            return;
            //todo verification
        SummoningData data = VALUES[click];
        PouchCreation creation = new PouchCreation(player, data);
        creation.start();

    }
}
