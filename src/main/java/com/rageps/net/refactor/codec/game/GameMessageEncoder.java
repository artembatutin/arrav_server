package com.rageps.net.refactor.codec.game;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.release.Release;
import com.rageps.world.World;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

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
			if(encoder.coordinatePacket(packet) != null)
				out.add(encoder.coordinatePacket(packet));
			if(encoder.onSent(packet))
				out.add(encoder.encode(packet));
		} else {
			World.getLogger().warn("{} has no encoder!", packet.getClass().getSimpleName());
		}
	}

}