package net.arrav.content.skill.runecrafting;

/**
 * Acts as a multiplier for crafting runes.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class RunecraftingMultiplier {
	
	/**
	 * The requirement to craft multiple runes.
	 */
	private final int requirement;
	
	/**
	 * The amount to multipy by.
	 */
	private final int multiply;
	
	/**
	 * Constructs a new {@link RunecraftingMultiplier}.
	 * @param requirement {@link #requirement}.
	 * @param multiply {@link #multiply}.
	 */
	RunecraftingMultiplier(int requirement, int multiply) {
		this.requirement = requirement;
		this.multiply = multiply;
	}
	
	/**
	 * @return {@link #requirement}.
	 */
	public int getRequirement() {
		return requirement;
	}
	
	/**
	 * @return {@link #multiply}.
	 */
	public int getMultiply() {
		return multiply;
	}
}
