package net.edge.content.combat.magic.lunars.impl;

import net.edge.content.combat.magic.lunars.LunarSpell;

/**
 * Holds support for Lunar Spells which are casted on entities.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class LunarCombatSpell extends LunarSpell {
	
	/**
	 * The spell identification.
	 */
	private final int spellId;
	
	/**
	 * Constructs a new {@link LunarCombatSpell}.
	 * @param spellId {@link #spellId}.
	 */
	public LunarCombatSpell(int spellId) {
		this.spellId = spellId;
	}
	
	/**
	 * @return the spellId
	 */
	public int getSpellId() {
		return spellId;
	}
}
