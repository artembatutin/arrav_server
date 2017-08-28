package net.edge.content.skill.hunter.butterfly;

/**
 * A class which acts as a simple policy for butterflies.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ButterflyPolicy {

	/**
	 * The requirement required.
	 */
	private final int requirement;

	/**
	 * The experience gained.
	 */
	private final int experience;

	/**
	 * Constructs a new {@link ButterflyPolicy}.
	 *
	 * @param requirement {@link #requirement}.
	 * @param experience  {@link #experience}.
	 */
	public ButterflyPolicy(int requirement, int experience) {
		this.requirement = requirement;
		this.experience = experience;
	}

	/**
	 * @return the requirement
	 */
	public int getRequirement() {
		return requirement;
	}

	/**
	 * @return the experience
	 */
	public int getExperience() {
		return experience;
	}
}
