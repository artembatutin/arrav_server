package net.edge.content.achievements;

/**
 * The types of achievement difficulties.
 *
 * @author Daniel
 */
public enum AchievementDifficulty {
	/**
	 * The easy achievement difficulty.
	 */
	EASY(75_000, 0xb3b4b3, "75K"),

	/**
	 * The medium achievement difficulty.
	 */
	MEDIUM(225_000, 0xD9750B, "225k"),

	/**
	 * The hard achievement difficulty.
	 */
	HARD(750_000, 0xbe7056, "750k"),

	/**
	 * The elite achievement difficulty.
	 */
	ELITE(1_500_000, 0xC41414, "1.5m");

	/**
	 * Cached difficulty array.
	 */
	public static final AchievementDifficulty DIF[] = values();

	/**
	 * The reward point for completing the achievement
	 */
	private final int reward;

	/**
	 * The color of the completion banner
	 */
	private final int color;

	/**
	 * The reward outgoing text.
	 */
	private final String out;

	/**
	 * Achievement difficulty
	 */
	AchievementDifficulty(int reward, int color, String out) {
		this.reward = reward;
		this.color = color;
		this.out = out;
	}

	/**
	 * Gets the reward point
	 */
	public int getReward() {
		return reward;
	}

	/**
	 * Gets the color
	 */
	public int getColor() {
		return color;
	}

	/**
	 * Out going reward string.
	 */
	public String getOut() {
		return out;
	}
}
