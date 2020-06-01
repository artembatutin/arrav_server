package com.rageps.net.packet.out;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;

public final class SendCameraShake implements OutgoingPacket {
	
	private final int parameter, jitter, amplitude, frequency;
	
	public SendCameraShake(int parameter, int jitter, int amplitude, int frequency) {
		Preconditions.checkArgument(parameter <= 4);
		this.parameter = parameter;
		this.jitter = jitter;
		this.amplitude = amplitude;
		this.frequency = frequency;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(35);
		out.put(parameter);
		out.put(jitter);
		out.put(amplitude);
		out.put(frequency);
		return out;
	}
}
