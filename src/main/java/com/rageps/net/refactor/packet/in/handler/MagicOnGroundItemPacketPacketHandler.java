package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.MagicOnGroundItemPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MagicOnGroundItemPacketPacketHandler implements PacketHandler<MagicOnGroundItemPacketPacket> {

    @Override
    public void handle(Player player, MagicOnGroundItemPacketPacket packet) {

        int x = packet.getX();
        int y = packet.getY();
        int itemId = packet.getItemId();
        int spellId = packet.getSpellId();

        if(World.get().getEnvironment().isDebug()) {
            player.message("item = " + itemId + ", spell = " + spellId + ", x = " + x + ", y = " + y + ".");
        }


    }
}
