package net.edge.net.packet.in;

import net.edge.action.ActionContainer;
import net.edge.action.impl.ItemAction;
import net.edge.content.Attributes;
import net.edge.content.skill.crafting.JewelleryMoulding;
import net.edge.content.skill.runecrafting.Runecrafting;
import net.edge.content.skill.runecrafting.pouch.PouchType;
import net.edge.content.skill.smithing.Smithing;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.ItemDefinition;
import net.edge.world.entity.item.container.session.ExchangeSession;
import net.edge.world.entity.item.container.session.ExchangeSessionManager;
import net.edge.world.entity.item.container.session.ExchangeSessionType;

import java.util.Optional;

/**
 * The message sent from the client when a player operates an item on an
 * interface.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemInterfacePacket implements IncomingPacket {
	
	/**
	 * Events called on item equip action.
	 */
	public static final ActionContainer<ItemAction> EQUIP = new ActionContainer<>();
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_INTERFACE))
			return;
		
		switch(opcode) {
			case 145:
				firstSlot(player, payload);
				break;
			case 117:
				secondSlot(player, payload);
				break;
			case 43:
				thirdSlot(player, payload);
				break;
			case 129:
				fourthSlot(player, payload);
				break;
			case 41:
				equipItem(player, payload);
				break;
			case 214:
				swapSlots(player, payload);
				break;
			case 216:
				bankTab(player, payload);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_INTERFACE);
	}
	
	/**
	 * Handles the first item slot click on an interface.
	 * @param player  the player to handle this for.
	 * @param payload the payload buffer for reading the sent data.
	 */
	private void firstSlot(Player player, IncomingMsg payload) {
		int interfaceId = payload.getShort(ByteTransform.A);
		int slot = payload.getShort(ByteTransform.A);
		int itemId = payload.getShort(ByteTransform.A);
		
		if(interfaceId < 0 || slot < 0 || itemId < 0) {
			return;
		}
		if(interfaceId == 3900) {
			if(player.getMarketShop() != null) {
				player.getMarketShop().purchase(player, new Item(itemId, slot));
			}
		}
		if(interfaceId == 3823) {
			if(player.getMarketShop() != null) {
				player.getMarketShop().sendSellingPrice(player, new Item(itemId));
			}
		}
		if(Attributes.firstSlot(player, interfaceId, slot)) {
			return;
		}
		if(JewelleryMoulding.mould(player, itemId, 1)) {
			return;
		}
		if(Smithing.forge(player, interfaceId, slot, 1)) {
			return;
		}
		Optional<ExchangeSession> session = ExchangeSessionManager.get().getExchangeSession(player);
		switch(interfaceId) {
			case 1688:
				player.getEquipment().unequip(slot);
				player.getCombat().reset(false, false);
				break;
			case 3322:
				if(!session.isPresent()) {
					return;
				}
				if(session.get().getType().equals(ExchangeSessionType.TRADE)) {
					session.get().add(player, slot, 1);
				} else if(session.get().getType().equals(ExchangeSessionType.DUEL)) {
					session.get().add(player, slot, 1);
				}
				break;
			case 3415:
				if(!session.isPresent()) {
					return;
				}
				if(session.get().getType().equals(ExchangeSessionType.TRADE)) {
					session.get().remove(player, new Item(itemId, 1));
				}
				break;
			case 6669:
				if(!session.isPresent()) {
					return;
				}
				if(session.get().getType().equals(ExchangeSessionType.DUEL)) {
					session.get().remove(player, new Item(itemId, 1));
				}
				break;
		}
	}
	
	/**
	 * Handles the second item slot click on an interface.
	 * @param player  the player to handle this for.
	 * @param payload the payload buffer for reading the sent data.
	 */
	private void secondSlot(Player player, IncomingMsg payload) {
		int interfaceId = payload.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		int itemId = payload.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		int slot = payload.getShort(true, ByteOrder.LITTLE);
		if(interfaceId < 0 || slot < 0 || itemId < 0)
			return;
		if(interfaceId == 3823) {
			if(player.getMarketShop() != null) {
				player.getMarketShop().sell(player, new Item(itemId, 1), slot);
			}
		}
		if(Attributes.secondSlot(player, interfaceId, slot)) {
			return;
		}
		if(JewelleryMoulding.mould(player, itemId, 5)) {
			return;
		}
		if(Smithing.forge(player, interfaceId, slot, 5)) {
			return;
		}
		Optional<ExchangeSession> session = ExchangeSessionManager.get().getExchangeSession(player);
		switch(interfaceId) {
			case 3322:
				if(!session.isPresent()) {
					return;
				}
				if(session.get().getType().equals(ExchangeSessionType.TRADE)) {
					session.get().add(player, slot, 5);
				} else if(session.get().getType().equals(ExchangeSessionType.DUEL)) {
					session.get().add(player, slot, 5);
				}
				break;
			case 3415:
				if(!session.isPresent()) {
					return;
				}
				if(session.get().getType().equals(ExchangeSessionType.TRADE)) {
					session.get().remove(player, new Item(itemId, 5));
				}
				break;
			case 6669:
				if(!session.isPresent()) {
					return;
				}
				if(session.get().getType().equals(ExchangeSessionType.DUEL)) {
					session.get().remove(player, new Item(itemId, 5));
				}
				break;
		}
	}
	
	/**
	 * Handles the third item slot click on an interface.
	 * @param player  the player to handle this for.
	 * @param payload the payload buffer for reading the sent data.
	 */
	private void thirdSlot(Player player, IncomingMsg payload) {
		int interfaceId = payload.getShort(ByteOrder.LITTLE);
		int itemId = payload.getShort(ByteTransform.A);
		int slot = payload.getShort(ByteTransform.A);
		if(interfaceId < 0 || slot < 0 || itemId < 0)
			return;
		if(Attributes.thirdSlot(player, interfaceId, slot)) {
			return;
		}
		if(JewelleryMoulding.mould(player, itemId, 10)) {
			return;
		}
		if(Smithing.forge(player, interfaceId, slot, 10)) {
			return;
		}
		Optional<ExchangeSession> session = ExchangeSessionManager.get().getExchangeSession(player);
		switch(interfaceId) {
			case 3823:
				if(player.getMarketShop() != null)
					player.getMarketShop().sell(player, new Item(itemId, 5), slot);
				break;
			case 3322:
				if(!session.isPresent()) {
					return;
				}
				if(session.get().getType().equals(ExchangeSessionType.TRADE)) {
					session.get().add(player, slot, 10);
				} else if(session.get().getType().equals(ExchangeSessionType.DUEL)) {
					session.get().add(player, slot, 10);
				}
				break;
			case 3415:
				if(!session.isPresent()) {
					return;
				}
				if(session.get().getType().equals(ExchangeSessionType.TRADE)) {
					session.get().remove(player, new Item(itemId, 10));
				}
				break;
			case 6669:
				if(!session.isPresent()) {
					return;
				}
				if(session.get().getType().equals(ExchangeSessionType.DUEL)) {
					session.get().remove(player, new Item(itemId, 10));
				}
				break;
		}
	}
	
	/**
	 * Handles the fourth item slot click on an interface.
	 * @param player  the player to handle this for.
	 * @param payload the payload buffer for reading the sent data.
	 */
	private void fourthSlot(Player player, IncomingMsg payload) {
		int slot = payload.getShort(ByteTransform.A);
		int interfaceId = payload.getShort();
		int itemId = payload.getShort(ByteTransform.A);
		if(interfaceId < 0 || slot < 0 || itemId < 0)
			return;
		if(interfaceId == 3823) {
			if(player.getMarketShop() != null) {
				player.getMarketShop().sell(player, new Item(itemId, 10), slot);
			}
		}
		if(Attributes.fourthSlot(player, interfaceId, itemId, slot)) {
			return;
		}
		Optional<ExchangeSession> session = ExchangeSessionManager.get().getExchangeSession(player);
		switch(interfaceId) {
			case 3322:
				if(!session.isPresent()) {
					return;
				}
				if(session.get().getType().equals(ExchangeSessionType.TRADE)) {
					session.get().add(player, slot, player.getInventory().computeAmountForId(itemId));
				} else if(session.get().getType().equals(ExchangeSessionType.DUEL)) {
					session.get().add(player, slot, player.getInventory().computeAmountForId(itemId));
				}
				break;
			case 3415:
				if(!session.isPresent()) {
					return;
				}
				if(session.get().getType().equals(ExchangeSessionType.TRADE)) {
					session.get().remove(player, new Item(itemId, session.get().getExchangeSession().get(player).computeAmountForId(itemId)));
				}
				break;
			case 6669:
				if(!session.isPresent()) {
					return;
				}
				if(session.get().getType().equals(ExchangeSessionType.DUEL)) {
					session.get().remove(player, new Item(itemId, player.getInventory().computeAmountForId(itemId)));
				}
				break;
		}
	}
	
	/**
	 * Handles the equipping of an item for {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payload buffer for reading the sent data.
	 */
	private void equipItem(Player player, IncomingMsg payload) {
		int itemId = payload.getShort(false);
		int slot = payload.getShort(false, ByteTransform.A);
		int interfaceId = payload.getShort(false, ByteTransform.A);
		if(interfaceId < 0 || slot < 0 || itemId < 0 || itemId > ItemDefinition.DEFINITIONS.length)
			return;
		
		Item item = player.getInventory().get(slot);
		if(item == null || !Item.valid(item)) {
			return;
		}
		switch(itemId) {
			case 5509:
				Runecrafting.empty(player, PouchType.SMALL);
				return;
			
			case 5510:
				Runecrafting.empty(player, PouchType.MEDIUM);
				return;
			
			case 5512:
				Runecrafting.empty(player, PouchType.LARGE);
				return;
			
			case 5514:
				Runecrafting.empty(player, PouchType.GIANT);
				return;
		}
		ItemAction e = EQUIP.get(item.getId());
		if(e != null)
			if(e.click(player, item, interfaceId, slot, 5))
				return;
		player.getEquipment().equip(slot);
	}
	
	/**
	 * Handles the swapping of items on an interface for {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payload buffer for reading the sent data.
	 */
	private void swapSlots(Player player, IncomingMsg payload) {
		int interfaceId = payload.getShort(ByteTransform.A, ByteOrder.LITTLE);
		int fromSlot = payload.getShort(ByteTransform.A, ByteOrder.LITTLE);
		int toSlot = payload.getShort(ByteOrder.LITTLE);
		if(interfaceId < 0 || fromSlot < 0 || toSlot < 0)
			return;
		if(interfaceId >= 0 && interfaceId <= 9) {
			if((boolean) player.getAttr().get("insert_item").get()) {
				player.getBank().swap(interfaceId, fromSlot, toSlot);
			} else {
				player.getBank().transfer(interfaceId, fromSlot, toSlot);
			}
		}
		switch(interfaceId) {
			case 3214://inventory
				player.getInventory().swap(fromSlot, toSlot);
				break;
			case 5382://unsure
				break;
			case 5064://banking inventory
				if(player.getAttr().get("banking").getBoolean()) {
					player.getInventory().swap(fromSlot, toSlot);
					player.getInventory().refreshSingle(player, 5064, fromSlot);
					player.getInventory().refreshSingle(player, 5064, toSlot);
				}
				break;
		}
	}
	
	/**
	 * Handles the bank tab switching of items on the bank panel for the {@code player}.
	 * @param player  the player to handle this for.
	 * @param payload the payload buffer for reading the sent data.
	 */
	private void bankTab(Player player, IncomingMsg payload) {
		int tab = payload.getShort(ByteTransform.A, ByteOrder.LITTLE);
		int fromSlot = payload.getShort(ByteTransform.A, ByteOrder.LITTLE);
		int toTab = payload.getShort(ByteTransform.A, ByteOrder.LITTLE);
		if(tab < 0 || fromSlot < 0 || toTab < 0)
			return;
		player.getBank().tabTransfer(tab, fromSlot, toTab);
	}
}
