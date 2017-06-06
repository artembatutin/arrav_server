package net.edge.content.combat.magic;

import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * The {@link CombatSpell} extension with support for effects and no damage.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CombatEffectSpell extends CombatSpell {
	
	@Override
	public final int maximumHit() {
		return -1;
	}
	
	@Override
	public final Optional<Item[]> equipmentRequired(Player player) {
		return Optional.empty();
	}
	
	@Override
	public final void executeOnHit(EntityNode cast, EntityNode castOn, boolean accurate, int damage) {
		if(accurate) {
			effect(cast, castOn);
		}
	}
	
	/**
	 * Executed when the spell casted by {@code cast} hits {@code castOn}.
	 * @param cast   the character who casted the spell.
	 * @param castOn the character who the spell was casted on.
	 */
	public abstract void effect(EntityNode cast, EntityNode castOn);
}
