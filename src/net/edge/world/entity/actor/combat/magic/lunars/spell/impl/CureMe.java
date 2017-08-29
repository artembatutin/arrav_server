package net.edge.world.entity.actor.combat.magic.lunars.spell.impl;

import net.edge.net.packet.out.SendConfig;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.magic.MagicRune;
import net.edge.world.entity.actor.combat.magic.RequiredRune;
import net.edge.world.entity.actor.combat.magic.lunars.spell.LunarButtonSpell;

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
		super("Cure Me", 117139, 71, 69, new RequiredRune(MagicRune.ASTRAL_RUNE, 2), new RequiredRune(MagicRune.COSMIC_RUNE, 2));
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
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);

		caster.toPlayer().message("You are no longer poisoned...");
		caster.toPlayer().out(new SendConfig(174, 0));
		caster.getPoisonDamage().set(0);
	}

	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!super.canCast(caster, victim)) {
			return false;
		}
		if(!caster.isPoisoned()) {
			caster.toPlayer().message("You are not poisoned.");
			return false;
		}
		return true;
	}
}
