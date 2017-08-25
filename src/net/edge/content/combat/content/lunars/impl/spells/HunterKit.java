package net.edge.content.combat.content.lunars.impl.spells;

import net.edge.content.combat.content.MagicRune;
import net.edge.content.combat.content.RequiredRune;
import net.edge.content.combat.content.lunars.impl.LunarButtonSpell;
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
		super("Hunter Kit", 117147, 71, 70, new RequiredRune(MagicRune.ASTRAL_RUNE, 2), new RequiredRune(MagicRune.EARTH_RUNE, 2));
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
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);
		caster.toPlayer().getInventory().add(HUNTER_KIT);
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!super.canCast(caster, victim)) {
			return false;
		}
		if(!caster.toPlayer().getInventory().hasCapacityFor(HUNTER_KIT)) {
			caster.toPlayer().message("You don't have enough space for the hunter kit...");
			return false;
		}
		return true;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(6303));
	}
	
	@Override
	public Optional<Graphic> startGraphic() {
		return Optional.of(new Graphic(1074, 100));
	}
	

}
