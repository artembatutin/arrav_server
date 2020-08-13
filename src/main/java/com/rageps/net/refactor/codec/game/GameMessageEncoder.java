package com.rageps.net.refactor.codec.game;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import com.rageps.net.refactor.packet.Packet;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.release.Release;

import java.util.List;

/**
 * A {@link MessageToMessageEncoder} which encodes {@link Packet}s into {@link GamePacket}s.
 *
 * @author Graham
 */
public final class GameMessageEncoder extends MessageToMessageEncoder<Packet> {

	/**
	 * The current release.
	 */
	private final Release release;

	/**
	 * Creates the game message encoder with the specified release.
	 *
	 * @param release The release.
	 */
	public GameMessageEncoder(Release release) {
		this.release = release;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) {
		PacketEncoder<Packet> encoder = (PacketEncoder<Packet>) release.getMessageEncoder(packet.getClass());
		if (encoder != null) {
			out.add(encoder.encode(packet));
		}
	}

}