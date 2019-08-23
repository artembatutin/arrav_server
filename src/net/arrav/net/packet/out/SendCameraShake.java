package net.arrav.net.packet.out;

import com.google.common.base.Preconditions;

import net.arrav.net.codec.game.GamePacket;
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
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(35);
		out.put(parameter);
		out.put(jitter);
		out.put(amplitude);
		out.put(frequency);
		return out;
	}
}
