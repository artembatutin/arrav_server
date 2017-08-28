package net.edge.content.skill.slayer;

import net.edge.world.entity.item.Item;

import java.util.stream.IntStream;

/**
 * Represents a single slayer key, which basically chains the minimum and maximum
 * amount to the specified slayer key.
 *
 * @author <a href="http://www.rune-server.org/members/Stand+Up/">Stan</a>
 */
public final class SlayerKeyPolicy {

	/**
	 * The identifier for this slayer key.
	 */
	private final String key;

	/**
	 * The difficulty of this slayer key.
	 */
	private final SlayerDifficulty difficulty;

	/**
	 * The amount of times you need to kill this slayer key.
	 */
	private int[] amount;

	/**
	 * The combat requirement required to get this npc assigned.
	 */
	private final int requirement;

	/**
	 * Rewards for completing this task.
	 */
	private final Item[] rewards;

	/**
	 * Constructs a new {@link SlayerKeyPolicy}.
	 *
	 * @param key               {@link #key}.
	 * @param difficulty        {@link #difficulty}.
	 * @param minAmount         the minimum amount.
	 * @param maxAmount         the maximum amount.
	 * @param combatRequirement {@link #requirement}.
	 */
	SlayerKeyPolicy(String key, SlayerDifficulty difficulty, int minAmount, int maxAmount, int combatRequirement, Item... rewards) {
		this.key = key;
		this.difficulty = difficulty;
		this.amount = IntStream.rangeClosed(minAmount, maxAmount).toArray();
		this.requirement = combatRequirement;
		this.rewards = rewards;
	}

	/**
	 * @return the key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the difficulty.
	 */
	SlayerDifficulty getDifficulty() {
		return difficulty;
	}

	/**
	 * @return the amount.
	 */
	public int[] getAmount() {
		return amount;
	}

	/**
	 * Sets a new amount.
	 */
	public void setAmount(int[] amount) {
		this.amount = amount;
	}

	/**
	 * @return the requirement.
	 */
	public int getCombatRequirement() {
		return requirement;
	}

	/**
	 * @return rewards.
	 */
	public Item[] getRewards() {
		return rewards;
	}

}
