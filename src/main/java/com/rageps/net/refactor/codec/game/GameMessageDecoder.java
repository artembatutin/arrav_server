package com.rageps.net.refactor.codec.game;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import com.rageps.net.refactor.message.Message;
import com.rageps.net.refactor.release.MessageDecoder;
import com.rageps.net.refactor.release.Release;

import java.util.List;

/**
 * A {@link MessageToMessageDecoder} that decodes {@link GamePacket}s into {@link Message}s.
 *
 * @author Graham
 */
public final class GameMessageDecoder extends MessageToMessageDecoder<GamePacket> {

	/**
	 * The current release.
	 */
	private final Release release;

	/**
	 * Creates the game message decoder with the specified release.
	 *
	 * @param release The release.
	 */
	public GameMessageDecoder(Release release) {
		this.release = release;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, GamePacket packet, List<Object> out) {
		MessageDecoder<?> decoder = release.getMessageDecoder(packet.getOpcode());
		if (decoder != null) {
			out.add(decoder.decode(packet));
		} else {
			System.out.println("Unidentified packet received - opcode: " + packet.getOpcode() + ".");
		}
	}

}