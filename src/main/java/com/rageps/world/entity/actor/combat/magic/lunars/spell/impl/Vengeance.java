package com.rageps.world.entity.actor.combat.magic.lunars.spell.impl;

import com.rageps.world.entity.actor.combat.attack.listener.other.VengeanceListener;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.magic.MagicRune;
import com.rageps.world.entity.actor.combat.magic.RequiredRune;
import com.rageps.world.entity.actor.combat.magic.lunars.spell.LunarButtonSpell;

import java.util.Optional;

/**
 * Holds functionality for the vengeance spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Vengeance extends LunarButtonSpell {
	
	/**
	 * Constructs a new {@link Vengeance}.
	 */
	public Vengeance() {
		super("Vengeance", 118082, 94, 112, new RequiredRune(MagicRune.ASTRAL_RUNE, 4), new RequiredRune(MagicRune.DEATH_RUNE, 2), new RequiredRune(MagicRune.EARTH_RUNE, 10));
	}
	
	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);
		caster.toPlayer().venged = true;
		caster.toPlayer().getCombat().addListener(VengeanceListener.get());
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(caster.toPlayer().venged) {
			caster.toPlayer().message("You already have an vengeance cast.");
			return false;
		}
		if(!super.canCast(caster, victim)) {
			return false;
		}
		return true;
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
}
