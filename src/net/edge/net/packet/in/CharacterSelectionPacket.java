package net.edge.net.packet.in;

import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.PlayerAppearance;
import net.edge.world.node.actor.player.assets.activity.ActivityManager.ActivityType;
import net.edge.world.node.actor.update.UpdateFlag;

/**
 * The message sent from the client when the character clicks "accept" on the
 * character selection interface.
 * @author lare96 <http://github.com/lare96>
 */
public final class CharacterSelectionPacket implements IncomingPacket {
	
	/**
	 * The valid colors for the character selection message.
	 */
	private static final int[][] VALID_COLORS = {
			{0, 11}, // hair color
			{0, 15}, // torso color
			{0, 15}, // legs color
			{0, 5}, // feet color
			{0, 7} // skin color
	};
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityType.CHARACTER_SELECTION))
			return;
		int cursor = 0;
		int[] values = new int[13];
		int gender = payload.get();
		if(gender != PlayerAppearance.GENDER_FEMALE && gender != PlayerAppearance.GENDER_MALE)
			return;
		values[cursor++] = gender;
		for(int i = 0; i < 7; i++) {
			int value = payload.getShort();
			values[cursor++] = value;
		}
		for(int[] VALID_COLOR : VALID_COLORS) {
			int value = payload.getShort();
			if((value < VALID_COLOR[0]) || (value > VALID_COLOR[1])) {
				return;
			}
			values[cursor++] = value;
		}
		player.getAppearance().setValues(values);
		player.getFlags().flag(UpdateFlag.APPEARANCE);
		player.closeWidget();
		if(player.getAttr().get("introduction_stage").getInt() == 0) {
			player.getAttr().get("introduction_stage").set(1);
		}
		player.getActivityManager().execute(ActivityType.CHARACTER_SELECTION);
	}
	
}
