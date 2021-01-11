package com.rageps.net.refactor.packet.in.handler;

import com.rageps.content.market.MarketShop;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.packet.in.model.MovementQueuePacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.World;
import com.rageps.world.entity.actor.move.MovementQueue;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.locale.Position;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MovementQueuePacketPacketHandler implements PacketHandler<MovementQueuePacketPacket> {

    @Override
    public void handle(Player player, MovementQueuePacketPacket packet) {

        if(player.getActivityManager().contains(ActivityManager.ActivityType.WALKING)) {
            return;
        }
        if(player.getMovementQueue().isLockMovement()) {
            return;
        }
        if(!MinigameHandler.execute(player, m -> m.canWalk(player))) {
            return;
        }

        boolean reset = false;
        player.faceEntity(null);
        if(packet.getOpcode() == 248) {
            player.setFollowing(false);
            player.getCombat().reset(false, true);
            reset = true;
        }

        if(packet.getOpcode() == 164) {
            player.setFollowing(false);
            if(!reset) {
                player.getCombat().reset(false, true);
                reset = true;
            }
        } else if(packet.getOpcode() == 98) {

        }

        if(player.isFrozen()) {
            player.message("You are frozen and unable to move!");
            return;
        }

        if(player.getDialogueBuilder() != null && !player.getMovementQueue().isLockMovement())
            player.getDialogueBuilder().interrupt();

        if(!reset) {
            player.getCombat().reset(false, true);
            reset = true;
        }

        player.closeWidget();
        if(player.getMarketShop() != null) {
            MarketShop.clearFromShop(player);
        }

        if(player.getMovementQueue().check()) {
            if(!reset) {
                player.getMovementQueue().reset();
            }
            boolean run = packet.isRunning();
            Position[] steps = packet.getSteps();
            MovementQueue queue = player.getMovementQueue();

            queue.setRunPath(run);

            for (Position step : steps) {
                queue.addToPath(step);
            }
            queue.finish();
        }

        if(World.get().getEnvironment().isDebug() && player.getRights().greater(Rights.ADMINISTRATOR)) {
            player.message("DEBUG[walking= " + player.getPosition().getRegion() + "]");
        }
        player.getActivityManager().execute(ActivityManager.ActivityType.WALKING);
    }
}
