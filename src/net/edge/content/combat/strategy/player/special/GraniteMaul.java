package net.edge.content.combat.strategy.player.special;

import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.player.PlayerMeleeStrategy;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

public class GraniteMaul extends PlayerMeleeStrategy {
    private static final Animation ANIMATION = new Animation(1667, Animation.AnimationPriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(340);

    @Override
    public void attack(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        super.attack(attacker, defender, hit, hits);
        attacker.graphic(GRAPHIC);

        if (attacker.getNewCombat().getStrategy() == this) {
            attacker.getCombatSpecial().drain(attacker);
        }
    }

    @Override
    public void finish(Player attacker, Actor defender, Hit[] hits) {
        WeaponInterface.setStrategy(attacker);
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        return ANIMATION;
    }

    @Override
    public int getAttackDelay(Player attacker, Actor defender, FightType fightType) {
        return 1;
    }
}
