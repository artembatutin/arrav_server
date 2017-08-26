package net.edge.net.packet.in;

import net.edge.Application;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.item.Item;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 26-8-2017.
 */
public class OperateEquipmentPacket implements IncomingPacket {

    /**
     * Handles the message designated to {@code opcode}.
     * @param player  the player this message is being handled for.
     * @param opcode  the opcode of this message.
     * @param size    the size of this message.
     * @param payload the data contained within this message.
     */
    @Override
    public void handle(Player player, int opcode, int size, IncomingMsg payload) {
        int option = payload.getShort(ByteTransform.A);
        int slot = payload.getShort(ByteTransform.A);
        Item item = player.getEquipment().get(slot);

        System.out.println("slot = " + slot + " option = " + option);
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
                    System.out.println(" to Duel Arena");
                }
                if(option == 2) {
                    System.out.println(" to Castle Wars");
                }
                if(option == 3) {
                    System.out.println(" to Clan Wars");
                }
                break;
            case 11283:
                System.out.println("Dragonfire execute");
                break;
            case 1706:
            case 1708:
            case 1710:
            case 1712:
            case 10362:
                if(option == 1) {
                    System.out.println(" to Edgeville");
                }
                if(option == 2) {
                    System.out.println(" to Karamja");
                }
                if(option == 3) {
                    System.out.println(" to Draynor Village");
                }
                if(option == 4) {
                    System.out.println(" to Al-Kharid");
                }
                break;
            default:
                if(player.getRights().greater(Rights.ADMINISTRATOR) && Application.DEBUG) {
                    player.message("Operate Item - itemId: " + slot + " amount: " + item.getAmount() + " slot: ");
                }
                break;
        }
    }
}
