package net.edge.world.content.combat.magic.lunars.impl.spells;

import com.google.common.collect.ImmutableMap;
import net.edge.world.content.combat.magic.lunars.impl.LunarButtonSpell;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Map.Entry;
import java.util.Optional;

/**
 * Holds functionality for the plank make spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class PlankMake extends LunarButtonSpell {
	
	/**
	 * Constructs a new {@link PlankMake}.
	 */
	public PlankMake() {
		super(118034);
	}
	
	@Override
	public void effect(Player caster, EntityNode victim) {
		Optional<Entry<Integer, Integer>> set = PLANKS.entrySet().stream().filter(entry -> caster.getInventory().contains(entry.getKey())).findAny();
		
		set.ifPresent(plank -> {
			caster.getInventory().remove(new Item(plank.getKey()));
			caster.getInventory().add(new Item(plank.getValue()));
		});
	}
	
	@Override
	public boolean prerequisites(Player caster, EntityNode victim) {
		if(PLANKS.keySet().stream().noneMatch(caster.getInventory()::contains)) {
			caster.message("You don't have any logs that can be made into planks.");
			return false;
		}
		return true;
	}
	
	@Override
	public String name() {
		return "Plank Make";
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(6298));
	}
	
	@Override
	public Optional<Graphic> startGraphic() {
		return Optional.of(new Graphic(1063, 100));
	}
	
	@Override
	public int levelRequired() {
		return 86;
	}
	
	@Override
	public double baseExperience() {
		return 90;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(9075, 3), new Item(557, 15), new Item(561, 1)});
	}
	
	private static final ImmutableMap<Integer, Integer> PLANKS = ImmutableMap.<Integer, Integer>builder().put(1511, 960)//regular plank
			.put(1521, 8778)//oak plank
			.put(6333, 8780)//teak plank
			.put(6332, 8782)//mahogany plank
			.build();
}
