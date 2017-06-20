package net.edge.net.packet.impl;

import net.edge.content.skill.construction.furniture.ConstructFurniture;
import net.edge.content.skill.construction.furniture.Furniture;
import net.edge.content.skill.construction.room.RoomData;
import net.edge.content.skill.construction.room.RoomManipulation;
import net.edge.content.skill.summoning.PouchCreation;
import net.edge.net.codec.ByteMessage;
import net.edge.net.packet.PacketReader;
import net.edge.world.node.entity.player.Player;

import static net.edge.content.skill.construction.room.RoomData.*;

/**
 * This message sent from the client when the player clicks a construction panel button.
 * @author Artem Batutin<artembatutin@gmail.com>
 */
public final class ConstructionPacket implements PacketReader {
	
	private static final RoomData[] DATA = {
			GARDEN,
			PARLOUR,
			KITCHEN,
			DINING_ROOM,
			WORKSHOP,
			BEDROOM,
			SKILL_ROOM,
			QUEST_HALL_DOWN,
			SKILL_HALL_DOWN,
			GAMES_ROOM,
			COMBAT_ROOM,
			QUEST_ROOM,
			MENAGERY,
			STUDY,
			CUSTOME_ROOM,
			CHAPEL,
			PORTAL_ROOM,
			FORMAL_GARDEN,
			THRONE_ROOM,
			OUBLIETTE,
			PIT,
			DUNGEON_STAIR_ROOM,
			CORRIDOR,
			JUNCTION,
			TREASURE_ROOM,
	};
	
	@Override
	public void handle(Player player, int opcode, int size, ByteMessage payload) {
		int click = payload.get();
		if(click < 0 || click >= DATA.length) {
			click -= 40;
			Furniture[] plans = player.getHouse().get().getPlan().getPanel();
			if(plans == null)
				return;
			player.getHouse().get().getPlan().setSelected(plans[click]);
			ConstructFurniture cons = new ConstructFurniture(player, player.getHouse().get().getPlan());
			cons.start();
		} else {
			RoomData data = ConstructionPacket.DATA[click];
			RoomManipulation.createRoom(data, player, player.getPosition().getZ());
		}
	}
	
}
