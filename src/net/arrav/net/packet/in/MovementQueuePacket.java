package net.arrav.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.arrav.Arrav;
import net.arrav.content.market.MarketShop;
import net.arrav.content.minigame.MinigameHandler;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager;
import net.arrav.world.locale.Position;

/**
 * The message sent from the client when a player makes a yellow {@code X} click,
 * a red {@code X} click, or when they click the minimap.
 * @author lare96 <http://github.com/lare96>
 */
public final class MovementQueuePacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
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
		if(opcode == 248) {
			player.setFollowing(false);
			player.getCombat().reset(false, true);
			reset = true;
			size -= 14;
		}
		
		if(opcode == 164) {
			player.setFollowing(false);
			if(!reset) {
				player.getCombat().reset(false, true);
				reset = true;
			}
		} else if(opcode == 98) {

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
		
		int steps = (size - 5) / 2;
		int[][] path = new int[steps][2];
		int firstStepX = buf.getShort(ByteTransform.A, ByteOrder.LITTLE);
		
		for(int i = 0; i < steps; i++) {
			path[i][0] = buf.get();
			path[i][1] = buf.get();
		}
		
		if(player.getMovementQueue().check()) {
			int firstStepY = buf.getShort(ByteOrder.LITTLE);
			if(!reset) {
				player.getMovementQueue().reset();
			}
			player.getMovementQueue().setRunPath(buf.get(ByteTransform.C) == 1);
			player.getMovementQueue().addToPath(new Position(firstStepX, firstStepY));
			
			for(int i = 0; i < steps; i++) {
				path[i][0] += firstStepX;
				path[i][1] += firstStepY;
				player.getMovementQueue().addToPath(new Position(path[i][0], path[i][1]));
			}
			player.getMovementQueue().finish();
		}
		
		if(Arrav.DEBUG && player.getRights().greater(Rights.ADMINISTRATOR)) {
			player.message("DEBUG[walking= " + player.getPosition().getRegion() + "]");
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.WALKING);
	}
}
