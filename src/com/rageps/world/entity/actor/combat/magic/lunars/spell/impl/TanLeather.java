package com.rageps.world.entity.actor.combat.magic.lunars.spell.impl;

import com.rageps.content.skill.crafting.Tanning;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.magic.MagicRune;
import com.rageps.world.entity.actor.combat.magic.RequiredRune;
import com.rageps.world.entity.actor.combat.magic.lunars.spell.LunarButtonSpell;

import java.util.Optional;

/**
 * Holds functionality for the tan leather lunar spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class TanLeather extends LunarButtonSpell {
	
	private Tanning.TanningData data;
	
	/**
	 * Constructs a new {@link TanLeather}.
	 */
	public TanLeather() {
		super("Tan Leather", 118122, 78, 81, new RequiredRune(MagicRune.NATURE_RUNE, 1), new RequiredRune(MagicRune.ASTRAL_RUNE, 2), new RequiredRune(MagicRune.FIRE_RUNE, 5));
	}
	
	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);
		Tanning.create(caster.toPlayer(), data, 5, true);
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!super.canCast(caster, victim)) {
			return false;
		}
		
		Tanning.TanningData data = Tanning.TanningData.getByPlayer(caster.toPlayer()).orElse(null);
		
		if(data == null) {
			caster.toPlayer().message("You don't have any leather or hides that can be tanned.");
			return false;
		}
		
		this.data = data;
		return true;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(713, 10));
	}
	
	@Override
	public Optional<Graphic> startGraphic() {
		return Optional.of(new Graphic(983, 0));
	}
	
}
