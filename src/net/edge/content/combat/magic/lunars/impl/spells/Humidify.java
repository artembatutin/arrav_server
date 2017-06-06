package net.edge.content.combat.magic.lunars.impl.spells;

import com.google.common.collect.ImmutableMap;
import net.edge.content.combat.magic.lunars.impl.LunarButtonSpell;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the humidify spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Humidify extends LunarButtonSpell {

	/**
	 * Constructs a new {@link HumidifySpell}.
	 */
	public Humidify() {
		super(117104);
	}

	@Override
	public void effect(Player caster, EntityNode victim) {
		VIALS.forEach((empty, filled) -> {
			if(caster.getInventory().contains(empty)) {
				caster.getInventory().replaceAll(empty, filled);
			}
		});
	}

	@Override
	public boolean prerequisites(Player caster, EntityNode victim) {
		if(VIALS.keySet().stream().noneMatch(caster.getInventory()::contains)) {
			caster.message("You don't have any empty vessels that can be filled with water.");
			return false;
		}
		return true;
	}

	@Override
	public String name() {
		return "Humidify";
	}

	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(722));
	}

	@Override
	public Optional<Graphic> startGraphic() {
		return Optional.of(new Graphic(1061));
	}

	@Override
	public int levelRequired() {
		return 68;
	}

	@Override
	public double baseExperience() {
		return 65;
	}

	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(9075, 1), new Item(554, 1), new Item(555, 3)});
	}

	private static final ImmutableMap<Integer, Integer> VIALS = ImmutableMap.<Integer, Integer>builder().put(1925, 1929)//bucket of water
			.put(1935, 1937)//jug of water
			.put(229, 227)//vial of water
			.put(5331, 5340)//watering can
			.put(1831, 1823)//waterskin
			.put(1923, 1921)//bowl of water
			.put(434, 1761)//clay
			.build();

}
