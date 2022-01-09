package com.rageps.combat.listener.other.prayer.curses;

import com.rageps.combat.listener.SimplifiedListener;
import com.rageps.content.skill.Skills;
import com.rageps.net.refactor.packet.out.model.GraphicPacket;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.HitIcon;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Graphic;

public class WrathListener extends SimplifiedListener<Player> {

    @Override
    public void preDeath(Actor attacker, Player defender, Hit hit) {
        defender.graphic(new Graphic(2259));
        int x = defender.getPosition().getX() - 3;
        int y = defender.getPosition().getY() - 2;
        for (int i = 0; i < 25; i++) {
            x++;
            if (i == 5 || i == 10 || i == 15 || i == 20) {
                x -= 5;
                y++;
            }
            if (i % 2 == 1)
                continue;
            GraphicPacket.local(defender, 2260, new Position(x, y, defender.getPosition().getZ()), 25);
        }
        int maxHit = (int) ((defender.getSkills()[Skills.PRAYER].getCurrentLevel() / 100.D) * 25);
        if (defender.inMulti()) {
            defender.getLocalMobs().stream().filter(n -> n.getPosition().withinDistance(defender.getPosition(), 3)).forEach(h -> h.damage(new Hit(RandomUtils.inclusive(maxHit), Hitsplat.NORMAL, HitIcon.NONE)));
            if (defender.inWilderness()) {
                defender.getLocalPlayers().stream().filter(p -> p.getPosition().withinDistance(defender.getPosition(), 3)).forEach(h -> h.damage(new Hit(RandomUtils.inclusive(maxHit), Hitsplat.NORMAL, HitIcon.NONE)));
            }
        } else {
            Actor victim = defender.getCombat().getLastDefender();
            if (victim != null && victim.getPosition().withinDistance(defender.getPosition(), 3)) {
                victim.damage(new Hit(RandomUtils.inclusive(maxHit), Hitsplat.NORMAL, HitIcon.NONE));
            }
        }
    }
}
