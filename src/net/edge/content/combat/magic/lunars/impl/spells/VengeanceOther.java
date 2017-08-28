package net.edge.content.combat.magic.lunars.impl.spells;

import net.edge.content.combat.magic.lunars.impl.LunarCombatSpell;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the vengeance other spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class VengeanceOther extends LunarCombatSpell {
	
	/**
	 * Constructs a new {@link VengeanceOther}.
	 */
	public VengeanceOther() {
		super(30282);
	}
	
	@Override
	public void effect(Player caster, Actor victim) {
		Player player = victim.toPlayer();
		caster.faceEntity(victim);
		player.message(caster.getFormatUsername() + " has casted vengeance on you... ");
		player.graphic(new Graphic(725, 100));
		player.venged = true;
	}
	
	@Override
	public boolean prerequisites(Player caster, Actor victim) {
		if(!victim.isPlayer()) {
			return false;
		}
		
		Player player = victim.toPlayer();
		
		caster.getMovementQueue().reset();
		
		if(!player.getAttr().get("accept_aid").getBoolean()) {
			player.message("This player is not accepting any aid.");
			return false;
		}
		
		if(player.venged) {
			caster.message(player.getFormatUsername() + " already has a vengeance spell casted...");
			return false;
		}
		return true;
	}
	
	@Override
	public String name() {
		return "Vengeance Other";
	}
	
	@Override
	public int delay() {
		return 30_000;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(4411));
	}
	
	@Override
	public int levelRequired() {
		return 93;
	}
	
	@Override
	public double baseExperience() {
		return 108;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(9075, 3), new Item(560, 2), new Item(557, 10)});
	}
	
}
