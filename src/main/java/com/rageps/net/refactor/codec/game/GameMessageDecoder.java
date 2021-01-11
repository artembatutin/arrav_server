package com.rageps.net.refactor.codec.game;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.release.Release;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * A {@link MessageToMessageDecoder} that decodes {@link GamePacket}s into {@link Packet}s.
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
		PacketDecoder<?> decoder = release.getMessageDecoder(packet.getOpcode());
		if (decoder != null) {
			out.add(decoder.decode(packet));
		} else {
			System.out.println("Unidentified packet received - opcode: " + packet.getOpcode() + ".");
		}
	}

}