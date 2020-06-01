package com.rageps.world.entity.actor.combat.magic.lunars.spell;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.combat.magic.RequiredRune;
import com.rageps.world.entity.actor.combat.magic.lunars.LunarSpell;
import com.rageps.world.entity.item.Item;

/**
 * Holds support for Lunar Spells which are casted when they are used on inventory items.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class LunarItemSpell extends LunarSpell {
	
	/**
	 * The spell identification.
	 */
	public final int spellId;
	
	/**
	 * The interface identification.
	 */
	public final int interfaceId;
	
	public LunarItemSpell(String name, int spellId, int interfaceId, int level, double baseExperience, RequiredRune... runes) {
		super(name, level, baseExperience, runes);
		this.spellId = spellId;
		this.interfaceId = interfaceId;
	}
	
	/**
	 * The spell casted on the specified {@code item}.
	 * @param caster the player casting the spell.
	 * @param item the item the spell was used on.
	 */
	public abstract void effect(Player caster, Item item);
	
	/**
	 * Determines if this spell can be casted.
	 * @param caster the caster who is casting the spell.
	 * @param item the item the spell was casted on.
	 * @return {@code true} if the spell can be casted, {@code false} otherwise.
	 */
	public abstract boolean canCast(Player caster, Item item);
}
