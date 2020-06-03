package com.rageps.net.packet.in;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAppearance;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager.ActivityType;
import com.rageps.world.entity.actor.update.UpdateFlag;

import static com.rageps.content.achievements.Achievement.CHANGE_APPEARANCE;

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
	public void handle(Player player, int opcode, int size, GamePacket buf) {
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
		if(player.getAttributeMap().getInt(PlayerAttributes.INTRODUCTION_STAGE) == 0) {
			player.getAttributeMap().set(PlayerAttributes.INTRODUCTION_STAGE, 1);
		}
		player.getActivityManager().execute(ActivityType.CHARACTER_SELECTION);
		CHANGE_APPEARANCE.inc(player);
	}
	
}
