package net.arrav.content;

import net.arrav.content.skill.summoning.Summoning;
import net.arrav.net.packet.in.ItemInterfacePacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.ItemDefinition;

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
		if(interfaceId >= 0 && interfaceId <= 9 && player.getAttr().get("banking").getBoolean()) {
			player.getBank().withdraw(player, interfaceId, slot, 1);
			return true;
		}
		switch(interfaceId) {
			case 5064:
				if(player.getAttr().get("banking").getBoolean()) {
					player.getBank().deposit(slot, 1, player.getInventory(), true);
				} else if(player.getAttr().get("bob").getBoolean()) {
					Summoning.store(player, slot, 1);
				}
				return true;
			case 2702:
				if(player.getAttr().get("bob").getBoolean()) {
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
		if(interfaceId >= 0 && interfaceId <= 9 && player.getAttr().get("banking").getBoolean()) {
			player.getBank().withdraw(player, interfaceId, slot, 5);
			return true;
		}
		switch(interfaceId) {
			case 5064:
				if(player.getAttr().get("banking").getBoolean()) {
					player.getBank().deposit(slot, 5, player.getInventory(), true);
				} else if(player.getAttr().get("bob").getBoolean()) {
					Summoning.store(player, slot, 5);
				}
				return true;
			case 2702:
				if(player.getAttr().get("bob").getBoolean()) {
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
		if(interfaceId >= 0 && interfaceId <= 9 && player.getAttr().get("banking").getBoolean()) {
			player.getBank().withdraw(player, interfaceId, slot, 10);
			return true;
		}
		switch(interfaceId) {
			case 5064:
				if(player.getAttr().get("banking").getBoolean()) {
					player.getBank().deposit(slot, 10, player.getInventory(), true);
				} else if((Boolean) player.getAttr().get("bob").get()) {
					Summoning.store(player, slot, 10);
				}
				return true;
			case 2702:
				if(player.getAttr().get("bob").getBoolean()) {
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
		if(interfaceId >= 0 && interfaceId <= 9 && player.getAttr().get("banking").getBoolean()) {
			int amount;
			if(player.getAttr().get("withdraw_as_note").getBoolean()) {
				amount = player.getBank().amount(itemId);
			} else {
				Item itemWithdrew = new Item(itemId, 1);
				amount = ItemDefinition.DEFINITIONS[itemWithdrew.getId()].isStackable() ? player.getBank().amount(itemId) : 28;
			}
			player.getBank().withdraw(player, interfaceId, slot, amount);
			return true;
		}
		switch(interfaceId) {
			case 5064:
				Item inv = player.getInventory().get(slot);
				if(inv == null) {
					return false;
				}
				if(player.getAttr().get("banking").getBoolean()) {
					player.getBank().deposit(slot, player.getInventory().computeAmountForId(inv.getId()), player.getInventory(), true);
				} else if(player.getAttr().get("bob").getBoolean()) {
					Summoning.store(player, slot, player.getInventory().computeAmountForId(inv.getId()));
				}
				return true;
			case 2702:
				if(player.getAttr().get("bob").getBoolean()) {
					Summoning.withdraw(player, slot, -1);
				}
				return true;
		}
		return false;
	}
	
}