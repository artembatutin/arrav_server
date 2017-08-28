package net.edge.content.combat.content.lunars.impl.spells;

import net.edge.content.combat.content.MagicRune;
import net.edge.content.combat.content.RequiredRune;
import net.edge.content.combat.content.lunars.impl.LunarCombatSpell;
import net.edge.content.combat.hit.Hit;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * Holds functionality for the energy transfer lunar spell.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class EnergyTransfer extends LunarCombatSpell {

	/**
	 * Constructs a new {@link LunarCombatSpell}.
	 */
	public EnergyTransfer() {
		super("Energy Transfer", 30298, 91, 100, new RequiredRune(MagicRune.LAW_RUNE, 2), new RequiredRune(MagicRune.NATURE_RUNE, 1), new RequiredRune(MagicRune.ASTRAL_RUNE, 3));
	}

	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);

		Player target = victim.get().toPlayer();

		caster.faceEntity(target);

		int hit = (int) ((caster.getCurrentHealth() / 100.0f) * 10.0f);

		caster.damage(new Hit(hit));
		caster.toPlayer().getSpecialPercentage().set(0);

		target.graphic(new Graphic(734, 100));
		target.getSpecialPercentage().set(100);
		target.setRunEnergy(100);
		target.message("Your run and special attack energy have been restored by " + caster.toPlayer().getFormatUsername() + ".");
	}

	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		Player target = victim.get().toPlayer();

		caster.getMovementQueue().reset();

		if(!target.getAttr().get("accept_aid").getBoolean()) {
			caster.toPlayer().message("This player is not accepting any aid.");
			return false;
		}

		if(caster.getCurrentHealth() < ((caster.toPlayer().getMaximumHealth()) / 100.0f) * 10.0f) {
			caster.toPlayer().message("Your hitpoints are too low to cast this spell.");
			return false;
		}

		if(caster.toPlayer().getSpecialPercentage().get() != 100) {
			caster.toPlayer().message("Your special attack percentage has not fully regenerated yet to cast this spell.");
			return false;
		}

		if(target.getSpecialPercentage().get() == 100 && target.runEnergy == 100) {
			caster.toPlayer().message("This players run and special attack energy are fully replenished already.");
			return false;
		}
		return true;
	}

	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(4411));
	}

}
