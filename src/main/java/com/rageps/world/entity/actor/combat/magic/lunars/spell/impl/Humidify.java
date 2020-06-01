package com.rageps.world.entity.actor.combat.magic.lunars.spell.impl;

import com.google.common.collect.ImmutableMap;
import com.rageps.world.entity.actor.combat.magic.lunars.spell.LunarButtonSpell;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.magic.MagicRune;
import com.rageps.world.entity.actor.combat.magic.RequiredRune;

import java.util.Optional;

/**
 * Holds functionality for the humidify spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Humidify extends LunarButtonSpell {
	
	/**
	 * Constructs a new {@link Humidify}.
	 */
	public Humidify() {
		super("Humidify", 117104, 68, 65, new RequiredRune(MagicRune.ASTRAL_RUNE, 1), new RequiredRune(MagicRune.FIRE_RUNE, 1), new RequiredRune(MagicRune.WATER_RUNE, 3));
	}
	
	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);
		
		VIALS.forEach((empty, filled) -> {
			if(caster.toPlayer().getInventory().contains(empty)) {
				caster.toPlayer().getInventory().replaceAll(empty, filled);
			}
		});
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!super.canCast(caster, victim)) {
			return false;
		}
		if(VIALS.keySet().stream().noneMatch(caster.toPlayer().getInventory()::contains)) {
			caster.toPlayer().message("You don't have any empty vessels that can be filled with water.");
			return false;
		}
		return true;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(722));
	}
	
	@Override
	public Optional<Graphic> startGraphic() {
		return Optional.of(new Graphic(1061));
	}
	
	private static final Int2IntArrayMap VIALS = new Int2IntArrayMap(ImmutableMap.<Integer, Integer>builder().put(1925, 1929)//bucket of water
			.put(1935, 1937)//jug of water
			.put(229, 227)//vial of water
			.put(5331, 5340)//watering can
			.put(1831, 1823)//waterskin
			.put(1923, 1921)//bowl of water
			.put(434, 1761)//clay
			.build());
	
}
