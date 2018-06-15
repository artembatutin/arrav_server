package net.arrav.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.util.TextUtils;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager;

import java.util.Optional;

public final class EnterInputPacket implements IncomingPacket {
	
	public static final int ENTER_AMOUNT_OPCODE = 208, ENTER_SYNTAX_OPCODE = 60;
	
	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ENTER_INPUT)) {
			return;
		}
		
		switch(opcode) {
			case ENTER_AMOUNT_OPCODE:
				enterAmount(player, buf);
				break;
			case ENTER_SYNTAX_OPCODE:
				enterSyntax(player, buf);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.ENTER_INPUT);
	}
	
	/**
	 * The enter amount packet which deals with numerical values.
	 * @param player the player this packet is sent for.
	 * @param buf the buffer chained to this packet.
	 */
	private void enterAmount(Player player, ByteBuf buf) {
		int amount = buf.getInt();
		if(amount < 1) {
			return;
		}
		player.getEnterInputListener().ifPresent(t -> t.apply(Integer.toString(amount)).execute());
		player.setEnterInputListener(Optional.empty());
	}
	
	/**
	 * The enter syntax packet which deals with alphabetical values.
	 * @param player the player this packet is sent for.
	 * @param buf the buffer chained to this packet.
	 */
	private void enterSyntax(Player player, ByteBuf buf) {
		String name = TextUtils.hashToName(buf.getLong());
		if(name.isEmpty()) {
			return;
		}
		player.getEnterInputListener().ifPresent(t -> t.apply(name).execute());
		player.setEnterInputListener(Optional.empty());
	}
}
