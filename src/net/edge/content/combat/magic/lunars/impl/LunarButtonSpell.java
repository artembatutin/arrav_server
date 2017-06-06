package net.edge.content.combat.magic.lunars.impl;

import net.edge.content.combat.magic.lunars.LunarSpell;

/**
 * Holds support for Lunar Spells which are casted when a button is clicked.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class LunarButtonSpell extends LunarSpell {
	
	/**
	 * The button id for this spell.
	 */
	private final int buttonId;
	
	/**
	 * Constructs a new {@link LunarButtonSpell}.
	 * @param buttonId {@link #buttonId}.
	 */
	public LunarButtonSpell(int buttonId) {
		this.buttonId = buttonId;
	}
	
	/**
	 * @return the buttonId
	 */
	public int getButtonId() {
		return buttonId;
	}
}
