package net.edge.world.content.combat.magic.lunars.impl.spells;

import net.edge.world.World;
import net.edge.world.content.combat.Combat;
import net.edge.world.content.combat.magic.lunars.impl.LunarButtonSpell;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.model.Graphic;
import net.edge.world.node.entity.model.Hit;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.List;
import java.util.Optional;

/**
 * Holds functionality for the heal group spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class HealGroup extends LunarButtonSpell {
	
	/**
	 * Constructs a new {@link HealGroup}.
	 */
	public HealGroup() {
		super(118106);
	}
	
	List<Player> local_players;
	
	@Override
	public void effect(Player caster, EntityNode victim) {
		int transfer = (int) ((caster.getCurrentHealth() / 100.0f) * 75.0f);
		caster.damage(new Hit(transfer));
		transfer = transfer / local_players.size();
		
		String name = caster.getFormatUsername();
		for(Player target : local_players) {
			
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
	public boolean prerequisites(Player caster, EntityNode victim) {
		if(caster.getCurrentHealth() < ((caster.getMaximumHealth()) / 100.0f) * 11.0f) {
			caster.message("Your hitpoints are too low to cast this spell.");
			return false;
		}
		
		local_players = Combat.charactersWithinDistance(caster, World.getLocalPlayers(caster), 1);
		
		if(local_players.isEmpty()) {
			caster.message("There are no players within your radius to cast this spell for.");
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
		caster.message("There are no players within your radius which are below full health.");
		return false;
	}
	
	@Override
	public String name() {
		return "Cure Group";
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(4409));
	}
	
	@Override
	public int levelRequired() {
		return 74;
	}
	
	@Override
	public double baseExperience() {
		return 74;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(9075, 2), new Item(564, 2)});
	}
	
}
