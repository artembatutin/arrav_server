package net.edge.content.combat.content.lunars;

import com.google.common.collect.ImmutableSet;
import net.edge.content.combat.content.lunars.impl.LunarButtonSpell;
import net.edge.content.combat.content.lunars.impl.LunarCombatSpell;
import net.edge.content.combat.content.lunars.impl.LunarItemSpell;
import net.edge.content.combat.content.lunars.impl.spells.*;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Holds functionality for executing lunar spells.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class LunarSpells {

	/**
	 * Caches the item spells.
	 */
	private static final ImmutableSet<LunarItemSpell> ITEM_SPELLS = ImmutableSet.of(new BakePie());

	/**
	 * Caches the button spells.
	 */
	private static final ImmutableSet<LunarButtonSpell> BUTTON_SPELLS = ImmutableSet.of(new CureMe(), new Humidify(), new HunterKit(), new CureGroup(), new TanLeather(), new Dream(), new StringJewellery(), new PlankMake(), new HealGroup(), new Vengeance());

	/**
	 * Caches the combat spells.
	 */
	private static final ImmutableSet<LunarCombatSpell> COMBAT_SPELLS = ImmutableSet.of(new CureOther(), new EnergyTransfer(), new HealOther(), new VengeanceOther());

	/**
	 * Gets the item spells.
	 * @param spellId     the spell id casted.
	 * @param interfaceId the interface id the spell casts on.
	 * @return an optional containing the spell, {@link Optional#empty()} otherwise.
	 */
	public static Optional<LunarItemSpell> getItemSpell(int spellId, int interfaceId) {
		return ITEM_SPELLS.stream().filter(s -> s.spellId == spellId && s.interfaceId == interfaceId).findAny();
	}

	/**
	 * Gets the button spells.
	 * @param buttonId the button id casted.
	 * @return an optional containing the spell, {@link Optional#empty()} otherwise.
	 */
	public static Optional<LunarButtonSpell> getButtonSpell(int buttonId) {
		return BUTTON_SPELLS.stream().filter(s -> s.buttonId == buttonId).findAny();
	}

	/**
	 * Gets the combat spells.
	 * @param spellId the spell id casted.
	 * @return an optional containing the spell, {@link Optional#empty()} otherwise.
	 */
	public static Optional<LunarCombatSpell> getCombatSpell(int spellId) {
		return COMBAT_SPELLS.stream().filter(s -> s.spellId == spellId).findAny();
	}

	/**
	 * Attempts to cast the spellbook swap spell.
	 * @param player the player casting this spell.
	 */
	public static void castSpellbookSwap(Player player) {
		player.getDialogueBuilder().append(new OptionDialogue(t -> {
			new SpellbookSwap(t).effect(player, Optional.empty());
			player.closeWidget();
		}, "Modern Magicks", "Ancient Magicks", "Nevermind"));
	}

	/**
	 * Attempts to cast the button spells.
	 * @param player   the player casting the spell.
	 * @param buttonId the button id of the spell.
	 * @return {@code true} if the spell was casted, {@code false} otherwise.
	 */
	public static boolean castButtonSpell(Player player, int buttonId) {
		LunarButtonSpell spell = getButtonSpell(buttonId).orElse(null);

		if(spell == null) {
			return false;
		}

		spell.effect(player, Optional.empty());
		return true;
	}

	/**
	 * Attempts to cast the item spells.
	 * @param player      the player casting the spell.
	 * @param item        the item the spell was casted on.
	 * @param spellId     the spell id of the spell.
	 * @param interfaceId the interface id of the spell.
	 * @return {@code true} if the spell was executed, {@code false} otherwise.
	 */
	public static boolean castItemSpells(Player player, Item item, int spellId, int interfaceId) {
		LunarItemSpell spell = getItemSpell(spellId, interfaceId).orElse(null);

		if(spell == null) {
			return false;
		}

		if(!spell.canCast(player, Optional.empty())) {
			return false;
		}

		if(!spell.canCast(player, item)) {
			return false;
		}

		spell.effect(player, item);
		return true;
	}

	/**
	 * Attempts to cast the combat spells.
	 * @param player  the player casting the spell.
	 * @param other   the other entity getting casted at.
	 * @param spellId the spell identification.
	 * @return {@code true} if the spell was casted, {@code false} otherwise.
	 */
	public static boolean castCombatSpells(Player player, Actor other, int spellId) {
		LunarCombatSpell spell = getCombatSpell(spellId).orElse(null);

		if(spell == null) {
			return false;
		}

		spell.effect(player, Optional.of(other));
		return true;
	}
}
