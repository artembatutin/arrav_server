package com.rageps.world.entity.actor.combat.magic.lunars.spell.impl;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.magic.MagicRune;
import com.rageps.world.entity.actor.combat.magic.RequiredRune;
import com.rageps.world.entity.actor.combat.magic.lunars.spell.LunarCombatSpell;
import com.rageps.world.entity.actor.player.PlayerAttributes;

import java.util.Optional;

/**
 * Holds functionality for the energy transfer lunar spell.
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
		caster.toPlayer().playerData.getSpecialPercentage().set(0);
		
		target.graphic(new Graphic(734, 100));
		target.playerData.getSpecialPercentage().set(100);
		target.setRunEnergy(100);
		target.message("Your run and special attack energy have been restored by " + caster.toPlayer().getFormatUsername() + ".");
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		Player target = victim.get().toPlayer();
		
		caster.getMovementQueue().reset();
		
		if(!target.getAttributeMap().getBoolean(PlayerAttributes.ACCEPT_AID)) {
			caster.toPlayer().message("This player is not accepting any aid.");
			return false;
		}
		
		if(caster.getCurrentHealth() < ((caster.toPlayer().getMaximumHealth()) / 100.0f) * 10.0f) {
			caster.toPlayer().message("Your hitpoints are too low to cast this spell.");
			return false;
		}
		
		if(caster.toPlayer().playerData.getSpecialPercentage().get() != 100) {
			caster.toPlayer().message("Your special attack percentage has not fully regenerated yet to cast this spell.");
			return false;
		}
		
		if(target.playerData.getSpecialPercentage().get() == 100 && target.playerData.getRunEnergy() == 100) {
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
