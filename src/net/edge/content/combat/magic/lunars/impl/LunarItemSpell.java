package net.edge.content.combat.magic.lunars.impl;

import net.edge.content.combat.magic.lunars.LunarSpell;
import net.edge.world.node.actor.Actor;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

/**
 * Holds support for Lunar Spells which are casted when they are used on inventory items.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class LunarItemSpell extends LunarSpell {
	
	/**
	 * The spell identification.
	 */
	private final int spellId;
	
	/**
	 * The interface identification.
	 */
	private final int interfaceId;
	
	/**
	 * Constructs a new {@link LunarItemSpell}.
	 * @param spellId     {@link #spellId}.
	 * @param interfaceId {@link #interfaceId}.
	 */
	public LunarItemSpell(int spellId, int interfaceId) {
		this.spellId = spellId;
		this.interfaceId = interfaceId;
	}
	
	/**
	 * The spell casted on the specified {@code item}.
	 * @param caster the player casting the spell.
	 * @param item   the item the spell was used on.
	 */
	public abstract void effect(Player caster, Item item);
	
	public abstract boolean canCast(Player caster, Item item);
	
	@Override
	public final void effect(Player caster, Actor victim) {

	}
	
	@Override
	public final boolean prerequisites(Player caster, Actor victim) {
		return true;
	}
	
	/**
	 * @return the spellId
	 */
	public int getSpellId() {
		return spellId;
	}
	
	/**
	 * @return the interfaceId
	 */
	public int getInterfaceId() {
		return interfaceId;
	}
}
