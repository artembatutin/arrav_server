package net.arrav.content.market.currency;

import net.arrav.content.market.currency.impl.ItemCurrency;
import net.arrav.content.market.currency.impl.PestCurrency;
import net.arrav.content.market.currency.impl.SlayerCurrency;
import net.arrav.content.market.currency.impl.VoteCurrency;

import java.util.Optional;

/**
 * The enumerated type whose elements represent all of the different currencies
 * that can be used with shops.
 * @author lare96 <http://github.com/lare96>
 */
public enum Currency {
	SLAYER_POINTS(new SlayerCurrency()),
	COINS(new ItemCurrency(995)),
	TOKKUL(new ItemCurrency(6529)),
	CASTLE_WARS_TICKETS(new ItemCurrency(4067)),
	AGILITY_ARENA_TICKETS(new ItemCurrency(2996)),
	BLOOD_MONEY(new ItemCurrency(19000, Optional.of("blood money"))),
	EDGE_TOKENS(new ItemCurrency(7478, Optional.of("arrav tokens"))),
	VOTE_POINTS(new VoteCurrency()),
	PEST_POINTS(new PestCurrency()),
	STARDUST(new ItemCurrency(13727));
	
	/**
	 * The currency that is represented by this element.
	 */
	private final GeneralCurrency currency;
	
	/**
	 * Creates a new {@link Currency}.
	 * @param currency the currency that is represented by this element.
	 */
	Currency(GeneralCurrency currency) {
		this.currency = currency;
	}
	
	@Override
	public final String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}
	
	/**
	 * Gets the currency that is represented by this element.
	 * @return the currency that is represented.
	 */
	public final GeneralCurrency getCurrency() {
		return currency;
	}
}
