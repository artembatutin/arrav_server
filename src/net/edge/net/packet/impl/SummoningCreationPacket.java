package net.edge.net.packet.impl;

import net.edge.content.skill.summoning.PouchCreation;
import net.edge.content.skill.summoning.SummoningData;
import net.edge.net.codec.ByteMessage;
import net.edge.net.packet.PacketReader;
import net.edge.world.node.entity.player.Player;

import static net.edge.content.skill.summoning.SummoningData.VALUES;

/**
 * This message sent from the client when the player clicks a summoning creation button.
 * @author Artem Batutin<artembatutin@gmail.com>
 */
public final class SummoningCreationPacket implements PacketReader {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteMessage payload) {
		int click = payload.get();
		System.out.println(click);
		if(click < 0 || click >= VALUES.length)
			return;
		SummoningData data = VALUES[click];
		PouchCreation creation = new PouchCreation(player, data);
		creation.start();
	}
	
}
