package com.rageps.net.refactor.packet.in.handler;

import com.rageps.content.dialogue.impl.StatementDialogue;
import com.rageps.content.market.MarketShop;
import com.rageps.net.refactor.packet.in.model.MarketPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * The message that is sent from the client when the player searches an item in the market.
 * todo - verification?
 * @author Tamatea <tamateea@gmail.com>
 */
public class MarketPacketPacketHandler implements PacketHandler<MarketPacketPacket> {

    @Override
    public void handle(Player player, MarketPacketPacket packet) {

        String search = packet.getText();

        if(player.isIronMan() && !player.isIronMaxed()) {
            player.getDialogueBuilder().append(new StatementDialogue("You are in iron man mode.", "Therefore you can't search the global market.", "Once you max-out you will be able to."));
            return;
        }
        new MarketShop(player, search);
    }
}
