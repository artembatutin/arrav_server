package net.arrav.net.packet.out;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

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
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(35);
		buf.put(parameter);
		buf.put(jitter);
		buf.put(amplitude);
		buf.put(frequency);
		return buf;
	}
}
