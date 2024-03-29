package net.arrav.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.PlayerAppearance;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager.ActivityType;
import net.arrav.world.entity.actor.update.UpdateFlag;

import static net.arrav.content.achievements.Achievement.CHANGE_APPEARANCE;

/**
 * The message sent from the client when the character clicks "accept" on the
 * character selection interface.
 * @author lare96 <http://github.com/lare96>
 */
public final class CharacterSelectionPacket implements IncomingPacket {
	
	/**
	 * The valid colors for the character selection message.
	 */
	private static final int[][] VALID_COLORS = {{0, 11}, // hair color
			{0, 15}, // torso color
			{0, 15}, // legs color
			{0, 5}, // feet color
			{0, 7} // skin color
	};
	
	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
		if(player.getActivityManager().contains(ActivityType.CHARACTER_SELECTION))
			return;
		int cursor = 0;
		int[] values = new int[13];
		int gender = buf.get();
		if(gender != PlayerAppearance.GENDER_FEMALE && gender != PlayerAppearance.GENDER_MALE)
			return;
		values[cursor++] = gender;
		for(int i = 0; i < 7; i++) {
			int value = buf.getShort();
			values[cursor++] = value;
		}
		for(int[] VALID_COLOR : VALID_COLORS) {
			int value = buf.getShort();
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
		CHANGE_APPEARANCE.inc(player);
	}
	
}
