package com.rageps.world.entity.actor.combat.magic.lunars;

import com.rageps.content.skill.Skills;
import com.rageps.content.skill.magic.Spellbook;
import com.rageps.util.Stopwatch;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.magic.MagicRune;
import com.rageps.world.entity.actor.combat.magic.MagicSpell;
import com.rageps.world.entity.actor.combat.magic.RequiredRune;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Holds basic support for Lunar Spells.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class LunarSpell extends MagicSpell {
	
	/**
	 * The delay the last spell was casted.
	 */
	private final Stopwatch delay = new Stopwatch();
	
	private final String name;
	
	public LunarSpell(String name, int level, double baseExperience, RequiredRune... runes) {
		super(level, baseExperience, runes);
		this.name = name;
	}
	
	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		Player player = caster.toPlayer();
		
		MagicRune.remove(player, runes);
		Skills.experience(player, baseExperience, Skills.MAGIC);
		
		startAnimation().ifPresent(player::animation);
		startGraphic().ifPresent(player::graphic);
		delay.reset();
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!caster.isPlayer()) {
			return false;
		}
		
		if(super.canCast(caster, victim)) {
			return false;
		}
		
		Player player = caster.toPlayer();
		
		if(!player.getSpellbook().equals(Spellbook.LUNAR)) {
			return false;
		}
		
		if(!delay.elapsed(delay(), TimeUnit.MILLISECONDS)) {
			player.message("You must wait " + (TimeUnit.MILLISECONDS.toSeconds(delay() - delay.elapsedTime())) + " seconds before casting " + name + " again.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * The animation that should be played when the spell is casted.
	 * @return an optional containing the animation, {@link Optional#empty()} otherwise.
	 */
	public Optional<Animation> startAnimation() {
		return Optional.empty();
	}
	
	/**
	 * The graphic that should be played when the spell is casted.
	 * @return an optional containing the graphic, {@link Optional#empty()} otherwise.
	 */
	public Optional<Graphic> startGraphic() {
		return Optional.empty();
	}
	
	/**
	 * The delay in <b>milliseconds<b/> before this spell can be used again.
	 * @return a numerical value determining the delay.
	 */
	public int delay() {
		return 1800;
	}
	
}
