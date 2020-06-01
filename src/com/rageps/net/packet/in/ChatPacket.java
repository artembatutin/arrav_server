package com.rageps.net.packet.in;

import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager.ActivityType;
import com.rageps.world.entity.actor.update.UpdateFlag;

/**
 * The message sent from the client when the player speaks.
 * @author lare96 <http://github.com/lare96>
 */
public final class ChatPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityType.CHAT_MESSAGE))
			return;
		if(player.muted || player.ipMuted) {
			player.message("You are currently muted.");
			return;
		}
		int effects = buf.get(false, ByteTransform.S);
		int color = buf.get(false, ByteTransform.S);
		int chatLength = (size - 2);
		byte[] text = buf.getBytesReverse(chatLength, ByteTransform.A);
		if(effects < 0 || color < 0 || chatLength < 0)
			return;
		player.setChatEffects(effects);
		player.setChatColor(color);
		player.setChatText(text);
		player.getFlags().flag(UpdateFlag.CHAT);
		player.getActivityManager().execute(ActivityType.CHAT_MESSAGE);
	}
}
