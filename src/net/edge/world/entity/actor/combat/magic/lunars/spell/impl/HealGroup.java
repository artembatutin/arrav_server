package net.edge.world.entity.actor.combat.magic.lunars.spell.impl;

import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatUtil;
import net.edge.world.entity.actor.combat.magic.MagicRune;
import net.edge.world.entity.actor.combat.magic.RequiredRune;
import net.edge.world.entity.actor.combat.magic.lunars.spell.LunarButtonSpell;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.player.Player;

import java.util.List;
import java.util.Optional;

/**
 * Holds functionality for the heal group spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class HealGroup extends LunarButtonSpell {
	
	List<Player> local_players;
	
	/**
	 * Constructs a new {@link HealGroup}.
	 */
	public HealGroup() {
		super("Heal Group", 118106, 74, 74, new RequiredRune(MagicRune.ASTRAL_RUNE, 2), new RequiredRune(MagicRune.COSMIC_RUNE, 2));
	}
	
	@Override
	public void effect(Actor caster, Optional<Actor> victim) {
		super.effect(caster, victim);
		
		int transfer = (int) ((caster.getCurrentHealth() / 100.0f) * 75.0f);
		caster.damage(new Hit(transfer));
		transfer = transfer / local_players.size();
		String name = caster.toPlayer().getFormatUsername();
		for(Player target : local_players) {
			if(target.getCurrentHealth() >= (target.getMaximumHealth() / 10) || !target.getAttr().get("accept_aid").getBoolean()) {
				continue;
			}
			
			int victimMaxHealth = target.getMaximumHealth() * 10;
			
			if(transfer + target.getCurrentHealth() > victimMaxHealth) {
				transfer = victimMaxHealth - transfer;
			}
			
			target.healEntity(transfer);
			target.graphic(new Graphic(745, 90));
			
			target.message("You have been healed by " + name + ".");
		}
	}
	
	@Override
	public boolean canCast(Actor caster, Optional<Actor> victim) {
		if(!super.canCast(caster, victim)) {
			return false;
		}
		if(caster.getCurrentHealth() < ((caster.toPlayer().getMaximumHealth()) / 100.0f) * 11.0f) {
			caster.toPlayer().message("Your hitpoints are too low to cast this spell.");
			return false;
		}
		
		local_players = CombatUtil.actorsWithinDistance(caster, caster.getLocalPlayers(), 1);
		
		if(local_players.isEmpty()) {
			caster.toPlayer().message("There are no players within your radius to cast this spell for.");
			return false;
		}
		
		for(Player target : local_players) {
			if(target.getCurrentHealth() >= (target.getMaximumHealth()) || !target.getAttr().get("accept_aid").getBoolean()) {
				continue;
			}
			if(target.getCurrentHealth() < (target.getMaximumHealth())) {
				return true;
			}
		}
		caster.toPlayer().message("There are no players within your radius which are below full health.");
		return false;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(4409));
	}
	
}
