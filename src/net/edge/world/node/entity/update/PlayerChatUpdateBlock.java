package net.edge.world.node.entity.update;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.world.node.entity.player.Player;

import static net.edge.world.node.entity.player.assets.Rights.IRON_MAN;
import static net.edge.world.node.entity.player.assets.Rights.PLAYER;

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
	public int write(Player player, Player other, GameBuffer msg) {
		msg.putShort(((other.getChatColor() & 0xff) << 8) + (other.getChatEffects() & 0xff), ByteOrder.LITTLE);
		msg.put(other.getRights() == PLAYER && player.isIronMan() ? IRON_MAN.getProtocolValue() : other.getRights().getProtocolValue());
		msg.put(other.getChatText().length, ByteTransform.C);
		msg.putBytesReverse(other.getChatText());
		return -1;
	}
}
