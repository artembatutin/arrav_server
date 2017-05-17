package net.edge.world.content.combat.magic.ancients;

import net.edge.world.content.combat.Combat;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.magic.CombatSpell;
import net.edge.world.model.locale.Location;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;

import java.util.Optional;

/**
 * The {@link CombatSpell} extension with support for effects and the ability to
 * multicast characters within a certain radius.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CombatAncientSpell extends CombatSpell {
	
	@Override
	public final void executeOnHit(EntityNode cast, EntityNode castOn, boolean accurate, int damage) {
		if(accurate) {
			effect(cast, castOn, damage);
			if(radius() == 0 || !Location.inMultiCombat(castOn))
				return;
			if(castOn.isPlayer()) {
				Combat.damagePlayersWithin(cast, castOn.getPosition(), radius(), 1, CombatType.MAGIC, false, t -> {
					cast.getCurrentlyCasting().endGraphic().ifPresent(t::graphic);
					effect(cast, castOn, damage);
				});
			} else {
				Combat.damageNpcsWithin(cast, castOn.getPosition(), radius(), 1, CombatType.MAGIC, false, t -> {
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
	public abstract void effect(EntityNode cast, EntityNode castOn, int damage);
	
	/**
	 * The radius of this spell for multicast support.
	 * @return the radius of this spell.
	 */
	public abstract int radius();
}
