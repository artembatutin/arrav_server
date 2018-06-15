package net.arrav.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.arrav.Arrav;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.item.Item;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 26-8-2017.
 */
public class OperateEquipmentPacket implements IncomingPacket {
	
	/**
	 * Handles the message designated to {@code opcode}.
	 * @param player the player this message is being handled for.
	 * @param opcode the opcode of this message.
	 * @param size the size of this message.
	 * @param buf the data contained within this message.
	 */
	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
		int option = buf.getShort(ByteTransform.A);
		int slot = buf.getShort(ByteTransform.A);
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
				if(player.getRights().greater(Rights.ADMINISTRATOR) && Arrav.DEBUG) {
					player.message("Operate Item - itemId: " + slot + " amount: " + item.getAmount() + " slot: ");
				}
				break;
		}
	}
}
