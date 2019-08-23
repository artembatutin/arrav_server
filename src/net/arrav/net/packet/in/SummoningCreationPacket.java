package net.arrav.net.packet.in;

import net.arrav.content.skill.summoning.PouchCreation;
import net.arrav.content.skill.summoning.SummoningData;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.entity.actor.player.Player;

import static net.arrav.content.skill.summoning.SummoningData.VALUES;

/**
 * This message sent from the client when the player clicks a summoning creation button.
 * @author Artem Batutin<artembatutin@gmail.com>
 */
public final class SummoningCreationPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		int click = buf.get();
		if(click < 0 || click >= VALUES.length)
			return;
		SummoningData data = VALUES[click];
		PouchCreation creation = new PouchCreation(player, data);
		creation.start();
	}
	
}
