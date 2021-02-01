package com.rageps.combat.listener.other.prayer.curses;

import com.rageps.combat.listener.SimplifiedListener;
import com.rageps.content.skill.Skills;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.player.Player;

public class WrathListener extends SimplifiedListener<Player> {

    @Override
    public void onDeath(Actor attacker, Player defender, Hit hit) {

        int maxDamage = (int) ((float) defender.getSkills()[Skills.PRAYER].getCurrentLevel() * 0.30F);





        super.onDeath(attacker, defender, hit);
    }
}
