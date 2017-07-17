package net.edge.content.combat.magic.lunars;

import net.edge.util.Stopwatch;
import net.edge.content.MagicStaff;
import net.edge.content.skill.Skills;
import net.edge.world.node.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Spell;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Spellbook;
import net.edge.world.node.item.Item;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Holds basic support for Lunar Spells.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class LunarSpell extends Spell {

	/**
	 * The delay the last spell was casted.
	 */
	private final Stopwatch delay = new Stopwatch();

	/**
	 * The functionality that should occur for the specified caster.
	 * @param caster the caster casting the spell.
	 * @param victim the victim hit by the spell.
	 */
	public abstract void effect(Player caster, Actor victim);

	/**
	 * Any checks that should be made before casting the spell.
	 * @param caster the caster casting the spell.
	 */
	public abstract boolean prerequisites(Player caster, Actor victim);

	/**
	 * The name of this spell.
	 * @return the alphabetical value representing this spell.
	 */
	public abstract String name();

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

	@Override
	public final Optional<Item[]> equipmentRequired(Player player) {
		return Optional.empty();
	}

	@Override
	public int startCast(Actor cast, Actor castOn) {
		if(!cast.isPlayer()) {
			return 0;
		}

		Player player = cast.toPlayer();

		if(!player.getSpellbook().equals(Spellbook.LUNAR)) {
			return 0;
		}

		if(!delay.elapsed(delay(), TimeUnit.MILLISECONDS)) {
			player.message("You must wait " + (TimeUnit.MILLISECONDS.toSeconds(delay() - delay.elapsedTime())) + " seconds before casting " + name() + " again...");
			return 0;
		}

		if(!this.canCast(player, false)) {
			return 0;
		}

		if(!prerequisites(player, castOn)) {
			return 0;
		}

		player.getInventory().removeAll(MagicStaff.suppressRunes(player, this.itemsRequired(player).get()));
		Skills.experience(player, this.baseExperience(), Skills.MAGIC);
		
		startAnimation().ifPresent(player::animation);
		startGraphic().ifPresent(player::graphic);
		effect(player, castOn);
		delay.reset();
		return 0;
	}

}
