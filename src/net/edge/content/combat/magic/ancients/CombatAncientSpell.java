package net.edge.content.combat.magic.ancients;

import net.edge.content.combat.Combat;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatSpell;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * The {@link CombatSpell} extension with support for effects and the ability to
 * multicast characters within a certain radius.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CombatAncientSpell extends CombatSpell {
	
	@Override
	public final void executeOnHit(Actor cast, Actor castOn, boolean accurate, int damage) {
		if(accurate) {
			effect(cast, castOn, damage);
			if(radius() == 0 || !castOn.inMulti())
				return;
			if(castOn.isPlayer()) {
				Combat.damagePlayersWithin(cast, castOn.getPosition(), radius(), 1, CombatType.MAGIC, false, t -> {
					cast.getCurrentlyCasting().endGraphic().ifPresent(t::graphic);
					effect(cast, castOn, damage);
				});
			} else {
				Combat.damageMobsWithin(cast, castOn.getPosition(), radius(), 1, CombatType.MAGIC, false, t -> {
					cast.getCurrentlyCasting().endGraphic().ifPresent(t::graphic);
					effect(cast, castOn, damage);
				});
			}
		}
	}
	
	@Override
	public final Optional<Item[]> equipmentRequired(Player player) {
		return Optional.empty();
	}
	
	/**
	 * Executed when the spell casted by {@code cast} hits {@code castOn}.
	 * @param cast   the character who casted the spell.
	 * @param castOn the character who the spell was casted on.
	 * @param damage the damage that was inflicted by the spell.
	 */
	public abstract void effect(Actor cast, Actor castOn, int damage);
	
	/**
	 * The radius of this spell for multicast support.
	 * @return the radius of this spell.
	 */
	public abstract int radius();
}
