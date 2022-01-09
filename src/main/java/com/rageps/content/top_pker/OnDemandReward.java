package com.rageps.content.top_pker;


import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

import java.time.ZonedDateTime;

/**
 * A reward obtained on-demand from the sql table. This almost always represents an item
 * and not a predefined reward.
 */
public class OnDemandReward implements Reward {

	/**
	 * The id of the session.
	 */
	private final int session;

	/**
	 * The predefined reward, or NONE if we are to expect an item.
	 */
	private final PredefinedReward predefinedReward;

	/**
	 * The item id if the predefined reward is NONE, or -1.
	 */
	private final int itemId;

	/**
	 * The amount of the item if the predefined reward is NONE, otherwise -1.
	 */
	private final int itemAmount;

	/**
	 * Constructs a new reward that follows the predefined contract of the class.
	 *
	 * @param session
	 * 			  the id of the session.
	 * @param predefinedReward
	 * 			  the predefined reward, or none.
	 * @param itemId
	 * 			  the item id, or -1.
	 * @param itemAmount
	 * 			  the item amount, or -1.
	 */
	private OnDemandReward(int session, PredefinedReward predefinedReward, int itemId, int itemAmount) {
		this.session = session;
		this.predefinedReward = predefinedReward;
		this.itemId = itemId;
		this.itemAmount = itemAmount;
	}

	/**
	 * Creates a new on-demand reward for a predefined reward.
	 *
	 * @param session
	 * 			  the id of the session.
	 * @param predefinedReward
	 * 			  the predefined reward.
	 */
	public OnDemandReward(int session, PredefinedReward predefinedReward) {
		this(session, predefinedReward, -1, -1);
	}

	/**
	 * Creates a new on-demand reward from custom items.
	 *
	 * @param session
	 * 			  the session id.
	 * @param itemId
	 * 			  the item id, not -1.
	 * @param itemAmount
	 * 			  the item amount, not -1.
	 */
	public OnDemandReward(int session, int itemId, int itemAmount) {
		this(session, PredefinedReward.NONE, itemId, itemAmount);
	}

	@Override
	public void append(Player player) {
		if (predefinedReward != PredefinedReward.NONE) {
			predefinedReward.append(player);
		} else {
			player.getBank().add(0, new Item(itemId, itemAmount));//todo bank tab
		}
	}

	@Override
	public final boolean appendable(Player player, ZonedDateTime endDate) {
		return true;
	}

	/**
	 * The id of the session.
	 *
	 * @return the session.
	 */
	public int getSession() {
		return session;
	}

	/**
	 * The predefined reward, or potentially {@link PredefinedReward#NONE}.
	 *
	 * @return the reward, or none.
	 */
	public PredefinedReward getPredefinedReward() {
		return predefinedReward;
	}

	/**
	 * The id of the reward to add.
	 *
	 * @return the item id.
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * The item amount of the reward to add.
	 *
	 * @return the amount.
	 */
	public int getItemAmount() {
		return itemAmount;
	}

	@Override
	public Item getItem() {
		return new Item(itemId, itemAmount);
	}

	@Override
	public String toString() {
		return "OnDemandReward{" + "session=" + session + ", predefinedReward=" + predefinedReward + ", itemId=" + itemId + ", itemAmount=" + itemAmount + '}';
	}
}
