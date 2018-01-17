package net.edge.world.entity.actor.combat.attack.listener.other.prayer.curses.leech;

import net.edge.content.skill.prayer.Prayer;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.player.Player;

import static net.edge.world.Animation.AnimationPriority.HIGH;

public class LeechDefenceListener extends SimplifiedListener<Player> {

    @Override
    public void start(Player attacker, Actor defender, Hit[] hits) {
        if (attacker.curseManager.isActivated(Prayer.LEECH_DEFENCE)) {
            defender.graphic(new Graphic(2232, 0, 2));
            attacker.animation(new Animation(12575, HIGH));
            attacker.message("You leech the targets defence.");
            attacker.curseManager.deactivate(Prayer.LEECH_DEFENCE);
        }
    }

    @Override
    public int modifyDefenceLevel(Actor attacker, Player defender, int level) {
        return defender.curseManager.modifyOutgoingLevel(level, 5, 10, Prayer.LEECH_DEFENCE);
    }

}
