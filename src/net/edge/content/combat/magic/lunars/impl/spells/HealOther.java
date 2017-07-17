package net.edge.content.combat.magic.lunars.impl.spells;

import net.edge.content.combat.magic.lunars.impl.LunarCombatSpell;
import net.edge.world.node.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Hit;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the heal other lunar spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class HealOther extends LunarCombatSpell {
	
	/**
	 * Constructs a new {@link HealOther}.
	 */
	public HealOther() {
		super(30306);
	}
	
	@Override
	public void effect(Player caster, Actor victim) {
		Player target = victim.toPlayer();
		
		int transfer = (int) ((caster.getCurrentHealth() / 100.0f) * 75.0f);
		
		caster.faceEntity(target);
		caster.damage(new Hit(transfer));
		
		int victimMaxHealth = target.getMaximumHealth();
		
		if(transfer + target.getCurrentHealth() > victimMaxHealth) {
			transfer = victimMaxHealth - transfer;
		}
		
		target.healEntity(transfer);
		target.graphic(new Graphic(745, 90));
	}
	
	@Override
	public boolean prerequisites(Player caster, Actor victim) {
		Player target = victim.toPlayer();
		
		caster.getMovementQueue().reset();
		
		int targetHitpoints = target.getCurrentHealth();
		
		if(!target.getAttr().get("accept_aid").getBoolean()) {
			caster.message("This player is not accepting any aid.");
			return false;
		}
		
		if(caster.getCurrentHealth() < ((caster.getMaximumHealth()) / 100.0f) * 11.0f) {
			caster.message("Your hitpoints are too low to cast this spell.");
			return false;
		}
		
		if(targetHitpoints >= target.getMaximumHealth()) {
			caster.message("This players hitpoints are currently full.");
			return false;
		}
		return true;
	}
	
	@Override
	public String name() {
		return "Heal Other";
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(4411));
	}
	
	@Override
	public int levelRequired() {
		return 92;
	}
	
	@Override
	public double baseExperience() {
		return 101;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(565, 1), new Item(563, 3), new Item(9075, 3)});
	}
	
}
