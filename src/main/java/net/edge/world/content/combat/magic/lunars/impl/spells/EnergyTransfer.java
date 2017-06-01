package net.edge.world.content.combat.magic.lunars.impl.spells;

import net.edge.world.content.combat.magic.lunars.impl.LunarCombatSpell;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Hit;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the energy transfer lunar spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class EnergyTransfer extends LunarCombatSpell {
	
	/**
	 * Constructs a new {@link EnergyTransferSpell}.
	 */
	public EnergyTransfer() {
		super(30298);
	}
	
	@Override
	public void effect(Player caster, EntityNode victim) {
		Player target = victim.toPlayer();
		
		caster.faceEntity(target);
		
		int hit = (int) ((caster.getCurrentHealth() / 100.0f) * 10.0f);
		
		caster.damage(new Hit(hit));
		caster.getSpecialPercentage().set(0);
		
		target.graphic(new Graphic(734, 100));
		target.getSpecialPercentage().set(100);
		target.setRunEnergy(100);
		target.message("Your run and special attack energy have been restored by " + caster.getFormatUsername() + ".");
	}
	
	@Override
	public boolean prerequisites(Player caster, EntityNode victim) {
		Player target = victim.toPlayer();
		
		caster.getMovementQueue().reset();
		
		if(!target.getAttr().get("accept_aid").getBoolean()) {
			caster.message("This player is not accepting any aid.");
			return false;
		}
		
		if(caster.getCurrentHealth() < ((caster.getMaximumHealth()) / 100.0f) * 10.0f) {
			caster.message("Your hitpoints are too low to cast this spell.");
			return false;
		}
		
		if(caster.getSpecialPercentage().get() != 100) {
			caster.message("Your special attack percentage has not fully regenerated yet to cast this spell.");
			return false;
		}
		
		if(target.getSpecialPercentage().get() == 100 && target.getRunEnergy() == 100) {
			caster.message("This players run and special attack energy are fully replenished already.");
			return false;
		}
		return true;
	}
	
	@Override
	public String name() {
		return "Energy Transfer";
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(4411));
	}
	
	@Override
	public int levelRequired() {
		return 91;
	}
	
	@Override
	public double baseExperience() {
		return 100;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(563, 2), new Item(561, 1), new Item(9075, 3)});
	}
}
