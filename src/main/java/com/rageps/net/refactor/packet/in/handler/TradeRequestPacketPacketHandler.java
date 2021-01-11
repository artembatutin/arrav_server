package com.rageps.net.refactor.packet.in.handler;

import com.rageps.GameConstants;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.net.refactor.packet.in.model.TradeRequestPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.container.session.test.impl.TradeSession;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class TradeRequestPacketPacketHandler implements PacketHandler<TradeRequestPacketPacket> {

    @Override
    public void handle(Player player, TradeRequestPacketPacket packet) {
        if(player.getActivityManager().contains(ActivityManager.ActivityType.REQUEST_MESSAGE))
            return;

        int index = packet.getIndex();

        Player other = World.get().getPlayers().get(index - 1);
        if(GameConstants.TRADE_DISABLED) {
            player.message("Trading has been temporarily disabled!");
        }
        if(other == null || !other.getPosition().isViewableFrom(player.getPosition()) || other.same(player))
            return;
        if(!MinigameHandler.execute(player, m -> m.canTrade(player, other)))
            return;
        //ExchangeSessionManager.get().request(new TradeSession(player, other, ExchangeSession.REQUEST));
        player.exchange_manager.request(new TradeSession(player, other));

        player.getActivityManager().execute(ActivityManager.ActivityType.REQUEST_MESSAGE);

    }
}
