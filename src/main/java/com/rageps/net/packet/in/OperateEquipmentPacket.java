package com.rageps.net.packet.in;

import com.rageps.RagePS;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.item.Item;

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
	public void handle(Player player, int opcode, int size, GamePacket buf) {
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
				if(player.getRights().greater(Rights.ADMINISTRATOR) && World.get().getEnvironment().isDebug()) {
					player.message("Operate Item - itemId: " + slot + " amount: " + item.getAmount() + " slot: ");
				}
				break;
		}
	}
}
