package net.edge.world.content.market.exchange.item;

import net.edge.world.content.market.MarketCounter;
import net.edge.world.node.item.ItemDefinition;

/**
 * Represents an exchanged item in the {@link MarketCounter}.
 */
public class ExchangeItem {
	
	/**
	 * The identification of this item.
	 */
	private int id;
	
	/**
	 * The quantity of this item.
	 */
	private int amount;
	
	/**
	 * The desired price of this item.
	 */
	private int desiredPrice;
	
	/**
	 * The state of this item.
	 */
	private final ExchangeItemState state;
	
	/**
	 * Creates a new {@link ExchangeItem}.
	 * @param id           the identification of this item.
	 * @param amount       the quantity of this item.
	 * @param desiredPrice the desired price, being sold or bought.
	 * @param state        the exchangable state of this item.
	 */
	public ExchangeItem(int id, int amount, int desiredPrice, ExchangeItemState state) {
		this.id = id;
		this.amount = amount;
		this.desiredPrice = desiredPrice;
		this.state = state;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getDesiredPrice() {
		return desiredPrice;
	}
	
	public void setDesiredPrice(int desiredPrice) {
		this.desiredPrice = desiredPrice;
	}
	
	public int getBasePrice() {
		return 0;//add it.
	}
	
	public ExchangeItemState getState() {
		return state;
	}
	
	public ItemDefinition getDefinition() {
		return ItemDefinition.get(id);
	}
}
