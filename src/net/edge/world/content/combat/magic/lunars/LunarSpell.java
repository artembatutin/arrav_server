package net.edge.world.content.combat.magic.lunars;

import net.edge.utils.Stopwatch;
import net.edge.world.content.MagicStaff;
import net.edge.world.content.skill.Skills;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.model.Graphic;
import net.edge.world.node.entity.model.Spell;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Spellbook;
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
	public abstract void effect(Player caster, EntityNode victim);

	/**
	 * Any checks that should be made before casting the spell.
	 * @param caster the caster casting the spell.
	 */
	public abstract boolean prerequisites(Player caster, EntityNode victim);

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
	public void startCast(EntityNode cast, EntityNode castOn) {
		if(!cast.isPlayer()) {
			return;
		}

		Player player = cast.toPlayer();

		if(!player.getSpellbook().equals(Spellbook.LUNAR)) {
			return;
		}

		if(!delay.elapsed(delay(), TimeUnit.MILLISECONDS)) {
			player.message("You must wait " + (TimeUnit.MILLISECONDS.toSeconds(delay() - delay.elapsedTime())) + " seconds before casting " + name() + " again...");
			return;
		}

		if(!this.canCast(player, false)) {
			return;
		}

		if(!prerequisites(player, castOn)) {
			return;
		}

		player.getInventory().removeAll(MagicStaff.suppressRunes(player, this.itemsRequired(player).get()));

		Skills.experience(player, this.baseExperience(), Skills.MAGIC);
		
		startAnimation().ifPresent(player::animation);
		startGraphic().ifPresent(player::graphic);
		effect(player, castOn);
		delay.reset();
	}

}
