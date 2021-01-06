package com.rageps.combat.listener.item;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.net.packet.out.SendMessage;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.combat.listener.ItemCombatListenerSignature;
import com.rageps.combat.listener.SimplifiedListener;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.item.container.impl.Equipment;

/**
 * @author Michael | Chex
 */
@ItemCombatListenerSignature(items = {2550})
public class RingOfRecoilListener extends SimplifiedListener<Player> {
	
	@Override
	public void block(Actor attacker, Player defender, Hit hit, CombatType combatType) {
		if(hit.getDamage() < 10) {
			return;
		}
		
		int recoil = hit.getDamage() / 10;
		int charges = defender.playerData.ringOfRecoil;
		charges -= recoil;
		
		if(charges <= 0) {
			defender.message("Your ring of recoil has shattered!");
			defender.getEquipment().unequip(Equipment.RING_SLOT, null, true, -1);
			defender.getCombat().removeListener(this);
			// if charge is negative, recoil was too high for it's charge
			// so we add the -charges to get the amount of recoil left
			recoil += charges;
			charges = 400;
		}
		
		defender.playerData.ringOfRecoil = charges;
		attacker.damage(new Hit(recoil, Hitsplat.NORMAL_LOCAL));
		attacker.getCombat().getDamageCache().add(defender, new Hit(recoil));
	}
	
}
