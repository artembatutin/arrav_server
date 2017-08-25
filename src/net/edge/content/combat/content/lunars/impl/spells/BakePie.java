package net.edge.content.combat.content.lunars.impl.spells;

import net.edge.content.combat.content.MagicRune;
import net.edge.content.combat.content.RequiredRune;
import net.edge.content.combat.content.lunars.impl.LunarItemSpell;
import net.edge.content.skill.cooking.Cooking;
import net.edge.content.skill.cooking.CookingData;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the bake pie spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BakePie extends LunarItemSpell {


	private CookingData data;

	/**
	 * Constructs a new {@link BakePie}.
	 */
	public BakePie(String name, int spellId, int interfaceId, int level, double baseExperience, RequiredRune... runes) {
		super("Bake Pie", 30017, 3186, 63, 60, new RequiredRune(MagicRune.ASTRAL_RUNE, 1));
	}

	@Override
	public void effect(Player caster, Item item) {
		Cooking cooking = new Cooking(caster, null, data, false, 1, true);
		cooking.start();
	}

	@Override
	public boolean canCast(Player player, Item item) {
		CookingData data = CookingData.forItem(item);

		if(data == null) {
			player.message("You can only cast this spell on pies.");
			return false;
		}

		if(!data.toString().contains("pie")) {
			player.message("You can only bake pies with this spell.");
			return false;
		}
		this.data = data;
		return true;
	}


	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(9075, 1), new Item(554, 5), new Item(555, 4)});
	}

}
