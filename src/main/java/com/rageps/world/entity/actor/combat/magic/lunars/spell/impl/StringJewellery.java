package com.rageps.world.entity.actor.combat.magic.lunars.spell.impl;

import com.rageps.content.skill.crafting.AmuletStringing;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.magic.MagicRune;
import com.rageps.world.entity.actor.combat.magic.RequiredRune;
import com.rageps.world.entity.actor.combat.magic.lunars.spell.LunarButtonSpell;

import java.util.Optional;

/**
 * Holds support for the string jewellery spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class StringJewellery extends LunarButtonSpell {
	
	/**
	 * The amulet data this spell is dependent of.
	 */
	private AmuletStringing.AmuletData data;
	
	/**
	 * Constructs a new {@link StringJewellery}.
	 */
	public StringJewellery() {
		super("String Jewellery", 117234, 80, 83, new RequiredRune(MagicRune.ASTRAL_RUNE, 2), new RequiredRune(MagicRune.EARTH_RUNE, 10), new RequiredRune(MagicRune.WATER_RUNE, 5));
	}
	
	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);
		AmuletStringing.create(caster.toPlayer(), data, true);
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!super.canCast(caster, victim)) {
			return false;
		}
		
		AmuletStringing.AmuletData data = AmuletStringing.AmuletData.getDefinition(caster.toPlayer()).orElse(null);
		
		if(data == null) {
			caster.toPlayer().message("You don't have any unstrung amulets...");
			return false;
		}
		this.data = data;
		return true;
	}
}
