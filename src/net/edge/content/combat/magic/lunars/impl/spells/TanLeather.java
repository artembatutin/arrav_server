package net.edge.content.combat.magic.lunars.impl.spells;

import net.edge.content.combat.magic.lunars.impl.LunarButtonSpell;
import net.edge.content.skill.crafting.Tanning;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the tan leather lunar spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class TanLeather extends LunarButtonSpell {
	
	/**
	 * Constructs a new {@link TanLeather}.
	 */
	public TanLeather() {
		super(118122);
	}
	
	private Tanning.TanningData data;
	
	@Override
	public void effect(Player caster, EntityNode victim) {
		Tanning.create(caster, data, 5, true);
	}
	
	@Override
	public boolean prerequisites(Player caster, EntityNode victim) {
		Tanning.TanningData data = Tanning.TanningData.getByPlayer(caster).orElse(null);
		
		if(data == null) {
			caster.message("You don't have any leather or hides that can be tanned.");
			return false;
		}
		
		this.data = data;
		return true;
	}
	
	@Override
	public String name() {
		return "Tan Leather";
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(713, 10));
	}
	
	@Override
	public Optional<Graphic> startGraphic() {
		return Optional.of(new Graphic(983, 0));
	}
	
	@Override
	public int levelRequired() {
		return 78;
	}
	
	@Override
	public double baseExperience() {
		return 81;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.of(new Item[]{new Item(561, 1), new Item(9075, 2), new Item(554, 5)});
	}
	
}
