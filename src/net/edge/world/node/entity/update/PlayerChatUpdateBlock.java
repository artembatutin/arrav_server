package net.edge.world.node.entity.update;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.world.node.entity.player.Player;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the chat update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class PlayerChatUpdateBlock extends PlayerUpdateBlock {
	
	/**
	 * Creates a new {@link PlayerChatUpdateBlock}.
	 */
	public PlayerChatUpdateBlock() {
		super(0x80, UpdateFlag.CHAT);
	}
	
	@Override
	public int write(Player player, Player mob, ByteMessage msg) {
		msg.putShort(((mob.getChatColor() & 0xff) << 8) + (mob.getChatEffects() & 0xff), ByteOrder.LITTLE);
		msg.put(mob.getRights().getProtocolValue());
		msg.put(mob.getChatText().length, ByteTransform.C);
		msg.putBytesReverse(mob.getChatText());
		return -1;
	}
}
