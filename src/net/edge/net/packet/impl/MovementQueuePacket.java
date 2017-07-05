package net.edge.net.packet.impl;

import net.edge.Server;
import net.edge.content.market.MarketShop;
import net.edge.content.minigame.MinigameHandler;
import net.edge.locale.Position;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.PacketReader;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;

/**
 * The message sent from the client when a player makes a yellow {@code X} click,
 * a red {@code X} click, or when they click the minimap.
 * @author lare96 <http://github.com/lare96>
 */
public final class MovementQueuePacket implements PacketReader {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.WALKING)) {
			return;
		}
		if(player.getMovementQueue().isLockMovement()) {
			return;
		}
		if(!MinigameHandler.execute(player, m -> m.canWalk(player))) {
			return;
		}
		
		player.faceEntity(null);
		
		if(opcode == 248) {
			player.setFollowing(false);
			player.getCombatBuilder().cooldown(false);
			size -= 14;
		}
		
		if(opcode == 164) {
			player.setFollowing(false);
			player.getCombatBuilder().cooldown(false);
		} else if(opcode == 98) {
			
		}
		
		if(player.isFrozen()) {
			player.message("You are frozen and unable to " + "move!");
			return;
		}
		
		if(player.getDialogueBuilder() != null && !player.getMovementQueue().isLockMovement())
			player.getDialogueBuilder().interrupt();
		
		player.getMessages().sendCloseWindows();
		if(player.getMarketShop() != null) {
			MarketShop.clearFromShop(player);
		}
		
		int steps = (size - 5) / 2;
		int[][] path = new int[steps][2];
		int firstStepX = payload.getShort(ByteTransform.A, ByteOrder.LITTLE);
		
		for(int i = 0; i < steps; i++) {
			path[i][0] = payload.get();
			path[i][1] = payload.get();
		}
		int firstStepY = payload.getShort(ByteOrder.LITTLE);
		player.getMovementQueue().reset();
		player.getMovementQueue().setRunPath(payload.get(ByteTransform.C) == 1);
		player.getMovementQueue().addToPath(new Position(firstStepX, firstStepY));
		
		for(int i = 0; i < steps; i++) {
			path[i][0] += firstStepX;
			path[i][1] += firstStepY;
			player.getMovementQueue().addToPath(new Position(path[i][0], path[i][1]));
		}
		player.getMovementQueue().finish();
		
		if(Server.DEBUG && player.getRights().greater(Rights.ADMINISTRATOR)) {
			player.message("DEBUG[walking= " + player.getPosition().getRegion() + "]");
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.WALKING);
	}
}
