package net.edge.content.combat.content.lunars.impl.spells;

import net.edge.content.combat.magic.lunars.impl.LunarButtonSpell;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

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
	public void effect(Player caster, Actor victim) {
		caster.getInventory().add(HUNTER_KIT);
	}
	
	@Override
	public boolean prerequisites(Player caster, Actor victim) {
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
