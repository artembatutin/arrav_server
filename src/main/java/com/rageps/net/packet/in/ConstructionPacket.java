//package com.rageps.net.packet.in;
//
//import com.rageps.content.skill.construction.furniture.ConstructFurniture;
//import com.rageps.content.skill.construction.furniture.Furniture;
//import com.rageps.content.skill.construction.room.RoomData;
//import com.rageps.content.skill.construction.room.RoomManipulation;
//import com.rageps.net.codec.game.GamePacket;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.net.refactor.packet.in.handler.ConstructionPacketPacketHandler;
//import com.rageps.world.entity.actor.player.Player;
//
//import static com.rageps.content.skill.construction.room.RoomData.*;
//
///**
// * This message sent from the client when the player clicks a construction panel button.
// * @author Artem Batutin<artembatutin@gmail.com>
// */
//public final class ConstructionPacket implements IncomingPacket {
//
//	private static final RoomData[] DATA = {GARDEN, PARLOUR, KITCHEN, DINING_ROOM, WORKSHOP, BEDROOM, SKILL_ROOM, QUEST_HALL_DOWN, SKILL_HALL_DOWN, GAMES_ROOM, COMBAT_ROOM, QUEST_ROOM, MENAGERY, STUDY, CUSTOME_ROOM, CHAPEL, PORTAL_ROOM, FORMAL_GARDEN, THRONE_ROOM, OUBLIETTE, PIT, DUNGEON_STAIR_ROOM, CORRIDOR, JUNCTION, TREASURE_ROOM,};
//
//	@Override
//	public void handle(Player player, int opcode, int size, GamePacket buf) {
//		int click = buf.get();
//		if(click < 0 || click >= DATA.length) {
//			click -= 40;
//			Furniture[] plans = player.getHouse().get().getPlan().getPanel();
//			if(plans == null)
//				return;
//			player.getHouse().get().getPlan().setSelected(plans[click]);
//			ConstructFurniture cons = new ConstructFurniture(player, player.getHouse().get().getPlan());
//			cons.start();
//		} else {
//			RoomData data = ConstructionPacketPacketHandler.DATA[click];
//			RoomManipulation.createRoom(data, player, player.getPosition().getZ());
//		}
//	}
//
//}
