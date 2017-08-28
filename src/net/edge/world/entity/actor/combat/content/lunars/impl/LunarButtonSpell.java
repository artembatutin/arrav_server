package net.edge.world.entity.actor.combat.content.lunars.impl;

import net.edge.world.entity.actor.combat.content.RequiredRune;
import net.edge.world.entity.actor.combat.content.lunars.LunarSpell;

/**
 * Holds support for Lunar Spells which are casted when a button is clicked.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class LunarButtonSpell extends LunarSpell {
	
	/**
	 * The button id for this spell.
	 */
	public final int buttonId;
	
	public LunarButtonSpell(String name, int buttonId, int level, double baseExperience, RequiredRune... runes) {
		super(name, level, baseExperience, runes);
		this.buttonId = buttonId;
	}
}
