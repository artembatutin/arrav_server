package net.edge.content.combat.content.lunars.impl.spells;

import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.content.MagicRune;
import net.edge.content.combat.content.RequiredRune;
import net.edge.content.combat.content.lunars.impl.LunarButtonSpell;
import net.edge.net.packet.out.SendConfig;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.player.Player;

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
			if(!target.getAttr().get("accept_aid").getBoolean()) {
				continue;
			}
			target.graphic(new Graphic(744, 90));
			target.getPoisonDamage().set(0);
			target.out(new SendConfig(174, 0));
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
			if(!target.isPoisoned() || !target.getAttr().get("accept_aid").getBoolean()) {
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
