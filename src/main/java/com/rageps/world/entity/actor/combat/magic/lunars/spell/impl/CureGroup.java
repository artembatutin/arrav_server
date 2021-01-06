package com.rageps.world.entity.actor.combat.magic.lunars.spell.impl;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.net.refactor.packet.out.model.ConfigPacket;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatUtil;
import com.rageps.world.entity.actor.combat.magic.MagicRune;
import com.rageps.world.entity.actor.combat.magic.RequiredRune;
import com.rageps.world.entity.actor.combat.magic.lunars.spell.LunarButtonSpell;
import com.rageps.world.entity.actor.player.PlayerAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Holds functionality for the cure group spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CureGroup extends LunarButtonSpell {
	
	List<Player> local_players;
	
	/**
	 * Constructs a new {@link CureGroup}.
	 */
	public CureGroup() {
		super("Cure Group", 117170, 74, 74, new RequiredRune(MagicRune.ASTRAL_RUNE, 2), new RequiredRune(MagicRune.COSMIC_RUNE, 2));
	}
	
	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);
		
		for(Player target : local_players) {
			if(!target.getAttributeMap().getBoolean(PlayerAttributes.ACCEPT_AID)) {
				continue;
			}
			target.graphic(new Graphic(744, 90));
			target.getPoisonDamage().set(0);
			target.send(new ConfigPacket(174, 0));
			target.message("Your poison has been cured by " + caster.toPlayer().getFormatUsername());
		}
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!super.canCast(caster, victim)) {
			return false;
		}
		
		local_players = CombatUtil.actorsWithinDistance(caster, caster.getLocalPlayers(), 1);
		
		if(local_players.isEmpty()) {
			caster.toPlayer().message("There are no players within your radius to cast this spell for.");
			return false;
		}
		for(Player target : local_players) {
			if(!target.isPoisoned() || !target.getAttributeMap().getBoolean(PlayerAttributes.ACCEPT_AID)) {
				continue;
			}
			if(target.isPoisoned()) {
				return true;
			}
		}
		caster.toPlayer().message("There are no players within your radius which are poisoned.");
		return false;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(4409));
	}
}
