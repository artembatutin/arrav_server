package net.edge.content.combat.strategy.mob;

import com.google.common.collect.ImmutableList;
import net.edge.content.combat.Combat;
import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.strategy.Strategy;
import net.edge.content.skill.Skills;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Dave/Ophion
 * Date: 08/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class PhoenixStrategy implements Strategy {

    @Override
    public boolean canOutgoingAttack(Actor actor, Actor victim) {
        return actor.isMob();
    }

    @Override
    public CombatHit outgoingAttack(Actor actor, Actor victim) {
        return magic(actor, victim);
    }

    private CombatHit magic(Actor actor, Actor victim) {
        int dustAttackChance = RandomUtils.inclusive(100);
        CombatNormalSpell spell = (dustAttackChance > 20 ? FIRE_ATTACK : DUST_ATTACK);
        actor.animation(new Animation(11076));
        actor.setCurrentlyCasting(spell);
        spell.projectile(actor, victim).get().sendProjectile();
        return new CombatHit(actor, victim, 1, CombatType.MAGIC, false) {
            @Override
           public void postAttack(int counter) {
                for (int i = 0; i <= 6; i++) {
                    if (i == 3 || i == 5 || i == 2 || i == 1)
                        continue;
                    victim.toPlayer().getSkills()[i].decreaseLevel(1);
                }
            }
        };
    }

    @Override
    public boolean hitBack() {
        return true;
    }

    @Override
    public int attackDelay(Actor actor) {
        return 5;
    }

    @Override
    public int attackDistance(Actor actor) {
        return 10;
    }

    @Override
    public int[] getMobs() {
        return new int[]{8549};
    }

    private static final CombatNormalSpell DUST_ATTACK = new CombatNormalSpell() {

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.empty();
        }

        @Override
        public Optional<Projectile> projectile(Actor cast, Actor castOn) {
            return Optional.of(new Projectile(cast, castOn, 393, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(2739, 100));
        }

        @Override
        public int maximumHit() {
            return 290;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return -1;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public int levelRequired() {
            return -1;
        }

        @Override
        public int spellId() {
            return -1;
        }
    };

    private static final CombatNormalSpell FIRE_ATTACK = new CombatNormalSpell() {

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.empty();
        }

        @Override
        public Optional<Projectile> projectile(Actor cast, Actor castOn) {
            return Optional.of(new Projectile(cast, castOn, 393, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(2739, 100));
        }

        @Override
        public int maximumHit() {
            return 290;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return -1;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public int levelRequired() {
            return -1;
        }

        @Override
        public int spellId() {
            return -1;
        }
    };

}
