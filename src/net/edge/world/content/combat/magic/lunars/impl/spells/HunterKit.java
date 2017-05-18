package net.edge.world.content.combat.magic.lunars.impl.spells;

import net.edge.world.content.combat.magic.lunars.impl.LunarButtonSpell;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.model.Graphic;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the hunter kit lunar spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class HunterKit extends LunarButtonSpell {
	
	/**
	 * Constructs a new {@link HunterKit}.
	 */
	public HunterKit() {
		super(117147);
	}
	
	/**
	 * A constant representing the hunter kit item.
	 */
	public static final Item HUNTER_KIT = new Item(11159);
	
	/**
	 * The items received when opening the hunter kit.
	 */
	public static final Item[] ITEMS = Item.convert(10150, 10010, 10006, 10031, 10029, 596, 10008, 11260);
	
	@Override
	public void effect(Player caster, EntityNode victim) {
		caster.getInventory().add(HUNTER_KIT);
	}
	
	@Override
	public boolean prerequisites(Player caster, EntityNode victim) {
		if(!caster.getInventory().hasCapacityFor(HUNTER_KIT)) {
			caster.message("You don't have space for the hunter kit...");
			return false;
		}
		return true;
	}
	
	@Override
	public String name() {
		return "Hunter Kit";
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(6303));
	}
	
	@Override
	public Optional<Graphic> startGraphic() {
		return Optional.of(new Graphic(1074, 100));
	}
	
	@Override
	public int levelRequired() {
		return 71;
	}
	
	@Override
	public double baseExperience() {
		return 70;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(9075, 2), new Item(557, 2)});
	}
}
