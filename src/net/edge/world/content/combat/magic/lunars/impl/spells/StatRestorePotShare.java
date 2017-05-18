package net.edge.world.content.combat.magic.lunars.impl.spells;

import com.google.common.collect.ImmutableSet;
import net.edge.world.World;
import net.edge.world.content.combat.Combat;
import net.edge.world.content.combat.magic.lunars.impl.LunarButtonSpell;
import net.edge.world.content.item.PotionConsumable;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Holds functionality for the stat restore pot lunar spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class StatRestorePotShare extends LunarButtonSpell {
	
	/**
	 * Constructs a new {@link StatRestorePotShare}.
	 */
	public StatRestorePotShare() {
		super(117242);
	}
	
	private static final ImmutableSet<PotionConsumable> POTIONS = ImmutableSet.of(PotionConsumable.PRAYER_POTIONS, PotionConsumable.SUPER_PRAYER_POTIONS, PotionConsumable.RESTORE_POTIONS, PotionConsumable.SUPER_RESTORE_POTIONS, PotionConsumable.ENERGY_POTIONS, PotionConsumable.SUPER_ENERGY_POTIONS);
	
	List<Player> local_players;
	
	@Override
	public void effect(Player caster, EntityNode victim) {
		//PotionConsumable.consume(caster, item, slot)
	}
	
	@Override
	public boolean prerequisites(Player caster, EntityNode victim) {
		if(POTIONS.stream().noneMatch(pot -> caster.getInventory().containsAny(pot.getIds()))) {
			caster.message("You don't have any potion which can be shared with other players.");
			return false;
		}
		
		local_players = Combat.charactersWithinDistance(caster, World.getLocalPlayers(caster), 1).stream().limit(4).collect(Collectors.toList());
		
		if(local_players.isEmpty() || local_players.stream().noneMatch(p -> p.getAttr().get("accept_aid").getBoolean())) {
			caster.toPlayer().message("There are no players within your radius to cast this spell for.");
			return false;
		}
		return true;
	}
	
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int levelRequired() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double baseExperience() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
