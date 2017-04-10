package net.edge.world.content.combat.magic;

import net.edge.world.model.node.entity.EntityNode;

/**
 * The {@link CombatSpell} extension with support for normal spells that have no
 * effects whatsoever.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CombatNormalSpell extends CombatSpell {
	
	@Override
	public void executeOnHit(EntityNode cast, EntityNode castOn, boolean accurate, int damage) {
		
	}
}