package net.edge.net.packet.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.packet.PacketReader;
import net.edge.util.TextUtils;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;

import java.util.Optional;

public final class EnterInputMessage implements PacketReader {
	
	public static final int ENTER_AMOUNT_OPCODE = 208, ENTER_SYNTAX_OPCODE = 60;
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ENTER_INPUT)) {
			return;
		}
		
		switch(opcode) {
			case ENTER_AMOUNT_OPCODE:
				enterAmount(player, payload);
				break;
			case ENTER_SYNTAX_OPCODE:
				enterSyntax(player, payload);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.ENTER_INPUT);
	}
	
	/**
	 * The enter amount packet which deals with numerical values.
	 * @param player  the player this packet is sent for.
	 * @param payload the payload chained to this packet.
	 */
	private void enterAmount(Player player, ByteMessage payload) {
		int amount = payload.getInt();
		if(amount < 1) {
			return;
		}
		player.getEnterInputListener().ifPresent(t -> t.apply(Integer.toString(amount)).execute());
		player.setEnterInputListener(Optional.empty());
	}
	
	/**
	 * The enter syntax packet which deals with alphabetical values.
	 * @param player  the player this packet is sent for.
	 * @param payload the payload chained to this packet.
	 */
	private void enterSyntax(Player player, ByteMessage payload) {
		String name = TextUtils.hashToName(payload.getLong());
		if(name.isEmpty()) {
			return;
		}
		player.getEnterInputListener().ifPresent(t -> t.apply(name).execute());
		player.setEnterInputListener(Optional.empty());
	}
}
