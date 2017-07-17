package net.edge.world.node.item.container.session.impl;

import net.edge.net.packet.out.SendContainer;
import net.edge.net.packet.out.SendInventoryInterface;
import net.edge.util.log.Log;
import net.edge.util.log.impl.TradeLog;
import net.edge.world.node.item.container.session.ExchangeSession;
import net.edge.world.node.item.container.session.ExchangeSessionActionType;
import net.edge.world.node.item.container.session.ExchangeSessionType;
import net.edge.world.World;
import net.edge.world.node.EntityState;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

import java.util.Arrays;

/**
 * The trade session class that represents a trade session between 2 players.
 * @author lare96 <http://github.com/lare96>
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class TradeSession extends ExchangeSession {

	/**
	 * Creates a new {@link TradeSession}.
	 * @param player the player that controls this trade session.
	 * @param other  the player that was invited to this trade session.
	 * @param stage  the stage of this trade session.
	 */
	public TradeSession(Player player, Player other, int stage) {
		super(Arrays.asList(player, other), stage, ExchangeSessionType.TRADE);
	}

	@Override
	public void onRequest(Player player, Player requested) {
		TradeSession session = (TradeSession) World.getExchangeSessionManager().isAvailable(player, requested, ExchangeSessionType.TRADE).orElse(null);

		if(session != null) {
			session.setStage(OFFER_ITEMS);
			session.updateMainComponents();
			this.getPlayers().forEach(World.getExchangeSessionManager()::resetRequests);
			session.setAttachment(null);
		} else {
			session = new TradeSession(player, requested, ExchangeSession.REQUEST);
			player.message("Sending trade request...");
			requested.message(player.getFormatUsername() + ":tradereq:");
			session.setAttachment(player);
			World.getExchangeSessionManager().add(session);
		}
	}

	@Override
	public void onClickButton(Player player, int button) {
		switch(button) {
			case 13092:
				this.accept(player, OFFER_ITEMS);
				break;
			case 13218:
				this.accept(player, CONFIRM_DECISION);
				break;
		}
	}

	/**
	 * Determines and returns the trade display name for {@code player}.
	 * @param player the player to determine this display name for.
	 * @return the trade display name.
	 */
	private String name(Player player) {
		return player.getFormatUsername();
	}

	/**
	 * Determines and returns the text for {@code items} that will be displayed
	 * on the confirm trade screen.
	 * @param items the array of items to display.
	 * @return the confirm text for the array of items.
	 */
	private String getItemNames(Player player, Item[] items) {
		String tradeItems = "Absolutely nothing!";
		String tradeAmount;
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
		switch(stage) {
			case OFFER_ITEMS:
				if(!player.getInventory().hasCapacityFor(getExchangeSession().get(other).getItems())) {
					player.message("You don't have enough free slots for this many items.");
					break;
				}

				if(!other.getInventory().hasCapacityFor(getExchangeSession().get(player).getItems())) {
					String username = other.getFormatUsername();
					player.message(username + " doesn't have enough free slots for this many items");
					break;
				}

				if(hasAttachment() && getAttachment() != player) {
					this.setAttachment(null);
					setStage(CONFIRM_DECISION);
					updateMainComponents();
					return;
				}
				setAttachment(player);
				player.text(3431, "Waiting for other player...");
				other.text(3431, "Other player has accepted");
				break;
			case CONFIRM_DECISION:
				if(hasAttachment() && getAttachment() != player) {
					this.setAttachment(null);
					accept(player, FINALIZE);
					return;
				}
				setAttachment(player);
				other.text(3535, "Other player has accepted.");
				player.text(3535, "Waiting for other player...");
				break;
			case FINALIZE:
				if(other.getState() == EntityState.ACTIVE && player.getState() == EntityState.ACTIVE) {
					player.getInventory().addAll(this.getExchangeSession().get(other).getItems());
					other.getInventory().addAll(this.getExchangeSession().get(player).getItems());
					World.getLoggingManager().write(Log.create(new TradeLog(player, this.getExchangeSession().get(player), other, this.getExchangeSession().get(other))));
					World.getLoggingManager().write(Log.create(new TradeLog(other, this.getExchangeSession().get(other), player, this.getExchangeSession().get(player))));
					this.getPlayers().forEach(p -> p.message("Trade successfully completed with " + this.getOther(p).getFormatUsername()));
					this.finalize(ExchangeSessionActionType.DISPOSE_ITEMS);
				}
				break;
		}
	}

	@Override
	public void updateMainComponents() {
		if(getStage() == OFFER_ITEMS) {
			this.getPlayers().forEach(player -> {
				Player recipient = getOther(player);
				int remaining = recipient.getInventory().remaining();
				player.out(new SendContainer(3415, this.getExchangeSession().get(player)));
				player.out(new SendContainer(3416, this.getExchangeSession().get(player)));
				player.text(3431, "");
				player.text(3417, "Trading with: " + name(recipient) + " " + "who has @gre@" + remaining + " free slots");
				player.text(3535, "Are you sure you want to make this trade?");
				player.out(new SendInventoryInterface(3323, 3321));
				player.out(new SendContainer(3322, player.getInventory()));
			});
		} else if(getStage() == CONFIRM_DECISION) {
			this.getPlayers().forEach(player -> {
				Player recipient = getOther(player);

				player.out(new SendContainer(3214, player.getInventory()));
				player.text(3557, getItemNames(player, this.getExchangeSession().get(player).getItems()));
				player.text(3558, getItemNames(recipient, this.getExchangeSession().get(recipient).getItems()));
				player.out(new SendInventoryInterface(3443, 3213));
			});
		}
	}

	@Override
	public void updateOfferComponents() {
		for(Player player : this.getExchangeSession().keySet()) {
			Player recipient = getOther(player);
			int remaining = recipient.getInventory().remaining();
			player.out(new SendContainer(3322, player.getInventory()));
			player.out(new SendContainer(3415, this.getExchangeSession().get(player)));
			recipient.out(new SendContainer(3416, this.getExchangeSession().get(player)));
			player.text(3431, "");
			player.text(3417, "Trading with: " + name(recipient) + " " + "who has @gre@" + remaining + " free slots");
		}
	}

	@Override
	public void onReset() {
		this.getPlayers().forEach(p -> {
			p.closeWidget();
		});
	}

}
