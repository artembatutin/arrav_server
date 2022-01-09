package com.rageps.combat.listener.other.prayer.curses;

import com.rageps.combat.listener.SimplifiedListener;
import com.rageps.world.model.Projectile;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.player.Player;

public class SoulsplitListener extends SimplifiedListener<Player> {


    @Override
    public void attack(Player attacker, Actor defender, Hit hit) {
        Projectile proj = new Projectile(attacker, defender, 2263, 44, 3, 43, 31);
        int heal = (int) (hit.getDamage() * 0.20F);

        defender.delay(1, () ->{
            proj.sendProjectile();
            defender.graphic(2264);
        });

        defender.delay(2, () ->{
            attacker.healEntity(heal);
            new Projectile(defender, attacker, 2263, 44, 3, 43, 31).sendProjectile();
        });

        super.attack(attacker, defender, hit);
    }
}
