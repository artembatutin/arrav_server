package com.rageps.content;

import com.rageps.content.skill.summoning.Summoning;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;
import com.rageps.world.entity.item.container.impl.Bank;

/**
 * The attributes class which holds functionality for actions done
 * while the attribute is active.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Attributes {
	
	/**
	 * The first slot action from the {@link ItemInterfacePacket} packet, any attribute
	 * action should be utilised here.
	 * @param player the player we're utilizing this action for.
	 * @param interfaceId the interface id of this interface.
	 * @param slot the item slot.
	 * @return <true> if the action was completed, <false> otherwise.
	 */
	public static boolean firstSlot(Player player, int interfaceId, int slot) {
		switch(interfaceId) {

			case Bank.BANK_INVENTORY_ID:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING))
					player.getBank().withdraw(player, 0, slot, 1);
				break;

			case Bank.SIDEBAR_INVENTORY_ID:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING))
					player.getBank().deposit(slot, 1, player.getInventory(), true);
				return true;

			case 5064:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BOB))
					Summoning.store(player, slot, 1);
				return true;
			case 2702:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BOB)) {
					Summoning.withdraw(player, slot, 1);
				}
				return true;
		}
		return false;
	}
	
	/**
	 * The second slot action from the {@link ItemInterfacePacket} packet, any attribute
	 * action should be utilised here.
	 * @param player the player we're utilizing this action for.
	 * @param interfaceId the interface id of this interface.
	 * @param slot the item slot.
	 * @return <true> if the action was completed, <false> otherwise.
	 */
	public static boolean secondSlot(Player player, int interfaceId, int slot) {

		switch(interfaceId) {
			case Bank.BANK_INVENTORY_ID:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING))
					player.getBank().withdraw(player, 0, slot, 5);
				break;
			case Bank.SIDEBAR_INVENTORY_ID:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING))
					player.getBank().deposit(slot, 5, player.getInventory(), true);
				return true;

			case 5064:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BOB))
					Summoning.store(player, slot, 5);
				return true;
			case 2702:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BOB)) {
					Summoning.withdraw(player, slot, 5);
				}
				return true;
		}
		return false;
	}
	
	/**
	 * The third slot action from the {@link ItemInterfacePacket} packet, any attribute
	 * action should be utilised here.
	 * @param player the player we're utilizing this action for.
	 * @param interfaceId the interface id of this interface.
	 * @param slot the item slot.
	 * @return <true> if the action was completed, <false> otherwise.
	 */
	public static boolean thirdSlot(Player player, int interfaceId, int slot) {

		switch(interfaceId) {

			case Bank.BANK_INVENTORY_ID:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING))
					player.getBank().withdraw(player, 0, slot, 10);
				break;

			case Bank.SIDEBAR_INVENTORY_ID:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING))
					player.getBank().deposit(slot, 10, player.getInventory(), true);
				return true;
			case 5064:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BOB))
					Summoning.store(player, slot, 10);
				return true;
			case 2702:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BOB)) {
					Summoning.withdraw(player, slot, 10);
				}
				return true;
		}
		return false;
	}
	
	/**
	 * The fourth slot action from the {@link ItemInterfacePacket} packet, any attribute
	 * action should be utilised here.
	 * @param player the player we're utilizing this action for.
	 * @param interfaceId the interface id of this interface.
	 * @param itemId the item id.
	 * @param slot the item slot.
	 * @return <true> if the action was completed, <false> otherwise.
	 */
	public static boolean fourthSlot(Player player, int interfaceId, int itemId, int slot) {

		switch(interfaceId) {

			case Bank.BANK_INVENTORY_ID:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING)) {
					int amount;
					if(player.getAttributeMap().getBoolean(PlayerAttributes.WITHDRAW_AS_NOTE)) {
						amount = player.getBank().amount(itemId);
					} else {
						Item itemWithdrew = new Item(itemId, 1);
						amount = ItemDefinition.DEFINITIONS[itemWithdrew.getId()].isStackable() ? player.getBank().amount(itemId) : 28;
					}
					player.getBank().withdraw(player, 0, slot, amount);//bank tab (should be interfaceid)
				}
				break;

			case 5064:
			case Bank.SIDEBAR_INVENTORY_ID:
				Item inv = player.getInventory().get(slot);
				if(inv == null) {
					return false;
				}
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING)) {
					player.getBank().deposit(slot, player.getInventory().computeAmountForId(inv.getId()), player.getInventory(), true);
				} else if(player.getAttributeMap().getBoolean(PlayerAttributes.BOB)) {
					Summoning.store(player, slot, player.getInventory().computeAmountForId(inv.getId()));
				}
				return true;
			case 2702:
				if(player.getAttributeMap().getBoolean(PlayerAttributes.BOB)) {
					Summoning.withdraw(player, slot, -1);
				}
				return true;
		}
		return false;
	}
	
}