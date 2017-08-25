package net.edge.content.combat.attack.listener.impl;

import net.edge.content.combat.CombatType;
import net.edge.content.combat.attack.listener.SimplifiedListener;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.hit.HitIcon;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

/**
 * no more bugs
 */
public class VengenceListener extends SimplifiedListener<Player> {

    @Override
    public void block(Actor attacker, Player defender, Hit hit, CombatType combatType) {
        Hit recoil = new Hit(hit.getDamage(), HitIcon.DEFLECT);
        attacker.damage(recoil);
        attacker.getCombat().getDamageCache().add(defender, recoil.getDamage());

        defender.forceChat("Taste vengeance!");
        defender.getCombat().removeListener(this);
        defender.setVenged(false);
    }

}
