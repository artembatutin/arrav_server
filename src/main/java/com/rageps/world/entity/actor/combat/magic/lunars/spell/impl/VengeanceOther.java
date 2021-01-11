package com.rageps.world.entity.actor.combat.magic.lunars.spell.impl;

import com.rageps.combat.listener.other.VengeanceListener;
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
 * Holds functionality for the vengeance other spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class VengeanceOther extends LunarCombatSpell {
	
	/**
	 * Constructs a new {@link LunarCombatSpell}.
	 */
	public VengeanceOther() {
		super("Vengeance Other", 30282, 93, 108, new RequiredRune(MagicRune.ASTRAL_RUNE, 3), new RequiredRune(MagicRune.DEATH_RUNE, 2), new RequiredRune(MagicRune.EARTH_RUNE, 10));
	}
	
	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);
		
		Player player = victim.get().toPlayer();
		
		caster.facePosition(player.getPosition());
		
		player.message(caster.toPlayer().getFormatUsername() + " has casted vengeance on you. ");
		
		player.graphic(new Graphic(725, 100));
		player.playerData.venged = true;
		player.getCombat().addListener(VengeanceListener.get());
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!super.canCast(caster, victim)) {
			return false;
		}
		
		Player player = victim.get().toPlayer();
		
		caster.getMovementQueue().reset();
		
		if(!player.getAttributeMap().getBoolean(PlayerAttributes.ACCEPT_AID)) {
			caster.toPlayer().message("This player is not accepting any aid.");
			return false;
		}
		
		if(player.playerData.venged) {
			caster.toPlayer().message(player.getFormatUsername() + " already has a vengeance spell casted...");
			return false;
		}
		return true;
	}
	
	@Override
	public int delay() {
		return 30_000;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(4411));
	}
	
}
