package net.edge.content.combat.magic.lunars.impl.spells;

import net.edge.content.combat.magic.lunars.impl.LunarButtonSpell;
import net.edge.world.node.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the vengeance spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Vengeance extends LunarButtonSpell {
	
	/**
	 * Constructs a new {@link Vengeance}.
	 * @param buttonId {@link #getButtonId()}
	 */
	public Vengeance() {
		super(118082);
	}
	
	@Override
	public void effect(Player caster, Actor victim) {
		caster.setVenged(true);
	}
	
	@Override
	public boolean prerequisites(Player caster, Actor victim) {
		if(caster.isVenged()) {
			caster.message("You have already casted this spell...");
			return false;
		}
		return true;
	}
	
	@Override
	public String name() {
		return "Vengeance";
	}
	
	@Override
	public int delay() {
		return 30_000;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(4410));
	}
	
	@Override
	public Optional<Graphic> startGraphic() {
		return Optional.of(new Graphic(726, 100));
	}
	
	@Override
	public int levelRequired() {
		return 94;
	}
	
	@Override
	public double baseExperience() {
		return 112;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(9075, 4), new Item(560, 2), new Item(557, 10)});
	}
}
