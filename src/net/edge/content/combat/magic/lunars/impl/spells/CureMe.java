package net.edge.content.combat.magic.lunars.impl.spells;

import net.edge.content.combat.magic.lunars.impl.LunarButtonSpell;
import net.edge.net.packet.out.SendConfig;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the cure me spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CureMe extends LunarButtonSpell {

	/**
	 * Constructs a new {@link CureMe}.
	 */
	public CureMe() {
		super(117139);
	}

	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(4411));
	}

	@Override
	public Optional<Graphic> startGraphic() {
		return Optional.of(new Graphic(748, 90));
	}

	@Override
	public void effect(Player caster, Actor victim) {
		caster.message("You are no longer poisoned...");
		caster.out(new SendConfig(174, 0));
		caster.getPoisonDamage().set(0);
	}

	@Override
	public boolean prerequisites(Player caster, Actor victim) {
		if(!caster.isPoisoned()) {
			caster.message("You are not poisoned.");
			return false;
		}
		return true;
	}

	@Override
	public String name() {
		return "Cure Me";
	}

	@Override
	public int levelRequired() {
		return 71;
	}

	@Override
	public double baseExperience() {
		return 69;
	}

	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(9075, 2), new Item(564, 2)});

	}
}
