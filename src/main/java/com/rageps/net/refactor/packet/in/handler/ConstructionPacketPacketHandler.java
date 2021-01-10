package com.rageps.net.refactor.packet.in.handler;

import com.rageps.content.skill.construction.furniture.ConstructFurniture;
import com.rageps.content.skill.construction.furniture.Furniture;
import com.rageps.content.skill.construction.room.RoomData;
import com.rageps.content.skill.construction.room.RoomManipulation;
import com.rageps.net.refactor.packet.in.model.ConstructionPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

import static com.rageps.content.skill.construction.room.RoomData.*;

/**
 * This message sent from the client when the player clicks a construction panel button.
 * @author Tamatea <tamateea@gmail.com>
 */
public class ConstructionPacketPacketHandler implements PacketHandler<ConstructionPacketPacket> {

    public static final RoomData[] DATA = {GARDEN, PARLOUR, KITCHEN, DINING_ROOM, WORKSHOP, BEDROOM, SKILL_ROOM, QUEST_HALL_DOWN, SKILL_HALL_DOWN, GAMES_ROOM, COMBAT_ROOM, QUEST_ROOM, MENAGERY, STUDY, CUSTOME_ROOM, CHAPEL, PORTAL_ROOM, FORMAL_GARDEN, THRONE_ROOM, OUBLIETTE, PIT, DUNGEON_STAIR_ROOM, CORRIDOR, JUNCTION, TREASURE_ROOM,};


    @Override
    public void handle(Player player, ConstructionPacketPacket packet) {
        int click = packet.getClick();
        if(click < 0 || click >= DATA.length) {
            click -= 40;
            Furniture[] plans = player.getHouse().get().getPlan().getPanel();
            if(plans == null)
                return;
            player.getHouse().get().getPlan().setSelected(plans[click]);
            ConstructFurniture cons = new ConstructFurniture(player, player.getHouse().get().getPlan());
            cons.start();
        } else {
            RoomData data = DATA[click];
            RoomManipulation.createRoom(data, player, player.getPosition().getZ());
        }
    }
}
