package net.edge.content.combat.content.lunars.impl.spells;

import net.edge.content.combat.magic.lunars.impl.LunarCombatSpell;
import net.edge.net.packet.out.SendConfig;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the cure other lunar spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CureOther extends LunarCombatSpell {
	
	/**
	 * Constructs a new {@link CureOther}.
	 */
	public CureOther() {
		super(30048);
	}
	
	@Override
	public void effect(Player caster, Actor victim) {
		if(victim.isPlayer() && caster.isPlayer() && victim.isPoisoned()) {
			Player player = victim.toPlayer();
			player.out(new SendConfig(174, 0));
			player.message("Your poison has been cured by " + caster.toPlayer().getFormatUsername());
		}
		
		caster.faceEntity(victim);
		victim.graphic(new Graphic(748, 90));
		victim.getPoisonDamage().set(0);
	}
	
	@Override
	public boolean prerequisites(Player caster, Actor victim) {
		Player target = (Player) victim;
		
		caster.getMovementQueue().reset();
		
		if(!target.getAttr().get("accept_aid").getBoolean()) {
			caster.message("This player is not accepting any aid.");
			return false;
		}
		if(!victim.isPoisoned()) {
			caster.message("Your opponent is not poisoned.");
			return false;
		}
		return true;
	}
	
	@Override
	public String name() {
		return "Cure Other";
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(4411));
	}
	
	@Override
	public int levelRequired() {
		return 68;
	}
	
	@Override
	public double baseExperience() {
		return 65;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(9075, 1), new Item(563, 1), new Item(557, 10)});
	}
	
}
