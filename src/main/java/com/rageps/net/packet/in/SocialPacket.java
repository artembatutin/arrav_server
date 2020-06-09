package com.rageps.net.packet.in;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.util.ChatCodec;
import com.rageps.util.StringUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.actor.player.assets.relations.PrivateChatMessage;

import java.util.Optional;

/**
 * The message sent from the client when a player adds, removes, or sends someone
 * a message.
 * @author lare96 <http://github.com/lare96>
 */
public final class SocialPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.PRIVATE_MESSAGE))
			return;
		
		switch(opcode) {
			case 188:
				addFriend(player, buf);
				break;
			case 215:
				removeFriend(player, buf);
				break;
			case 133:
				addIgnore(player, buf);
				break;
			case 74:
				removeIgnore(player, buf);
				break;
			case 126:
				sendMessage(player, size, buf);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.PRIVATE_MESSAGE);
	}
	
	/**
	 * Handles the adding of a new friend.
	 * @param player the player to handle this for.
	 * @param buf the payload buffer used for reading sent data.
	 */
	private void addFriend(Player player, GamePacket buf) {
		long name = buf.getLong();
		if(name < 0)
			return;
		player.relations.addFriend(name);
	}
	
	/**
	 * Handles the removing of an existing friend.
	 * @param player the player to handle this for.
	 * @param buf the payload buffer used for reading sent data.
	 */
	private void removeFriend(Player player, GamePacket buf) {
		long name = buf.getLong();
		if(name < 0)
			return;
		player.relations.deleteFriend(name);
	}
	
	/**
	 * Handles the adding of a new ignore.
	 * @param player the player to handle this for.
	 * @param buf the payload buffer used for reading sent data.
	 */
	private void addIgnore(Player player, GamePacket buf) {
		long name = buf.getLong();
		if(name < 0)
			return;
		player.relations.addIgnore(name);
	}
	
	/**
	 * Handles the removing of an existing ignore.
	 * @param player the player to handle this for.
	 * @param buf the payload buffer used for reading sent data.
	 */
	private void removeIgnore(Player player, GamePacket buf) {
		long name = buf.getLong();
		if(name < 0)
			return;
		player.relations.deleteIgnore(name);
	}
	
	/**
	 * Handles the sending of a private message.
	 * @param player the player to handle this for.
	 * @param buf the payload buffer used for reading sent data.
	 */
	private void sendMessage(Player player, int size, GamePacket buf) {
		long to = buf.getLong();

		final Optional<Player> result = World.get().search(StringUtil.formatText(StringUtil.longToString(to)).replace("_", " "));

		if (!result.isPresent()) {
			return;
		}

		final Player other = result.get();

		final byte[] input = buf.getBytes(size - Long.BYTES);
		final String decoded = ChatCodec.decode(input);
		final byte[] compressed = ChatCodec.encode(decoded);
		player.relations.message(other, new PrivateChatMessage(decoded, compressed));
	}
}
