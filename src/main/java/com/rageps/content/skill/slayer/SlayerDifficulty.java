package com.rageps.content.skill.slayer;

/**
 * The slayer difficulty class which sorts tasks by their difficulty.
 * @author <a href="http://www.rune-server.org/members/Stand+Up/">Stan</a>
 */
public enum SlayerDifficulty {
	EASY(1), MODERATE(2), HARD(3);
	
	/**
	 * The value that matches this difficulty level.
	 */
	private final int value;
	
	/**
	 * Constructs a new {@link SlayerDifficulty}.
	 * @param value {@link #value}.
	 */
	SlayerDifficulty(int value) {
		this.value = value;
	}
	
	/**
	 * @return {@link #value}.
	 */
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}
}

