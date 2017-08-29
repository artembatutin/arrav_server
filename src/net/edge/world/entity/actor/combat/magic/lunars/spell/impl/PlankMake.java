package net.edge.world.entity.actor.combat.magic.lunars.spell.impl;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.magic.MagicRune;
import net.edge.world.entity.actor.combat.magic.RequiredRune;
import net.edge.world.entity.actor.combat.magic.lunars.spell.LunarButtonSpell;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the plank make spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class PlankMake extends LunarButtonSpell {
	
	/**
	 * Constructs a new {@link PlankMake}.
	 */
	public PlankMake() {
		super("Plank Make", 118034, 86, 90, new RequiredRune(MagicRune.ASTRAL_RUNE, 3), new RequiredRune(MagicRune.EARTH_RUNE, 15), new RequiredRune(MagicRune.NATURE_RUNE, 1));
	}
	
	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);
		
		for(int i : PLANKS.keySet()) {
			if(caster.toPlayer().getInventory().contains(i)) {
				caster.toPlayer().getInventory().remove(new Item(i));
				caster.toPlayer().getInventory().add(new Item(PLANKS.get(i)));
			}
		}
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!super.canCast(caster, victim)) {
			return false;
		}
		if(PLANKS.keySet().stream().noneMatch(caster.toPlayer().getInventory()::contains)) {
			caster.toPlayer().message("You don't have any logs that can be made into planks.");
			return false;
		}
		return true;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(6298));
	}
	
	@Override
	public Optional<Graphic> startGraphic() {
		return Optional.of(new Graphic(1063, 100));
	}
	
	private static final Int2IntArrayMap PLANKS = new Int2IntArrayMap(ImmutableMap.<Integer, Integer>builder().put(1511, 960)//regular plank
			.put(1521, 8778)//oak plank
			.put(6333, 8780)//teak plank
			.put(6332, 8782)//mahogany plank
			.build());
	
}
