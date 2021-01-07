package com.rageps.world.entity.actor.combat.magic.lunars.spell.impl;

import com.rageps.net.refactor.packet.out.model.ConfigPacket;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.magic.MagicRune;
import com.rageps.world.entity.actor.combat.magic.RequiredRune;
import com.rageps.world.entity.actor.combat.magic.lunars.spell.LunarCombatSpell;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;

import java.util.Optional;

/**
 * Holds functionality for the cure other lunar spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CureOther extends LunarCombatSpell {
	
	/**
	 * Constructs a new {@link LunarCombatSpell}.
	 */
	public CureOther() {
		super("Cure Other", 30048, 68, 65, new RequiredRune(MagicRune.ASTRAL_RUNE, 1), new RequiredRune(MagicRune.LAW_RUNE, 1), new RequiredRune(MagicRune.EARTH_RUNE, 10));
	}
	
	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);
		
		Player player = victim.get().toPlayer();
		caster.facePosition(player.getPosition());
		
		player.send(new ConfigPacket(174, 0));
		player.graphic(new Graphic(748, 90));
		player.getPoisonDamage().set(0);
		player.message("Your poison has been cured by " + caster.toPlayer().getFormatUsername());
		
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!super.canCast(caster, victim)) {
			return false;
		}
		
		Player target = (Player) victim.get();
		
		caster.getMovementQueue().reset();
		
		if(!target.getAttributeMap().getBoolean(PlayerAttributes.ACCEPT_AID)) {
			caster.toPlayer().message("This player is not accepting any aid.");
			return false;
		}
		if(!target.isPoisoned()) {
			caster.toPlayer().message("Your opponent is not poisoned.");
			return false;
		}
		return true;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(4411));
	}
	
}
