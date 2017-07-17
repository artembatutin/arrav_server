package net.edge.net.packet.in;

import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.util.TextUtils;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;

import java.util.Optional;

public final class EnterInputPacket implements IncomingPacket {
	
	public static final int ENTER_AMOUNT_OPCODE = 208, ENTER_SYNTAX_OPCODE = 60;
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
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
	private void enterAmount(Player player, IncomingMsg payload) {
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
	private void enterSyntax(Player player, IncomingMsg payload) {
		String name = TextUtils.hashToName(payload.getLong());
		if(name.isEmpty()) {
			return;
		}
		player.getEnterInputListener().ifPresent(t -> t.apply(name).execute());
		player.setEnterInputListener(Optional.empty());
	}
}
