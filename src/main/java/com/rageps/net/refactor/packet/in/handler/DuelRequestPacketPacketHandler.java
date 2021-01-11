package com.rageps.net.refactor.packet.in.handler;

import com.rageps.GameConstants;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.DuelRequestPacketPacket;
import com.rageps.net.refactor.packet.in.model.TradeRequestPacketPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.container.session.ExchangeSession;
import com.rageps.world.entity.item.container.session.ExchangeSessionManager;
import com.rageps.world.entity.item.container.session.impl.DuelSession;
import com.rageps.world.entity.item.container.session.test.impl.TradeSession;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class DuelRequestPacketPacketHandler implements PacketHandler<DuelRequestPacketPacket> {

    @Override
    public void handle(Player player, DuelRequestPacketPacket packet) {
        if(player.getActivityManager().contains(ActivityManager.ActivityType.REQUEST_MESSAGE))
            return;

        int index = packet.getIndex();

        Player other = World.get().getPlayers().get(index - 1);
        if(GameConstants.DUEL_DISABLED) {
            player.message("Duelling has been temporarily disabled!");
        }
        if(other == null || !other.getPosition().isViewableFrom(player.getPosition()) || other.same(player))
            return;
        ExchangeSessionManager.get().request(new DuelSession(player, other, ExchangeSession.REQUEST));

        player.getActivityManager().execute(ActivityManager.ActivityType.REQUEST_MESSAGE);

    }
}
