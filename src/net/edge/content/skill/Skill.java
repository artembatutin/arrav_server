package net.edge.content.skill;

import net.edge.util.Stopwatch;

/**
 * The representation of a single skill that can be trained by a player.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Skill {

	/**
	 * The maximum amount of experience that can be obtained in a single skill.
	 */
	private static final double MAXIMUM_EXPERIENCE = 200_000_000;

	/**
	 * The delay for this skill.
	 */
	private transient final Stopwatch delay = new Stopwatch();

	/**
	 * The level of this skill that can be trained.
	 */
	private int level = 1;

	/**
	 * The experience of this skill that can be trained.
	 */
	private double experience = 0;

	/**
	 * The cached real level of this skill that can be trained, to reduce the
	 * amount of expensive level calculations.
	 */
	private int realLevel = 1;

	/**
	 * The current goal of the current skill.
	 */
	private int goal = 0;

	/**
	 * Gets the level based on the experience the player has.
	 *
	 * @return the level based on experience.
	 */
	public int getLevelForExperience() {
		int points = 0;
		int output;
		if(realLevel >= 99) {
			return 99;
		}
		for(int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if(output >= this.getExperience()) {
				realLevel = lvl;
				return lvl;
			}
		}
		realLevel = 99;
		return 99;
	}

	/**
	 * Determines if your level is greater than or equal to {@code level}.
	 *
	 * @param level the level to compare against this one.
	 * @return {@code true} if this level is greater than or equal to the other
	 * one, {@code false} otherwise.
	 */
	public boolean reqLevel(int level) {
		return this.level >= level;
	}

	/**
	 * Increments this level by {@code amount} to a maximum of {@code 120}.
	 *
	 * @param amount the amount to increase this level by.
	 */
	public void increaseLevel(int amount) {
		increaseLevel(amount, 120);
	}

	/**
	 * Increments this level by {@code amount} to a maximum of
	 * {@code realLevel + amount}.
	 *
	 * @param amount the amount to increase this level by.
	 */
	public void increaseLevelReal(int amount) {
		increaseLevel(amount, realLevel + amount);
	}

	/**
	 * Decrements this level by {@code amount} to a minimum of
	 * {@code realLevel - amount}.
	 *
	 * @param amount the amount to decrease this level by.
	 */
	public void decreaseLevelReal(int amount) {
		decreaseLevel(amount, realLevel - amount);
	}

	/**
	 * Increments this level by {@code amount} to {@code maximum}.
	 *
	 * @param amount  the amount to increase this level by.
	 * @param maximum the maximum level to increase this to.
	 */
	public void increaseLevel(int amount, int maximum) {
		if((level + amount) > maximum) {
			level = maximum;
			return;
		}
		level += amount;
	}

	/**
	 * Decrements this level by {@code amount} to a minimum of {@code 0}.
	 *
	 * @param amount the amount to decrease this level by.
	 */
	public void decreaseLevel(int amount) {
		decreaseLevel(amount, 0);
	}

	/**
	 * Decrements this level by {@code amount} to a minimum of {@code 0}.
	 *
	 * @param amount the amount to decrease this level by.
	 * @param zero   determines if this skill can be decreased to zero.
	 */
	public void decreaseLevel(int amount, boolean zero) {
		decreaseLevel(amount, 0, zero);
	}

	/**
	 * Decrements this level by {@code maximum} to {@code minimum}.
	 *
	 * @param maximum the amount to decrease this level by.
	 * @param minimum the minimum amount to decrease this level by.
	 */
	public void decreaseLevel(int maximum, int minimum) {
		decreaseLevel(maximum, minimum, false);
	}

	/**
	 * Decrements this level by {@code maximum} to {@code minimum}.
	 *
	 * @param maximum the amount to decrease this level by.
	 * @param minimum the minimum amount to decrease this level by.
	 * @param zero    determines if this level can be decreased down to zero.
	 */
	public void decreaseLevel(int maximum, int minimum, boolean zero) {
		boolean check = zero ? level < 1 : level <= 1;
		if(check) {
			return;
		}
		if((level - maximum) < minimum) {
			level = minimum;
			return;
		}
		level -= maximum;
	}

	/**
	 * Increments this experience by {@code experience}, to a maximum of
	 * {@code MAXIMUM_EXPERIENCE}.
	 *
	 * @param experience the amount to increment by.
	 */
	public void increaseExperience(double experience) {
		setExperience(this.experience + experience);
	}

	/**
	 * Gets the experience for this skill.
	 *
	 * @return the experience.
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * Sets the value for {@link Skill#experience}.
	 *
	 * @param experience the new value to set.
	 */
	public void setExperience(double experience) {
		this.experience = experience;
		if(this.experience > MAXIMUM_EXPERIENCE) {
			this.experience = MAXIMUM_EXPERIENCE;
		}
	}

	/**
	 * Gets the delay for this skill.
	 *
	 * @return the delay.
	 */
	public Stopwatch getDelay() {
		return delay;
	}

	/**
	 * Gets the level for this skill.
	 *
	 * @return the level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the value for {@link Skill#level}.
	 *
	 * @param level       the new value to set.
	 * @param restriction if the level should be modified before being set.
	 */
	public void setLevel(int level, boolean restriction) {
		if(restriction) {
			if(level < 0) {
				level = 0;
			} else if(this.level > 99) {
				level = 99;
			}
		}
		this.level = level;
	}

	/**
	 * Gets the level based on how much experience you have.
	 *
	 * @return the level based on how much experience you have.
	 */
	public int getRealLevel() {
		return realLevel;
	}

	/**
	 * Sets the value for {@link Skill#realLevel}.
	 *
	 * @param realLevel the new value to set.
	 */
	public void setRealLevel(int realLevel) {
		if(realLevel < 0) {
			realLevel = 1;
		} else if(realLevel > 99) {
			realLevel = 99;
		}
		this.realLevel = realLevel;
		this.level = this.realLevel;
		int experience = Skills.getExperienceForLevel(this.realLevel);
		this.setExperience(experience);
	}

	/**
	 * Gets the current goal for this skill.
	 *
	 * @return the goal.
	 */
	public int getGoal() {
		return goal;
	}

	/**
	 * Sets the value for {@link Skill#goal}.
	 *
	 * @param goal the new value to set.
	 */
	public void setGoal(int goal) {
		this.goal = goal;
	}

}
