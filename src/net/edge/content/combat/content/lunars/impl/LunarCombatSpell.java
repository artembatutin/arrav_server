package net.edge.content.combat.content.lunars.impl;

import net.edge.content.combat.content.RequiredRune;
import net.edge.content.combat.content.lunars.LunarSpell;

/**
 * Holds support for Lunar Spells which are casted on entities.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class LunarCombatSpell extends LunarSpell {
	
	/**
	 * The spell identification.
	 */
	public final int spellId;

	/**
	 * Constructs a new {@link LunarCombatSpell}.
	 * @param name				the name of this spell.
	 * @param spellId			the id of this spell.
	 * @param level				the level required to cast this spell.
	 * @param baseExperience	the base experience received for casting this spell.
	 * @param runes				the runes required to cast this spell.
	 */
	public LunarCombatSpell(String name, int spellId, int level, double baseExperience, RequiredRune... runes) {
		super(name, level, baseExperience, runes);
		this.spellId = spellId;
	}
}
