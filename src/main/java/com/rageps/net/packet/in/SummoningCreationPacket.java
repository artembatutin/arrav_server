//package com.rageps.net.packet.in;
//
//import com.rageps.content.skill.summoning.PouchCreation;
//import com.rageps.content.skill.summoning.SummoningData;
//import com.rageps.net.codec.game.GamePacket;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.world.entity.actor.player.Player;
//
//import static com.rageps.content.skill.summoning.SummoningData.VALUES;
//
///**
// * This message sent from the client when the player clicks a summoning creation button.
// * @author Artem Batutin<artembatutin@gmail.com>
// */
//public final class SummoningCreationPacket implements IncomingPacket {
//
//	@Override
//	public void handle(Player player, int opcode, int size, GamePacket buf) {
//		int click = buf.get();
//		if(click < 0 || click >= VALUES.length)
//			return;
//		SummoningData data = VALUES[click];
//		PouchCreation creation = new PouchCreation(player, data);
//		creation.start();
//	}
//
//}
