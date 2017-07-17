package net.edge.content.combat.magic.lunars.impl.spells;

import net.edge.content.combat.magic.lunars.impl.LunarButtonSpell;
import net.edge.content.skill.crafting.AmuletStringing;
import net.edge.world.node.actor.Actor;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * Holds support for the string jewellery spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class StringJewellery extends LunarButtonSpell {
	
	/**
	 * Constructs a new {@link StringJewellery}.
	 */
	public StringJewellery() {
		super(117234);
	}
	
	/**
	 * The amulet data this spell is dependent of.
	 */
	private AmuletStringing.AmuletData data;
	
	@Override
	public void effect(Player caster, Actor victim) {
		AmuletStringing.create(caster, data, true);
	}
	
	@Override
	public boolean prerequisites(Player caster, Actor victim) {
		AmuletStringing.AmuletData data = AmuletStringing.AmuletData.getDefinition(caster).orElse(null);
		
		if(data == null) {
			caster.message("You don't have any unstrung amulets...");
			return false;
		}
		this.data = data;
		return true;
	}
	
	@Override
	public String name() {
		return "String Jewellery";
	}
	
	@Override
	public int levelRequired() {
		return 80;
	}
	
	@Override
	public double baseExperience() {
		return 83;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(9075, 2), new Item(557, 10), new Item(555, 5)});
	}
	
}
