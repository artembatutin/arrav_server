package net.edge.content.combat.magic;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

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
	public final void executeOnHit(Actor cast, Actor castOn, boolean accurate, int damage) {
		if(accurate) {
			effect(cast, castOn);
		}
	}
	
	/**
	 * Executed when the spell casted by {@code cast} hits {@code castOn}.
	 * @param cast   the character who casted the spell.
	 * @param castOn the character who the spell was casted on.
	 */
	public abstract void effect(Actor cast, Actor castOn);
}
