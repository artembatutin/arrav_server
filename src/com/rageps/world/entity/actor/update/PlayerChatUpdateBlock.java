package com.rageps.world.entity.actor.update;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.player.Player;

import static com.rageps.world.entity.actor.player.assets.Rights.IRON_MAN;
import static com.rageps.world.entity.actor.player.assets.Rights.PLAYER;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the chat update block.
 * @author Artem Batutin
 */
public final class PlayerChatUpdateBlock extends PlayerUpdateBlock {
	
	/**
	 * Creates a new {@link PlayerChatUpdateBlock}.
	 */
	public PlayerChatUpdateBlock() {
		super(0x80, UpdateFlag.CHAT);
	}
	
	@Override
	public int write(Player player, Player other, GamePacket buf) {
		buf.putShort(((other.getChatColor() & 0xff) << 8) + (other.getChatEffects() & 0xff), ByteOrder.LITTLE);
		buf.put(other.getRights() == PLAYER && other.isIronMan() ? IRON_MAN.getProtocolValue() : other.getRights().getProtocolValue());
		buf.put(other.getChatText().length, ByteTransform.C);
		buf.putBytesReverse(other.getChatText());
		return -1;
	}
}
