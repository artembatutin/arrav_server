package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.OperateEquipmentPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.item.Item;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class OperateEquipmentPacketPacketHandler implements PacketHandler<OperateEquipmentPacketPacket> {

    @Override
    public void handle(Player player, OperateEquipmentPacketPacket packet) {

        int slot = packet.getSlot();
        int option = packet.getOption();

        Item item = player.getEquipment().get(slot);

        if(item == null || Item.valid(item)) {
            return;
        }

        switch(item.getId()) {
            case 2552:
            case 2554:
            case 2556:
            case 2558:
            case 2560:
            case 2562:
            case 2564:
            case 2566: //Ring of dueling
                if(option == 1) {
                    //TODO: TO DUEL ARENA
                }
                if(option == 2) {
                    //TODO: TO CASTLE WARS
                }
                if(option == 3) {
                    //TODO: TO CLAN WARS
                }
                break;
            case 11283:
                //TODO: TO DRAGON FIRE EXECUTE
                break;
            case 1706:
            case 1708:
            case 1710:
            case 1712:
            case 10362:
                if(option == 1) {
                    //TODO: TO EDGEVILLE
                }
                if(option == 2) {
                    //TODO: TO KARAMJA
                }
                if(option == 3) {
                    //TODO: TO DRAYNOR
                }
                if(option == 4) {
                    //TODO: TO AL-HARID
                }
                break;
            default:
                if(player.getRights().greater(Rights.ADMINISTRATOR) && World.get().getEnvironment().isDebug()) {
                    player.message("Operate Item - itemId: " + slot + " amount: " + item.getAmount() + " slot: ");
                }
                break;
        }
    }
}
