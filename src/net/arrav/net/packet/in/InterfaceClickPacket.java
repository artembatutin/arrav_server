package net.arrav.net.packet.in;

import net.arrav.content.minigame.MinigameHandler;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager;
import net.arrav.world.entity.item.container.session.ExchangeSessionManager;

/**
 * The message sent from the client when a player clicks certain options on an
 * interface.
 * @author lare96 <http://github.com/lare96>
 */
public final class InterfaceClickPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.INTERFACE_CLICK))
			return;
		
		if(ExchangeSessionManager.get().reset(player)) {
			return;
		}
		
		MinigameHandler.executeVoid(player, t -> t.onInterfaceClick(player));
		player.closeWidget();
		player.getActivityManager().execute(ActivityManager.ActivityType.INTERFACE_CLICK);
	}
}
