package net.edge.content.combat.attack.listener.impl;

import net.edge.content.combat.CombatType;
import net.edge.content.combat.attack.listener.PlayerCombatListenerSignature;
import net.edge.content.combat.attack.listener.SimplifiedListener;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.hit.HitIcon;
import net.edge.net.packet.out.SendMessage;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.container.impl.Equipment;

/** @author Michael | Chex */
@PlayerCombatListenerSignature(items = {2550})
public class RingOfRecoilListener extends SimplifiedListener<Player> {

    @Override
    public void block(Actor attacker, Player defender, Hit hit, CombatType combatType) {
        if (hit.getDamage() < 10) {
            return;
        }

        int recoil = hit.getDamage() / 10;
        int charges = defender.getRingOfRecoil();
        charges -= recoil;

        if (charges <= 0) {
            defender.out(new SendMessage("Your ring of recoil has shattered!"));
            defender.getEquipment().unequip(Equipment.RING_SLOT, null, true, -1);
            defender.getCombat().removeListener(this);

            // if charge is negative, recoil was too high for it's charge
            // so we add the -charges to get the amount of recoil left
            recoil += charges;
            charges = 400;
        }

        defender.setRingOfRecoil(charges);
        attacker.damage(new Hit(recoil, HitIcon.DEFLECT));
        attacker.getCombat().getDamageCache().add(defender, recoil);
    }

}
