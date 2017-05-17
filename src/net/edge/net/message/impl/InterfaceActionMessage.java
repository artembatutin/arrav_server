package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.message.InputMessageListener;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;
import net.edge.world.model.node.entity.player.assets.activity.ActivityManager;

/**
 * The message sent from the client when the player clicks some sort of button or
 * module.
 * @author lare96 <http://github.com/lare96>
 */
public final class InterfaceActionMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.INTERFACE_ACTION))
			return;
		int interfaceId = payload.getInt();
		int action = payload.getInt();
		if(player.getClan().isPresent()) {
			player.getClan().get().getClan().getSettings().click(player.getClan().get(), interfaceId, action);
		}
		if(interfaceId >= 50144 && interfaceId <= 50244) {
			if(player.getClan().isPresent()) {
				if(action == 0) {
					player.getClan().get().rank(interfaceId - 50144, 1);
				} else if(action == 1) {
					player.getClan().get().mute(interfaceId - 50144);
				} else if(action == 2) {
					player.getClan().get().ban(interfaceId - 50144);
				}
				return;
			}
		}
		if(interfaceId >= 400 && interfaceId <= 500) {
			if(player.getClan().isPresent()) {
				player.getClan().get().rank(interfaceId - 400, action);
				return;
			}
		}
		if(interfaceId >= 600 && interfaceId <= 700) {
			if(player.getClan().isPresent()) {
				if(action == 1) {
					player.getClan().get().unban(interfaceId - 600);
				} else {
					player.getClan().get().unmute(interfaceId - 600);
				}
			}
		}
		switch(interfaceId) {
			
			default:
				if(player.getRights().greater(Rights.ADMINISTRATOR))
					player.message("Interface: " + interfaceId + " - Action: " + action);
				break;
		}
		
		player.getActivityManager().execute(ActivityManager.ActivityType.INTERFACE_ACTION);
	}
}
