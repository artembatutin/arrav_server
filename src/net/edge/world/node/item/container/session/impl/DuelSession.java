package net.edge.world.node.item.container.session.impl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.util.Stopwatch;
import net.edge.world.node.item.container.session.ExchangeSession;
import net.edge.world.node.item.container.session.ExchangeSessionActionType;
import net.edge.world.node.item.container.session.ExchangeSessionType;
import net.edge.content.minigame.dueling.DuelMinigame;
import net.edge.content.minigame.dueling.DuelingRules;
import net.edge.locale.loc.Location;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Holds functionality for the exchange session of the dueling minigame.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DuelSession extends ExchangeSession {
	
	/**
	 * The last time in milliseconds a player modified a rule.
	 */
	private Stopwatch lastRuleModification = new Stopwatch();
	
	/**
	 * The set which holds all the rules a duel session can have.
	 */
	private final EnumSet<DuelingRules> rules = EnumSet.noneOf(DuelingRules.class);
	
	/**
	 * Constructs a new {@link DuelSession}.
	 * @param player the player that controls this duel session.
	 * @param other  the player that was invited to this duel session.
	 * @param stage  the stage of this duel session.
	 */
	public DuelSession(Player player, Player other, int stage) {
		super(Arrays.asList(player, other), stage, ExchangeSessionType.DUEL);
	}
	
	@Override
	public void onRequest(Player player, Player requested) {
		if(!Location.inDuelArena(player)) {
			player.message("You must be in the duel arena area to do this.");
			return;
		}
		if(!Location.inDuelArena(requested)) {
			player.message("The challenger must be in the duel arena area to do this.");
			return;
		}
		
		DuelSession session = (DuelSession) World.getExchangeSessionManager().isAvailable(player, requested, ExchangeSessionType.DUEL).orElse(null);
		
		if(session != null) {
			session.setStage(OFFER_ITEMS);
			session.updateMainComponents();
			this.getPlayers().forEach(World.getExchangeSessionManager()::resetRequests);
			session.setAttachment(null);
		} else {
			session = new DuelSession(player, requested, ExchangeSession.REQUEST);
			player.message("Sending duel request...");
			requested.message(player.getFormatUsername() + ":duelreq:");
			session.setAttachment(player);
			World.getExchangeSessionManager().add(session);
		}
	}
	
	@Override
	public void onClickButton(Player player, int button) {
		if(toggleRule(player, this, button)) {
			return;
		}
		
		switch(button) {
			case 148022:
				this.accept(player, OFFER_ITEMS);
				return;
			case 25120:
				this.accept(player, CONFIRM_DECISION);
				return;
			case 148025:
				World.getExchangeSessionManager().reset(player, ExchangeSessionType.DUEL);
				return;
		}
	}
	
	/**
	 * Attempts to toggle a rule for the specified {@code player} and the specified {@code session}
	 * the player is in.
	 * @param player   the player whom is toggling the rule.
	 * @param session  the session the rule is being toggled for.
	 * @param buttonId the button identification to check for.
	 * @return <true> if the rule was toggled, <false> otherwise.
	 */
	public static boolean toggleRule(Player player, DuelSession session, int buttonId) {
		Optional<DuelingRules> has_rule = DuelingRules.getRules(buttonId);
		
		if(!has_rule.isPresent()) {
			return false;
		}
		
		DuelingRules rule = has_rule.get();
		
		if(session.rules.contains(rule)) {
			session.getPlayers().forEach(p -> {
				session.rules.remove(rule);
				session.setAttachment(null);
				p.getMessages().sendString("", 37927);
				p.getMessages().sendConfig(780 + rule.ordinal(), 0);
				session.lastRuleModification.reset();
			});
			return true;
		}
		
		if(!rule.meets(player, session)) {
			player.getMessages().sendConfig(780 + rule.ordinal(), 0);
			return false;
		}
		
		session.getPlayers().forEach(p -> {
			session.rules.add(rule);
			session.setAttachment(null);
			p.getMessages().sendString("", 37927);
			p.getMessages().sendConfig(780 + rule.ordinal(), 1);
		});
		session.lastRuleModification.reset();
		return true;
	}
	
	@Override
	public boolean canAddItem(Player player, Item item, int slot) {
		return true;
	}
	
	@Override
	public boolean canRemoveItem(Player player, Item item) {
		return true;
	}
	
	@Override
	public void accept(Player player, int stage) {
		Player other = this.getOther(player);
		if(other == null) {
			player.message("The other person can't trade now.");
			return;
		}
		switch(stage) {
			case OFFER_ITEMS:
				if(!lastRuleModification.elapsed(1_000)) {
					player.message("A rule was changed in the last second, you cannot accept yet.");
					player.getMessages().sendString("A rule was changed in recently, you cannot accept yet.", 37927);
					return;
				}
				if(!player.getInventory().hasCapacityFor(getExchangeSession().get(other).getItems())) {
					player.message("You don't have enough free slots for this many items.");
					return;
				}
				
				if(!other.getInventory().hasCapacityFor(getExchangeSession().get(player).getItems())) {
					String username = other.getFormatUsername();
					player.message(username + " doesn't have enough free slots for this many items");
					return;
				}
				
				if(!player.getInventory().hasCapacityFor(getEquipmentCount(player))) {
					player.message("You don't have enough space to remove the disabled equipped items.");
					return;
				}
				
				if(!other.getInventory().hasCapacityFor(getEquipmentCount(other))) {
					String username = other.getFormatUsername();
					player.message(username + " doesn't have enough space to remove the disabled equipped items.");
					return;
				}
				if(hasAttachment() && getAttachment() != player) {
					this.setAttachment(null);
					setStage(CONFIRM_DECISION);
					updateMainComponents();
					return;
				}
				setAttachment(player);
				player.getMessages().sendString("Waiting for other player...", 37927);
				getOther(player).getMessages().sendString("Other player has accepted", 37927);
				break;
			case CONFIRM_DECISION:
				if(hasAttachment() && getAttachment() != player) {
					setAttachment(null);
					accept(player, FINALIZE);
					return;
				}
				setAttachment(player);
				player.getMessages().sendString("Waiting for other player...", 6571);
				getOther(player).getMessages().sendString("Other player has accepted", 6571);
				break;
			case FINALIZE:
				DuelMinigame minigame = new DuelMinigame(this);
				this.getPlayers().forEach(t -> t.setMinigame(Optional.of(minigame)));
				minigame.onEnter(player);
				this.finalize(ExchangeSessionActionType.HALT);
				break;
		}
	}
	
	@Override
	public void updateMainComponents() {
		if(getStage() == OFFER_ITEMS) {
			for(Player player : getPlayers()) {
				IntStream.range(0, 14).forEach(order -> {
					if(player.getEquipment().get(order) != null) {
						player.getMessages().sendItemOnInterfaceSlot(13824, player.getEquipment().get(order), order);
					}
				});
				
				Player recipient = getOther(player);
				int remaining = recipient.getInventory().remaining();
				
				recipient.getMessages().sendItemsOnInterface(6669, getExchangeSession().get(player));
				recipient.getMessages().sendItemsOnInterface(6670, getExchangeSession().get(player));
				
				player.getMessages().sendItemsOnInterface(6669, getExchangeSession().get(player));
				player.getMessages().sendItemsOnInterface(6670, getExchangeSession().get(player));
				
				player.getMessages().sendItemsOnInterface(3322, player.getInventory());
				player.getMessages().sendInventoryInterface(37888, 3321);
				player.getMessages().sendString("", 37927);
				player.getMessages().sendString("Dueling with: " + name(recipient) + " (level-" + recipient.determineCombatLevel() + ")" + " who has @gre@" + remaining + " free slots", 37928);
				//player.getMessages().sendString("Whip & dds only", 669);
			}
		} else if(getStage() == CONFIRM_DECISION) {
			List<DuelingRules> collection = DuelingRules.VALUES.asList();
			int interfaceIndex = 8242;
			boolean worn = false;
			for(Player player : getPlayers()) {
				Player recipient = getOther(player);
				
				player.getMessages().sendString("", 6571);
				player.getMessages().sendString("", 8240);
				player.getMessages().sendString("", 8241);
				
				for(int i = 0; i < DuelingRules.VALUES.size(); i++) {
					if(rules.contains(collection.get(i))) {
						player.getMessages().sendString(collection.get(i).getInterfaceMessage(), interfaceIndex);
						interfaceIndex++;
					}
				}
				
				for(int i = DuelingRules.HELM.ordinal(); i < DuelingRules.VALUES.size(); i++) {
					if(rules.contains(collection.get(i))) {
						player.getMessages().sendString(collection.get(i).getInterfaceMessage(), interfaceIndex);
						interfaceIndex++;
						worn = true;
					}
				}
				
				if(worn) {
					player.getMessages().sendString("Some worn items will be taken off.", 8238);
					player.getMessages().sendString("Combat statistics will be restored.", 8250);
					player.getMessages().sendString("Existing prayers will be stopped.", 8239);
				} else {
					player.getMessages().sendString("Existing prayers will be stopped.", 8250);
					player.getMessages().sendString("Combat statistics will be restored.", 8238);
					player.getMessages().sendString("", 8239);
				}
				
				if(interfaceIndex < 8254) {
					for(int i = interfaceIndex; i < 8254; i++) {
						if(i != 8250) {
							player.getMessages().sendString("", i);
						}
					}
				}
				
				player.getMessages().sendString(getItemNames(recipient, this.getExchangeSession().get(recipient).getItems()), 6517);
				player.getMessages().sendString(getItemNames(player, this.getExchangeSession().get(player).getItems()), 6516);
				
				player.getMessages().sendItemsOnInterface(3322, player.getInventory());
				player.getMessages().sendInventoryInterface(6412, 3321);
			}
		}
	}
	
	/**
	 * Determines and returns the text for {@code items} that will be displayed
	 * on the confirm trade screen.
	 * @param items the array of items to display.
	 * @return the confirm text for the array of items.
	 */
	private String getItemNames(Player player, Item[] items) {
		String tradeItems = "Absolutely nothing!";
		String tradeAmount = "";
		int count = 0;
		for(Item item : items) {
			if(item == null || (item != null && tradeItems.contains(item.getDefinition().getName()))) {
				continue;
			}
			int amount = this.getExchangeSession().get(player).computeAmountForId(item.getId());
			tradeAmount = item.getDefinition().isStackable() ? amount >= 1000 && amount < 1000000 ? "@cya@" + (amount / 1000) + "K @whi@" + "(" + amount + ")" : amount >= 1000000 ? "@gre@" + (amount / 1000000) + " " + "million @whi@(" + amount + ")" : "" + amount : "(x" + amount + ")";
			tradeItems = count == 0 ? item.getDefinition().getName() : tradeItems + "\\n" + item.getDefinition().getName();
			tradeItems = tradeItems + (item.getDefinition().isStackable() ? " x " : " ") + tradeAmount;
			count++;
		}
		return tradeItems;
	}
	
	@Override
	public void updateOfferComponents() {
		for(Player player : getExchangeSession().keySet()) {
			Player recipient = getOther(player);
			int remaining = recipient.getInventory().remaining();
			
			player.getMessages().sendItemsOnInterface(3322, player.getInventory());
			player.getMessages().sendInventoryInterface(37888, 3321);
			player.getMessages().sendItemsOnInterface(6669, getExchangeSession().get(player));
			player.getMessages().sendItemsOnInterface(6670, getExchangeSession().get(recipient));
			player.getMessages().sendString("", 37927);
			player.getMessages().sendString("Dueling with: " + name(recipient) + " (level-" + recipient.determineCombatLevel() + ")" + " who has @gre@" + remaining + " free slots", 6671);
		}
	}
	
	@Override
	public void onReset() {
		this.rules.clear();
		this.lastRuleModification.reset();
		for(DuelingRules r : DuelingRules.VALUES) {
			this.getPlayers().forEach(p -> p.getMessages().sendConfig(780 + r.ordinal(), 0));
		}
	}
	
	/**
	 * Gets the amount of inventory items to unequip.
	 * @return the numerical value for the amount of items to unequip.
	 */
	private Item[] getEquipmentCount(Player player) {
		ObjectList<Item> items = new ObjectArrayList<>();
		for(DuelingRules rule : DuelingRules.VALUES) {
			if(rule.getSlot() == -1 || player.getEquipment().get(rule.getSlot()) == null) {
				continue;
			}
			if(this.rules.contains(rule)) {
				items.add(player.getEquipment().get(rule.getSlot()));
			}
		}
		return items.toArray(new Item[items.size()]);
	}
	
	/**
	 * Determines and returns the duel display name for {@code player}.
	 * @param player the player to determine this display name for.
	 * @return the duel display name.
	 */
	private String name(Player player) {
		return player.getFormatUsername();
	}
	
	/**
	 * @return the rules
	 */
	public EnumSet<DuelingRules> getRules() {
		return rules;
	}
}
