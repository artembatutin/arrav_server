package net.edge.content.combat.magic.lunars.impl.spells;

import net.edge.content.combat.magic.lunars.impl.LunarItemSpell;
import net.edge.content.skill.cooking.Cooking;
import net.edge.content.skill.cooking.CookingData;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the bake pie spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BakePie extends LunarItemSpell {

	/**
	 * Constructs a new {@link BakePie}.
	 */
	public BakePie() {
		super(30017, 3186);
	}

	private CookingData data;

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
	public String name() {
		return "Bake Pie";
	}

	@Override
	public int levelRequired() {
		return 65;
	}

	@Override
	public double baseExperience() {
		return 60;
	}

	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(9075, 1), new Item(554, 5), new Item(555, 4)});
	}

}
