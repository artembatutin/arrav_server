package com.rageps.combat.listener.item;

import com.rageps.combat.listener.NpcCombatListenerSignature;
import com.rageps.world.entity.actor.Actor;
import com.rageps.combat.listener.ItemCombatListenerSignature;
import com.rageps.combat.listener.SimplifiedListener;
import com.rageps.world.entity.actor.combat.hit.Hit;

/**
 * Handles the Dharok's armor effects to the assigned npc and item ids.
 * @author Michael | Chex
 */
@NpcCombatListenerSignature(npcs = {1671})
@ItemCombatListenerSignature(items = {4716, 4718, 4720, 4722})
public class DharokListener extends SimplifiedListener<Actor> {

	@Override
	public void attack(Actor attacker, Actor defender, Hit hit) {
		super.attack(attacker, defender, hit);
	}

	@Override
	public int modifyDamage(Actor attacker, Actor defender, int damage) {
		int maxHealth;
		
		if(attacker.isMob()) {
			maxHealth = attacker.toMob().getDefinition().getHitpoints();
		} else {
			maxHealth = attacker.toPlayer().getMaximumHealth();
		}
		
		double multiplier = (((maxHealth - attacker.getCurrentHealth()) / 10) * 0.01) + 1;
		
		return (int) ((damage * multiplier));
	}
	
}
